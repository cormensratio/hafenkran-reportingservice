package de.unipassau.sep19.hafenkran.reportingservice.service;

import lombok.NonNull;

import java.util.UUID;

/**
 * The ResultService is responsible for handling all relevant actions for retrieving and saving new results for executions.
 */
public interface ResultService {

    /**
     * Retrieves the whole tar containing the execution results as base64.
     *
     * @param executionId the id of the target execution.
     * @param refresh whether to refresh the files which are already stored.
     * @return the base64 encoded tar file.
     */
    byte[] downloadResultsAsBase64(@NonNull UUID executionId, boolean refresh);

}
