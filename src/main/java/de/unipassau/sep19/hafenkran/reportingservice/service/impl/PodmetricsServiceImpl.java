package de.unipassau.sep19.hafenkran.reportingservice.service.impl;

import de.unipassau.sep19.hafenkran.reportingservice.dto.CsPodmetricsDTO;
import de.unipassau.sep19.hafenkran.reportingservice.dto.MetricsDTO;
import de.unipassau.sep19.hafenkran.reportingservice.dto.MetricsDTOList;
import de.unipassau.sep19.hafenkran.reportingservice.model.Podmetrics;
import de.unipassau.sep19.hafenkran.reportingservice.repository.PodmetricsRepository;
import de.unipassau.sep19.hafenkran.reportingservice.service.PodmetricsService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * {@inheritDoc}
 */
@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class PodmetricsServiceImpl implements PodmetricsService {

    @NonNull
    private final PodmetricsRepository podmetricsRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public MetricsDTOList retrieveMetricsDTOListByExecutionId(@NonNull UUID executionId) {
        List<Podmetrics> metricsList = retrievePodmetricsListByExecutionId(executionId);
        List<MetricsDTO> metricsDTOList = metricsList.stream().map(r -> new MetricsDTO(
                r.getCpu(),
                r.getMemory(),
                r.getTimestamp())).collect(Collectors.toList());
        return new MetricsDTOList(metricsDTOList, executionId);
    }

    @Override
    public void savePodmetrics(@NonNull CsPodmetricsDTO csPodmetricsDTO) {
        Podmetrics podmetrics = new Podmetrics(csPodmetricsDTO);
        podmetricsRepository.save(podmetrics);
    }

    private List<Podmetrics> retrievePodmetricsListByExecutionId(@NonNull UUID executionId) {
        return podmetricsRepository.findAllByExecutionId(executionId);
    }
}
