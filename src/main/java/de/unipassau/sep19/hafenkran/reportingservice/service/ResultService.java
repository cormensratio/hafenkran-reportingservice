package de.unipassau.sep19.hafenkran.reportingservice.service;

import de.unipassau.sep19.hafenkran.reportingservice.dto.CSResultDTO;
import de.unipassau.sep19.hafenkran.reportingservice.dto.ResultDTOList;
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
    ResultDTOList retrieveResultDTOListByExecutionId(@NonNull UUID executionId);

    /**
     * Retrieves the whole tar containing the execution results as base64.
     *
     * @param executionId the id of the target execution.
     * @param refresh whether to refresh the files which are already stored.
     * @return the base64 encoded tar file.
     */
    byte[] downloadResultsAsBase64(@NonNull UUID executionId, boolean refresh);

    /**
     * Persist the results from the {@link CSResultDTO} for the given owner and execution id.
     *
     * @param resultDTO the {@link CSResultDTO} containing the relevant information for storing the results.
     */
    void persistResults(@NonNull CSResultDTO resultDTO);

    /**
     * Deletes the results of an execution.
     *
     * @param executionIdList A list of ids of the executions which results should be deleted.
     */
    void deleteResults(@NonNull List<UUID> executionIdList);

}
