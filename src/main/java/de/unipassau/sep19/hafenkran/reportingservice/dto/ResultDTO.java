package de.unipassau.sep19.hafenkran.reportingservice.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import de.unipassau.sep19.hafenkran.reportingservice.model.Results;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import java.util.UUID;

/**
 * The Data Transfer Object (DTO) representation of an {@link Results}.
 */
@Data
@AllArgsConstructor(onConstructor = @__(@JsonCreator))
public class ResultDTO {

    @NonNull
    @JsonProperty("id")
    private final UUID id;

    @NonNull
    @JsonProperty("type")
    private final Results.ResultType type;

    @NonNull
    @JsonProperty("file")
    private String file;

}
