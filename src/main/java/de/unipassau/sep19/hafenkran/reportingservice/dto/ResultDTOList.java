package de.unipassau.sep19.hafenkran.reportingservice.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import de.unipassau.sep19.hafenkran.reportingservice.model.Result;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * The Data Transfer Object (DTO) representation of a list of {@link Result}s.
 */
@Data
@AllArgsConstructor(onConstructor = @__(@JsonCreator))
public class ResultDTOList {

    /*@NonNull
    @JsonProperty("id")
    private final UUID id;*/

    @NonNull
    @JsonProperty("executionId")
    private final UUID executionId;

    @NonNull
    @JsonProperty("updatedAt")
    private final LocalDateTime updatedAt;

    @NonNull
    @JsonProperty("resultList")
    private final List<ResultDTO> results;

    @JsonCreator
    public ResultDTOList(@NonNull List<Result> resultsList) {
        //this.id = resultsList.get(0).getId();
        this.executionId = resultsList.get(0).getExecutionId();
        this.updatedAt = resultsList.get(0).getCreatedAt();

        if (resultsList.isEmpty()) {
            this.results = Collections.emptyList();
        } else {
            this.results = convertResultListToDTOList(resultsList);
        }
    }

    /**
     * Converts a list of {@link Result}s into a {@link ResultDTOList}.
     *
     * @param resultsList The list of {@link Result}s that is going to be converted.
     * @return The converted {@link ResultDTOList}.
     */
    public static List<ResultDTO> convertResultListToDTOList(
            @NonNull @NotEmpty List<Result> resultsList) {

        return resultsList.stream()
                .map(ResultDTO::fromResults).collect(Collectors.toList());
    }

}
