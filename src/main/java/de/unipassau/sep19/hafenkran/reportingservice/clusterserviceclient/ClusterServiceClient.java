package de.unipassau.sep19.hafenkran.reportingservice.clusterserviceclient;

import de.unipassau.sep19.hafenkran.reportingservice.util.SecurityContextUtil;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
public class ClusterServiceClient {

    @Value("${clusterservice.path}")
    private String basePath;

    private <T> T get(String path, Class<T> responseType) {
        RestTemplate rt = new RestTemplate();
        String targetPath = basePath + path;
        ResponseEntity<T> response = rt.exchange(basePath + path, HttpMethod.GET, authHeaders(), responseType);

        if (!HttpStatus.Series.valueOf(response.getStatusCode()).equals(HttpStatus.Series.SUCCESSFUL)) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    String.format("Could not retrieve data from %s. Reason: %s %s", targetPath,
                            response.getStatusCodeValue(), response.getBody()));
        }

        return response.getBody();
    }

    private HttpEntity authHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + SecurityContextUtil.getJWT());
        return new HttpEntity<>("body", headers);
    }

    public byte[] retrieveResultsForExecutionId(@NonNull UUID executionId){
        return get(String.format("/executions/%s/results", executionId), byte[].class);
    }
}
