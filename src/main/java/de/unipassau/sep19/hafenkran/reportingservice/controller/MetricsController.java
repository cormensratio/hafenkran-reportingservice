package de.unipassau.sep19.hafenkran.reportingservice.controller;

import de.unipassau.sep19.hafenkran.reportingservice.dto.MetricsDTOList;
import de.unipassau.sep19.hafenkran.reportingservice.service.PodmetricsService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * The REST-Controller for metrics specific POST- and GET-Endpoints.
 */
@Slf4j
@RestController
@RequestMapping("/metrics")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MetricsController {

    private final PodmetricsService podmetricsService;

    /**
     * GET-Endpoint for receiving all metrics of an execution with the {@code executionId}.
     *
     * @param executionId The execution to get the metrics from.
     * @return An {@link de.unipassau.sep19.hafenkran.reportingservice.dto.MetricsDTOList}
     * of all metrics from the execution.
     */
    @GetMapping("/{executionId}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public MetricsDTOList getMetricsForExecutionId(@NonNull @PathVariable UUID executionId) {
        return podmetricsService.retrieveMetricsDTOListByExecutionId(executionId);
    }
}
