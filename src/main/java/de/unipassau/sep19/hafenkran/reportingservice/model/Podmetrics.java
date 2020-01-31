package de.unipassau.sep19.hafenkran.reportingservice.model;

import de.unipassau.sep19.hafenkran.reportingservice.dto.CsPodmetricsDTO;
import lombok.*;

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

    public Podmetrics(@NonNull UUID ownerId, @NonNull UUID executionId, @NonNull UUID experimentId,
                      @NonNull Timestamp timestamp, @NonNull String cpu, @NonNull String memory){
        super(ownerId);
        this.experimentId = experimentId;
        this.executionId = executionId;
        this.timestamp = timestamp;
        this.cpu = cpu;
        this.memory = memory;
    }

    public static Podmetrics fromPodmetricsDTO(@NonNull CsPodmetricsDTO csPodmetricsDTO) {
        return new Podmetrics(csPodmetricsDTO.getOwnerId(), csPodmetricsDTO.getExecutionId(),
                UUID.fromString(csPodmetricsDTO.getMetadata().getNamespace()),
                csPodmetricsDTO.getTimestamp(), csPodmetricsDTO.getContainers().get(0).getUsage().getCpu(),
                csPodmetricsDTO.getContainers().get(0).getUsage().getMemory());
    }
}
