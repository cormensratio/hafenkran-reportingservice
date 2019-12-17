package de.unipassau.sep19.hafenkran.reportingservice.model;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import java.util.UUID;

/**
 * Represents execution results for storage in the reporting service
 */
@Data
@Table(name = "results")
@Entity
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Results extends Resource {

    @NonNull
    @Column(name = "execution_id")
    private UUID executionId;

    @NonNull
    private ResultType type;

    @NonNull
    private String name;

    @NonNull
    @NotBlank
    private String path;

    public Results(@NonNull UUID executionId, @NonNull ResultType type, @NonNull @NotBlank String name, @NonNull @NotBlank String path) {
        super();
        this.executionId = executionId;
        this.type = type;
        this.name = name;
        this.path = path;
    }

    public enum ResultType {
        CSV, LOG
    }
}
