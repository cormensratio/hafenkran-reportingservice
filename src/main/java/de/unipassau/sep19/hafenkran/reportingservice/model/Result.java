package de.unipassau.sep19.hafenkran.reportingservice.model;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotBlank;
import java.util.UUID;

/**
 * Represents execution results for storage in the reporting service
 */
@Slf4j
@MappedSuperclass
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Result extends Resource {

    @NonNull
    private UUID executionId;

    @NonNull
    private ResultType type;

    @NonNull
    @NotBlank
    private String path;

    public enum ResultType{
        CSV, LOG
    }
}
