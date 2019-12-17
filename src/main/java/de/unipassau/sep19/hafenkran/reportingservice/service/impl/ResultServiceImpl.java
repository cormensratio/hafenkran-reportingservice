package de.unipassau.sep19.hafenkran.reportingservice.service.impl;

import de.unipassau.sep19.hafenkran.reportingservice.clusterserviceclient.ClusterServiceClient;
import de.unipassau.sep19.hafenkran.reportingservice.dto.ResultDTO;
import de.unipassau.sep19.hafenkran.reportingservice.dto.ResultDTOList;
import de.unipassau.sep19.hafenkran.reportingservice.model.Results;
import de.unipassau.sep19.hafenkran.reportingservice.repository.ResultRepository;
import de.unipassau.sep19.hafenkran.reportingservice.service.ResultService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static java.util.Base64.getEncoder;
import static org.apache.commons.codec.binary.Base64.*;

/**
 * {@inheritDoc}
 */
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
@Service
@Slf4j
public class ResultServiceImpl implements ResultService {

    @NonNull
    private final ResultRepository resultRepository;

    @NonNull
    private ClusterServiceClient csClient;

    @Value("${results.storage-path}")
    private String storagePath;

    /**
     * {@inheritDoc}
     */
    @Override
    public ResultDTOList retrieveResultDTOListByExecutionId(@NonNull UUID executionId) {
        List<Results> resultList = findResultListByExecutionId(executionId);
        List<ResultDTO> resultDTOList = new ArrayList<>();
        for (Results result : resultList) {
            ResultDTO resultDTO = new ResultDTO(
                                        result.getId(),
                                        result.getType(),
                                        encodeFileToBase64(result.getPath()));
            resultDTOList.add(resultDTO);
        }
        return new ResultDTOList(resultDTOList, executionId, LocalDateTime.now());
    }

    private List<Results> findResultListByExecutionId(@NonNull UUID executionId) {
        List<Results> resultsByExecutionId = resultRepository.findAllByExecutionId(executionId);
        resultsByExecutionId.forEach(Results::validatePermissions);
        return resultsByExecutionId;
    }

    private String encodeFileToBase64(@NonNull String file) {
        byte[] fileContent;
        try {
            fileContent = Files.readAllBytes(Paths.get(file));
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The file " + file + " couldn't be found.", e);
        }
        return getEncoder().encodeToString(fileContent);
    }

    /**
     * {@inheritDoc}
     */
    public byte[] downloadResultsAsBase64(@NonNull UUID executionId, boolean refresh) {
        if (refresh) {
            retrieveRemoteResultsOfExecution(executionId, Paths.get(storagePath + "/" + executionId).normalize());
        }

        File targetFile = new File(storagePath + "/" + executionId + ".tar");
        try {
            return encodeBase64(FileUtils.readFileToString(targetFile, Charset.defaultCharset()).getBytes());
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Could not read the results of " + executionId + " from the file system", e);
        }
    }

    /**
     * Retrieves the results from the execution in the ClusterService and removes old files stored for the given execution.
     *
     * @param executionId the id of the execution
     */
    private void retrieveRemoteResultsOfExecution(@NonNull UUID executionId, @NonNull Path folderPath) {
        try (ByteArrayInputStream resultsStream = new ByteArrayInputStream(
                csClient.retrieveResultsForExecutionId(executionId).getBytes());
             InputStream is = new Base64InputStream(resultsStream)) {

            cleanupOldFiles(executionId, folderPath);
            File file = saveTar(executionId, is, folderPath);
            extractResults(executionId, file, folderPath);

        } catch (IOException e) {
            throw new IllegalStateException("Could not read archive with results for execution " + executionId, e);
        }
    }

    private void extractResults(@NonNull UUID executionId, @NonNull File targetFile, @NonNull Path folderPath) throws IOException {
        // Extract important files and save them to the database
        ArchiveInputStream is = new TarArchiveInputStream(new FileInputStream(targetFile));
        TarArchiveEntry entry;
        while ((entry = (TarArchiveEntry) is.getNextEntry()) != null) {
            final File outputFile = Paths.get(folderPath.toString() + "/" + entry.getName()).normalize().toFile();

            // Only untar important files which can be rendered at the client
            String fileName = outputFile.toPath().getFileName().toString();
            String typeString = fileName.substring(fileName.lastIndexOf(".") + 1);
            Results.ResultType type;
            if (StringUtils.isEmpty(typeString)) {
                continue;
            } else if (typeString.equals("csv")) {
                type = Results.ResultType.CSV;
            } else if (typeString.equals("log")) {
                type = Results.ResultType.LOG;
            } else {
                continue;
            }

            if (entry.isDirectory()) {
                if (!outputFile.exists()) {
                    if (!outputFile.mkdirs()) {
                        throw new IllegalStateException(
                                String.format("Couldn't create directory %s.", outputFile.getAbsolutePath()));
                    }
                }
            } else {
                File parent = outputFile.getParentFile();
                if (!parent.exists()) {
                    boolean mkdirs = parent.mkdirs();
                }
                final OutputStream outputFileStream = new FileOutputStream(outputFile);
                IOUtils.copy(is, outputFileStream);
                outputFileStream.close();
            }

            resultRepository.save(new Results(executionId, type, fileName, outputFile.getPath()));
        }
    }

    private File saveTar(@NonNull UUID executionId, @NonNull InputStream debInputStream, @NonNull Path folderPath) {
        File outputFile = Paths.get(folderPath.toString() + ".tar").normalize().toFile();
        try {
            final OutputStream outputFileStream = new FileOutputStream(outputFile);
            IOUtils.copy(debInputStream, outputFileStream);
            outputFileStream.close();
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Could not store the results of " + executionId + " to the file system", e);
        }
        return outputFile;
    }

    private void cleanupOldFiles(@NonNull UUID executionId, @NonNull Path folderPath) {
        deleteDirectoryRecursion(folderPath);
        // resultRepository.deleteResultsByExecutionId(executionId);
        resultRepository.deleteAll();
    }

    private void deleteDirectoryRecursion(@NonNull Path path) {
        if (Files.isDirectory(path, LinkOption.NOFOLLOW_LINKS)) {
            try (DirectoryStream<Path> entries = Files.newDirectoryStream(path)) {
                for (Path entry : entries) {
                    deleteDirectoryRecursion(entry);
                }
            } catch (IOException e) {
                // ignore exception if directory does not exist
            }
        }

        try {
            Files.delete(path);
            Files.delete(Paths.get(path.toString() + ".tar").normalize());
        } catch (IOException e) {
            // ignore exception if file does not exist
        }
    }

}
