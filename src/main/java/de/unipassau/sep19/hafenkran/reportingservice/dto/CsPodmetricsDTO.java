package de.unipassau.sep19.hafenkran.reportingservice.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class CsPodmetricsDTO {

    @JsonProperty("executionId")
    UUID executionId;

    UUID ownerId;

    MetadataDTO metadata;
    List<ContainerDTO> containers;

    Timestamp timestamp;

    @JsonCreator
    public CsPodmetricsDTO(@JsonProperty("executionId") UUID executionId, @JsonProperty("ownerId") UUID ownerId,
                     @JsonProperty("metadata") MetadataDTO metadata,
                     @JsonProperty("containers") List<ContainerDTO> containers,
                     @JsonProperty("timestamp") Timestamp timestamp) {
        this.executionId = executionId;
        this.ownerId = ownerId;
        this.metadata = metadata;
        this.containers = containers;
        this.timestamp = timestamp;
    }

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class MetadataDTO {
        String name;
        String namespace;

        @JsonCreator
        public MetadataDTO(
                @JsonProperty("name") String name,
                @JsonProperty("namespace")
                        String namespace) {
            this.name = name;
            this.namespace = namespace;
        }

    }

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ContainerDTO {

        UsageDTO usage;

        @JsonCreator
        public ContainerDTO(@JsonProperty("usage")
                                    UsageDTO usage) {
            this.usage = usage;
        }
    }

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class UsageDTO {

        String memory;

        String cpu;

        @JsonCreator
        public UsageDTO(@JsonProperty("memory") String memory, @JsonProperty("cpu") String cpu) {
            this.memory = memory;
            this.cpu = cpu;
        }
    }
}
