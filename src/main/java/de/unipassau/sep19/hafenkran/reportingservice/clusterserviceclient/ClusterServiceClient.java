package de.unipassau.sep19.hafenkran.reportingservice.clusterserviceclient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ClusterServiceClient {

    @Value("${clusterservice.path}")
    private String basePath;

    public <T> T get(String path, Class<T> responseType) {
        RestTemplate rt = new RestTemplate();
        String targetPath = basePath + path;
        ResponseEntity<T> response = rt.exchange(basePath + path, HttpMethod.GET, null, responseType);

        if (!HttpStatus.Series.valueOf(response.getStatusCode()).equals(HttpStatus.Series.SUCCESSFUL)) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    String.format("Could not retrieve data from %s. Reason: %s %s", targetPath,
                            response.getStatusCodeValue(), response.getBody()));
        }

        return response.getBody();
    }

}
