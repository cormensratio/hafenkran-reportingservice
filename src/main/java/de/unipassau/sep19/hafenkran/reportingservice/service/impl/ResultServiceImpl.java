package de.unipassau.sep19.hafenkran.reportingservice.service.impl;

import de.unipassau.sep19.hafenkran.reportingservice.dto.ResultDTO;
import de.unipassau.sep19.hafenkran.reportingservice.dto.ResultDTOList;
import de.unipassau.sep19.hafenkran.reportingservice.model.Result;
import de.unipassau.sep19.hafenkran.reportingservice.model.Result.ResultType;
import de.unipassau.sep19.hafenkran.reportingservice.repository.ResultRepository;
import de.unipassau.sep19.hafenkran.reportingservice.service.ResultService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.UUID;

/**
 * {@inheritDoc}
 */
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
@Component
@Slf4j
public class ResultServiceImpl implements ResultService {

    private final ResultRepository resultRepository;

    private List<Result> retrieveRemoteResultsForExecution(UUID executionId){
        WebClient.create().
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ResultDTO> retrieveResultDTOListByExecutionIdAndType(@NonNull UUID executionId, @NonNull ResultType resultType) {
        return ResultDTOList.convertResultListToDTOList(findResultListByExecutionIdAndType(executionId, resultType));
    }

    private List<Result> findResultListByExecutionIdAndType(@NonNull UUID executionId, @NonNull ResultType resultType) {
        List<Result> resultsByExecutionId = resultRepository.findAllByExecutionIdAndTypeEquals(executionId, resultType);
        resultsByExecutionId.forEach(Result::validatePermissions);
        return resultsByExecutionId;
    }
}