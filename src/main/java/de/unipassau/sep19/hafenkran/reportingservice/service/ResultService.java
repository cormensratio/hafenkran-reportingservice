package de.unipassau.sep19.hafenkran.reportingservice.service;

import lombok.NonNull;

import java.util.UUID;

/**
 * The ResultService is responsible for handling all relevant actions for retrieving and saving new results for executions.
 */
public interface ResultService {

    byte[] downloadResultsAsBase64(@NonNull UUID executionId, boolean refresh);

}
