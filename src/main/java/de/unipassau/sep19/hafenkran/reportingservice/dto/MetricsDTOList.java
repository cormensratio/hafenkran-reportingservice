package de.unipassau.sep19.hafenkran.reportingservice.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import de.unipassau.sep19.hafenkran.reportingservice.model.Podmetrics;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * The Data Transfer Object (DTO) representation of a list of {@link Podmetrics}.
 */
@Data
@AllArgsConstructor(onConstructor = @__(@JsonCreator))
public class MetricsDTOList {

    @NonNull
    @JsonProperty("executionId")
    private final UUID executionId;

    @NonNull
    @JsonProperty("metricsList")
    private final List<MetricsDTO> metrics;

    @JsonCreator
    public MetricsDTOList(List<MetricsDTO> metricsDTOList, @NonNull UUID executionId) {
        this.executionId = executionId;

        if (metricsDTOList.isEmpty()) {
            this.metrics = Collections.emptyList();
        } else {
            this.metrics = metricsDTOList;
        }
    }
}

