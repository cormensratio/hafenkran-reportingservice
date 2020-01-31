package de.unipassau.sep19.hafenkran.reportingservice.service;

import de.unipassau.sep19.hafenkran.reportingservice.dto.CsPodmetricsDTO;
import de.unipassau.sep19.hafenkran.reportingservice.dto.MetricsDTOList;
import de.unipassau.sep19.hafenkran.reportingservice.model.Podmetrics;
import lombok.NonNull;

import java.util.UUID;

/**
 * The PodmetricsService is responsible for handling all relevant actions for retrieving and
 * saving new metrics for executions.
 */
public interface PodmetricsService {

    /**
     * Retrieves the podmetrics in an {@link de.unipassau.sep19.hafenkran.reportingservice.dto.MetricsDTOList} of an
     * execution with the {@code executionId}.
     *
     * @param executionId The execution to get the metrics from.
     * @return An {@link de.unipassau.sep19.hafenkran.reportingservice.dto.MetricsDTOList} with all metrics in it.
     */
    MetricsDTOList retrieveMetricsDTOListByExecutionId(@NonNull UUID executionId);

    /**
     * Saves the {@link CsPodmetricsDTO} Array as {@link Podmetrics} in the database.
     *
     * @param csPodmetricsDTOs The Array of metrics which should be persisted in the Database.
     */
    void savePodmetrics(@NonNull CsPodmetricsDTO[] csPodmetricsDTOs);
}
