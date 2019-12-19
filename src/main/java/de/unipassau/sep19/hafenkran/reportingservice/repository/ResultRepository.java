package de.unipassau.sep19.hafenkran.reportingservice.repository;

import de.unipassau.sep19.hafenkran.reportingservice.model.Results;
import lombok.NonNull;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Repository
public interface ResultRepository extends CrudRepository<Results, UUID> {

    List<Results> findAllByExecutionId(@NonNull UUID executionId);

    @Transactional
    void deleteByExecutionId(@NonNull UUID executionId);
}
