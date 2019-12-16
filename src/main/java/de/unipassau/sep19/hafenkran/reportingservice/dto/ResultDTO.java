package de.unipassau.sep19.hafenkran.reportingservice.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import de.unipassau.sep19.hafenkran.reportingservice.model.Result;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.UUID;

/**
 * The Data Transfer Object (DTO) representation of an {@link Result}.
 */
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
    @JsonProperty("fileContent")
    private final String fileContent;

    public static ResultDTO fromResults(@NonNull final Result result) {
        return new ResultDTO(
                result.getId(),
                result.getType(),
                retrieveFileAsBase64String(result)
        );
    }

    private static String retrieveFileAsBase64String(@NonNull final Result result) {
        byte[] fileContent = new byte[0];
        try {
            fileContent = Files.readAllBytes(Paths.get(result.getPath()));
        } catch (IOException e) {
            return Base64.getEncoder().encodeToString(fileContent);
        }
        return Base64.getEncoder().encodeToString(fileContent);
    }
}
