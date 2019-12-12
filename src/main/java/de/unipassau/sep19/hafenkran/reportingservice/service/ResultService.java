package de.unipassau.sep19.hafenkran.reportingservice.service;

import de.unipassau.sep19.hafenkran.reportingservice.dto.ResultDTO;
import lombok.NonNull;

import java.util.List;
import java.util.UUID;

/**
 * The ResultService is responsible for handling all relevant actions for retrieving and saving new results for executions.
 */
public interface ResultService {

    List<ResultDTO> retrieveResultDTOListByExecutionId(@NonNull UUID executionId);

}
