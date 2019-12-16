package de.unipassau.sep19.hafenkran.reportingservice.repository;

import de.unipassau.sep19.hafenkran.reportingservice.model.Results;
import lombok.NonNull;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ResultRepository extends CrudRepository<Results, UUID> {

    void deleteResultsByExecutionId(@NonNull UUID executionId);
}
