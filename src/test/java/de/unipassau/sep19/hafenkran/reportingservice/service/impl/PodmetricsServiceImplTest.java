package de.unipassau.sep19.hafenkran.reportingservice.service.impl;

import de.unipassau.sep19.hafenkran.reportingservice.dto.MetricsDTO;
import de.unipassau.sep19.hafenkran.reportingservice.dto.MetricsDTOList;
import de.unipassau.sep19.hafenkran.reportingservice.model.Podmetrics;
import de.unipassau.sep19.hafenkran.reportingservice.repository.PodmetricsRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PodmetricsServiceImplTest {

    public static final Timestamp MOCK_TIMESTAMP = Timestamp.valueOf(LocalDateTime.now());
    public static final String MOCK_CPU = "10";
    public static final String MOCK_RAM = "20";
    private static final UUID MOCK_EXECUTION_ID = UUID.fromString("00000000-0000-0000-0000-000000000001");
    private static final UUID MOCK_USER_ID = UUID.fromString("00000000-0000-0000-0000-000000000001");
    private static final UUID MOCK_USER_EXPERIMENT_ID = UUID.fromString("00000000-0000-0000-0000-000000000001");
    
    @Mock
    private PodmetricsRepository mockPodmetricsRepository;

    private Podmetrics testPodMetrics;

    private List<Podmetrics> testPodMetricsList;

    private MetricsDTO testMetricsDTO;

    private List<MetricsDTO> testMetricsDTOList;

    private PodmetricsServiceImpl subject;

    @Before
    public void setUp() {
        this.subject = new PodmetricsServiceImpl(mockPodmetricsRepository);
        this.testPodMetricsList = new ArrayList<>();
        this.testMetricsDTOList = new ArrayList<>();

        this.testPodMetrics = new Podmetrics(MOCK_USER_ID, MOCK_EXECUTION_ID, MOCK_USER_EXPERIMENT_ID, MOCK_TIMESTAMP, MOCK_CPU, MOCK_RAM);

        Podmetrics podmetrics1 = this.testPodMetrics;
        Podmetrics podmetrics2 = new Podmetrics(MOCK_USER_ID, MOCK_EXECUTION_ID, MOCK_USER_EXPERIMENT_ID, MOCK_TIMESTAMP, MOCK_CPU + 1, MOCK_RAM + 1);
        Podmetrics podmetrics3 = new Podmetrics(MOCK_USER_ID, MOCK_EXECUTION_ID, MOCK_USER_EXPERIMENT_ID, MOCK_TIMESTAMP, MOCK_CPU + 2, MOCK_RAM + 2);
        Podmetrics podmetrics4 = new Podmetrics(MOCK_USER_ID, MOCK_EXECUTION_ID, MOCK_USER_EXPERIMENT_ID, MOCK_TIMESTAMP, MOCK_CPU + 3, MOCK_RAM + 3);
        Podmetrics podmetrics5 = new Podmetrics(MOCK_USER_ID, MOCK_EXECUTION_ID, MOCK_USER_EXPERIMENT_ID, MOCK_TIMESTAMP, MOCK_CPU + 4, MOCK_RAM + 4);

        this.testPodMetricsList.add(podmetrics1);
        this.testPodMetricsList.add(podmetrics2);
        this.testPodMetricsList.add(podmetrics3);
        this.testPodMetricsList.add(podmetrics4);
        this.testPodMetricsList.add(podmetrics5);

        this.testMetricsDTO = new MetricsDTO(MOCK_CPU, MOCK_RAM, MOCK_TIMESTAMP);

        MetricsDTO metricsDTO1 = this.testMetricsDTO;
        MetricsDTO metricsDTO2 = new MetricsDTO(MOCK_CPU + 1, MOCK_RAM + 1, MOCK_TIMESTAMP);
        MetricsDTO metricsDTO3 = new MetricsDTO(MOCK_CPU + 2, MOCK_RAM + 2, MOCK_TIMESTAMP);
        MetricsDTO metricsDTO4 = new MetricsDTO(MOCK_CPU + 3, MOCK_RAM + 3, MOCK_TIMESTAMP);
        MetricsDTO metricsDTO5 = new MetricsDTO(MOCK_CPU + 4, MOCK_RAM + 4, MOCK_TIMESTAMP);

        this.testMetricsDTOList.add(metricsDTO1);
        this.testMetricsDTOList.add(metricsDTO2);
        this.testMetricsDTOList.add(metricsDTO3);
        this.testMetricsDTOList.add(metricsDTO4);
        this.testMetricsDTOList.add(metricsDTO5);
    }

    @Test
    public void testRetrieveMetricsDTOListByExecutionId_validExecutionId_validMetricsDTOList() {

        // Arrange
        when(mockPodmetricsRepository.findAllByExecutionId(MOCK_EXECUTION_ID)).thenReturn(testPodMetricsList);
        MetricsDTOList mockMetricsDTOList = new MetricsDTOList(testMetricsDTOList, MOCK_EXECUTION_ID);

        // Act
        MetricsDTOList actual = subject.retrieveMetricsDTOListByExecutionId(MOCK_EXECUTION_ID);

        // Assert
        verify(mockPodmetricsRepository, times(1)).findAllByExecutionId(MOCK_EXECUTION_ID);
        assertEquals(mockMetricsDTOList, actual);
        verifyNoMoreInteractions(mockPodmetricsRepository);
    }
}
