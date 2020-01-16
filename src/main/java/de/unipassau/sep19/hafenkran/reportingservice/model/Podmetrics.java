package de.unipassau.sep19.hafenkran.reportingservice.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.UUID;

@Data
@Table(name = "podmetrics")
@Entity
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Podmetrics extends Resource {

    @NonNull
    @Column(name = "execution_id")
    private UUID executionId;

    @NonNull
    private UUID experimentId;

    @NonNull
    private String cpu;

    @NonNull
    private String memory;

    @NonNull
    private Timestamp timestamp;

    public Podmetrics(@NonNull UUID executionId, @NonNull UUID experimentId, @NonNull String cpu, @NonNull String memory, @NonNull Timestamp timestamp) {
        super();
        this.executionId = executionId;
        this.experimentId = experimentId;
        this.cpu = cpu;
        this.memory = memory;
        this.timestamp = timestamp;
    }
}
