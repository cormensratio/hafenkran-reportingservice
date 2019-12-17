package de.unipassau.sep19.hafenkran.reportingservice.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import de.unipassau.sep19.hafenkran.reportingservice.model.Results;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * The Data Transfer Object (DTO) representation of a list of {@link Results}.
 */
@Data
@AllArgsConstructor(onConstructor = @__(@JsonCreator))
public class ResultDTOList {

    @NonNull
    @JsonProperty("executionId")
    private final UUID executionId;

    @NonNull
    @JsonProperty("updatedAt")
    private LocalDateTime updatedAt;

    @NonNull
    @JsonProperty("resultList")
    private final List<ResultDTO> results;

    @JsonCreator
    public ResultDTOList(List<ResultDTO> resultDTOList, @NonNull UUID executionId, LocalDateTime updatedAt) {
        this.executionId = executionId;
        this.updatedAt = updatedAt;

        if (resultDTOList.isEmpty()) {
            this.results = Collections.emptyList();
        } else {
            this.results = resultDTOList;
        }
    }

}
