package de.unipassau.sep19.hafenkran.reportingservice.serviceclient.impl;

import de.unipassau.sep19.hafenkran.reportingservice.serviceclient.ClusterServiceClient;
import de.unipassau.sep19.hafenkran.reportingservice.serviceclient.ServiceClient;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ClusterServiceClientImpl implements ClusterServiceClient {

    private final ServiceClient serviceClient;

    @Value("${service-user.secret}")
    private String serviceSecret;

    /**
     * {@inheritDoc}
     **/
    public String retrieveResultsForExecutionId(@NonNull UUID executionId) {
        return serviceClient.get(String.format("/executions/%s/results?secret=" + serviceSecret, executionId),
                String.class, null);
    }
}
