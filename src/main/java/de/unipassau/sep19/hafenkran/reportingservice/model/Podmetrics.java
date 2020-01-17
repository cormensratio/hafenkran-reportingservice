package de.unipassau.sep19.hafenkran.reportingservice.model;

import de.unipassau.sep19.hafenkran.reportingservice.dto.CsPodmetricsDTO;
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
    private Timestamp timestamp;

    @NonNull
    private String cpu;

    @NonNull
    private String memory;

    public Podmetrics (@NonNull CsPodmetricsDTO csPodmetricsDTO) {
        super(csPodmetricsDTO.getOwnerId());
        this.executionId = csPodmetricsDTO.getExecutionId();
        this.experimentId = UUID.fromString(csPodmetricsDTO.getMetadata().getNamespace());
        this.cpu = csPodmetricsDTO.getContainers().get(0).getUsage().getCpu();
        this.memory = csPodmetricsDTO.getContainers().get(0).getUsage().getMemory();
        this.timestamp = csPodmetricsDTO.getTimestamp();
    }
}
