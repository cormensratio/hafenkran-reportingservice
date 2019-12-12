package de.unipassau.sep19.hafenkran.reportingservice.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import de.unipassau.sep19.hafenkran.reportingservice.model.Result;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import java.io.File;
import java.nio.file.Paths;
import java.util.UUID;

@Data
@AllArgsConstructor(onConstructor = @__(@JsonCreator))
public class ResultDTO {

    @NonNull
    @JsonProperty("id")
    private final UUID id;

    @NonNull
    @JsonProperty("type")
    private final Result.ResultType type;

    @NonNull
    @JsonProperty("file")
    private final File file;

    public static ResultDTO fromResults(@NonNull final Result result) {
        return new ResultDTO(
                result.getId(),
                result.getType(),
                Paths.get(result.getPath()).toFile()
        );
    }
}
