package de.unipassau.sep19.hafenkran.reportingservice.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import de.unipassau.sep19.hafenkran.reportingservice.model.Podmetrics;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import java.sql.Timestamp;

/**
 * The Data Transfer Object (DTO) representation of an {@link Podmetrics}.
 */
@Data
@AllArgsConstructor(onConstructor = @__(@JsonCreator))
public class MetricsDTO {

    @NonNull
    @JsonProperty("cpu")
    private final String cpu;

    @NonNull
    @JsonProperty("memory")
    private final String memory;

    @NonNull
    @JsonProperty("timestamp")
    private Timestamp timestamp;

}
