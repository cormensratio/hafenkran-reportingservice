package de.unipassau.sep19.hafenkran.reportingservice.serviceclient;

import lombok.NonNull;

import java.util.UUID;

/**
 * Provides methods for interactions with the ClusterService.
 */
public interface ClusterServiceClient {
    /**
     * Retrieves the results for a given executionId.
     *
     * @param executionId the {@link UUID} of the target execution.
     * @return the Base64 encoded result string.
     */
    String retrieveResultsForExecutionId(@NonNull UUID executionId);
}
