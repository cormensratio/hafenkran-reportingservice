package de.unipassau.sep19.hafenkran.reportingservice.service.impl;

import de.unipassau.sep19.hafenkran.reportingservice.clusterserviceclient.ClusterServiceClient;
import de.unipassau.sep19.hafenkran.reportingservice.dto.ResultDTO;
import de.unipassau.sep19.hafenkran.reportingservice.dto.ResultDTOList;
import de.unipassau.sep19.hafenkran.reportingservice.model.Result;
import de.unipassau.sep19.hafenkran.reportingservice.repository.ResultRepository;
import de.unipassau.sep19.hafenkran.reportingservice.service.ResultService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.List;
import java.util.UUID;

/**
 * {@inheritDoc}
 */
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
@Component
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
    public List<ResultDTO> retrieveResultDTOListByExecutionId(@NonNull UUID executionId) {
        List<ResultDTO> resultDTOList = ResultDTOList.convertResultListToDTOList(findResultListByExecutionId(executionId));
        for (ResultDTO resultDTO : resultDTOList) {
            encodeFileToBase64(resultDTO);
        }
        return  resultDTOList;
    }

    private List<Result> findResultListByExecutionId(@NonNull UUID executionId) {
        List<Result> resultsByExecutionId = resultRepository.findAllByExecutionId(executionId);
        resultsByExecutionId.forEach(Result::validatePermissions);
        return resultsByExecutionId;
    }

    private void encodeFileToBase64(@NonNull ResultDTO resultDTO) {
        byte[] fileContent = new byte[0];
        try {
            fileContent = Files.readAllBytes(Paths.get(resultDTO.getFileContent()));
        } catch (IOException e) {
            // ignore exception if file is empty
        }
        resultDTO.setFileContent(Base64.getEncoder().encodeToString(fileContent));
    }

    /**
     * {@inheritDoc}
     */
    public byte[] downloadResultsAsBase64(@NonNull UUID executionId, boolean refresh) {
        if (refresh) {
            retrieveRemoteResultsOfExecution(executionId);
        }

        File targetFile = new File(storagePath + "/" + executionId + ".tar");
        try {
            return FileUtils.readFileToString(targetFile, Charset.defaultCharset()).getBytes();
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not read the results of " + executionId + " from the file system", e);
        }
    }

    /**
     * Retrieves the results from the execution in the ClusterService and removes old files stored for the given execution.
     *
     * @param executionId the id of the execution
     */
    private void retrieveRemoteResultsOfExecution(@NonNull UUID executionId) {
        try (ByteArrayInputStream resultsStream = new ByteArrayInputStream(csClient.retrieveResultsForExecutionId(executionId).getBytes());
             InputStream is = new Base64InputStream(resultsStream);
             TarArchiveInputStream debInputStream = (TarArchiveInputStream) new ArchiveStreamFactory().createArchiveInputStream("tar", is)) {

            cleanupOldFiles(executionId);
            saveTar(executionId, debInputStream);
            extractResults(executionId, debInputStream);

        } catch (ArchiveException | IOException e) {
            throw new IllegalStateException("Could not read archive with results for execution " + executionId, e);
        }
    }

    private void extractResults(@NonNull UUID executionId, @NonNull TarArchiveInputStream debInputStream) throws IOException {
        // Extract important files and save them to the database
        TarArchiveEntry entry;
        while ((entry = (TarArchiveEntry) debInputStream.getNextEntry()) != null) {
            final File outputFile = new File(storagePath + "/" + executionId + "/", entry.getName());

            // Only untar important files which can be rendered at the client
            String[] typeString = outputFile.toString().split(".");
            Result.ResultType type;
            if (typeString[typeString.length - 1].equals("csv")) {
                type = Result.ResultType.CSV;
            } else if (typeString[typeString.length - 1].equals("log")) {
                type = Result.ResultType.LOG;
            } else {
                continue;
            }

            if (entry.isDirectory()) {
                if (!outputFile.exists()) {
                    if (!outputFile.mkdirs()) {
                        throw new IllegalStateException(String.format("Couldn't create directory %s.", outputFile.getAbsolutePath()));
                    }
                }
            } else {
                final OutputStream outputFileStream = new FileOutputStream(outputFile);
                IOUtils.copy(debInputStream, outputFileStream);
                outputFileStream.close();
            }

            resultRepository.save(new Result(executionId, type, outputFile.getPath()));
        }
    }

    private void saveTar(@NonNull UUID executionId, @NonNull TarArchiveInputStream debInputStream) {
        File targetFile = new File(storagePath + "/" + executionId + ".tar");
        try {
            FileUtils.copyInputStreamToFile(debInputStream, targetFile);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not store the results of " + executionId + " to the file system", e);
        }
    }

    private void cleanupOldFiles(@NonNull UUID executionId) {
        Path targetPath = Paths.get(storagePath + "/" + executionId);
        deleteDirectoryRecursion(targetPath);
        resultRepository.deleteAllByExecutionId(executionId);
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
        } catch (IOException e) {
            // ignore exception if file does not exist
        }
    }

}