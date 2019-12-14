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
     * execution with the {@code executionId} and the {@code resultType}.
     *
     * @param executionId The execution to get the results from.
     * @param resultType The type that the results should have (can either be csv or log).
     * @return An {@link de.unipassau.sep19.hafenkran.reportingservice.dto.ResultDTOList} with all results in it.
     */
    List<ResultDTO> retrieveResultDTOListByExecutionIdAndType(@NonNull UUID executionId, @NonNull Result.ResultType resultType);

}
