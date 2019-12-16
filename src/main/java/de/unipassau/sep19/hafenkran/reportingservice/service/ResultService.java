package de.unipassau.sep19.hafenkran.reportingservice.service;

import de.unipassau.sep19.hafenkran.reportingservice.dto.ResultDTO;
import de.unipassau.sep19.hafenkran.reportingservice.model.Result;
import lombok.NonNull;

import java.util.List;
import java.util.UUID;

/**
 * The ResultService is responsible for handling all relevant actions for retrieving and saving new results for executions.
 */
public interface ResultService {

    /**
     * Retrieves the results in an {@link de.unipassau.sep19.hafenkran.reportingservice.dto.ResultDTOList} of an
     * execution with the {@code executionId}.
     *
     * @param executionId The execution to get the results from.
     * @return An {@link de.unipassau.sep19.hafenkran.reportingservice.dto.ResultDTOList} with all results in it.
     */
    List<ResultDTO> retrieveResultDTOListByExecutionId(@NonNull UUID executionId);

    /**
     * Retrieves the whole tar containing the execution results as base64.
     *
     * @param executionId the id of the target execution.
     * @param refresh whether to refresh the files which are already stored.
     * @return the base64 encoded tar file.
     */
    byte[] downloadResultsAsBase64(@NonNull UUID executionId, boolean refresh);

}
