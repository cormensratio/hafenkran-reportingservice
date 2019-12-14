package de.unipassau.sep19.hafenkran.reportingservice.service.impl;

import de.unipassau.sep19.hafenkran.reportingservice.clusterserviceclient.ClusterServiceClient;
import de.unipassau.sep19.hafenkran.reportingservice.model.Result;
import de.unipassau.sep19.hafenkran.reportingservice.service.ResultService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.io.*;
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
    private ClusterServiceClient csClient;

    @Value()

    private List<Result> storeRemoteResultsOfExecutionToFile(UUID executionId) {
        String response = csClient.get(String.format("/executions/%s/results", executionId), String.class);
        InputStream is = new Base64InputStream(new ByteArrayInputStream(response.getBytes()));
        ArchiveInputStream archive = new TarArchiveInputStream(is);

        File targetFile = new File("src/main/resources/targetFile.tmp");

        try {
            FileUtils.copyInputStreamToFile(archive, targetFile);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not resolve the results file to the file system", e);
        }
    }
}
