package de.unipassau.sep19.hafenkran.reportingservice.repository;

import de.unipassau.sep19.hafenkran.reportingservice.model.Results;
import lombok.NonNull;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ResultRepository extends CrudRepository<Results, UUID> {
    void deleteAllByExecutionId(@NonNull UUID executionId);
}
