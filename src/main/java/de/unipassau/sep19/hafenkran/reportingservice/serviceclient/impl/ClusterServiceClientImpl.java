package de.unipassau.sep19.hafenkran.reportingservice.serviceclient.impl;

import de.unipassau.sep19.hafenkran.reportingservice.dto.CsPodmetricsDTO;
import de.unipassau.sep19.hafenkran.reportingservice.service.PodmetricsService;
import de.unipassau.sep19.hafenkran.reportingservice.serviceclient.ClusterServiceClient;
import de.unipassau.sep19.hafenkran.reportingservice.serviceclient.ServiceClient;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ClusterServiceClientImpl implements ClusterServiceClient {

    private final ServiceClient serviceClient;

    private final PodmetricsService podmetricsService;

    @Value("${cluster-service-uri}")
    private String clusterServiceURI;

    @Value("${service-user.secret}")
    private String serviceSecret;

    /**
     * {@inheritDoc}
     **/
    public String retrieveResultsForExecutionId(@NonNull UUID executionId) {
        return serviceClient.get(String.format("/executions/%s/results?secret" +
                        "=%s", executionId, serviceSecret),
                String.class, null);
    }

    @Scheduled(fixedRateString = "#{${metrics.frequency} * 1000}",
            initialDelayString = "#{${metrics.initial-delay} * 1000}")
    public void retrieveAllPodmetrics() {
        CsPodmetricsDTO[] podmetricsDTOs = serviceClient.get(clusterServiceURI + "/metrics?secret=" + serviceSecret,
                CsPodmetricsDTO[].class, null);
        podmetricsService.savePodmetrics(podmetricsDTOs);
    }
}
