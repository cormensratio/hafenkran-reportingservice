package de.unipassau.sep19.hafenkran.reportingservice.clusterserviceclient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.unipassau.sep19.hafenkran.reportingservice.model.Podmetrics;
import de.unipassau.sep19.hafenkran.reportingservice.service.PodmetricsService;
import de.unipassau.sep19.hafenkran.reportingservice.util.SecurityContextUtil;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class ClusterServiceClient {

    @Value("${clusterservice.path}")
    private String basePath;

    private PodmetricsService podmetricsService;

    private <T> T get(String path, Class<T> responseType) {
        RestTemplate rt = new RestTemplate();
        String targetPath = basePath + path;
        ResponseEntity<T> response = rt.exchange(basePath + path, HttpMethod.GET, authHeaders(), responseType);

        if (!HttpStatus.Series.valueOf(response.getStatusCode()).equals(HttpStatus.Series.SUCCESSFUL)) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    String.format("Could not retrieve data from %s. Reason: %s %s", targetPath,
                            response.getStatusCodeValue(), response.getBody()));
        }

        return response.getBody();
    }

    private HttpEntity authHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + SecurityContextUtil.getJWT());
        return new HttpEntity<>("body", headers);
    }

    public String retrieveResultsForExecutionId(@NonNull UUID executionId){
        return get(String.format("/executions/%s/results", executionId), String.class);
    }

    @Scheduled(fixedRate=60000)
    public void retrieveAllPodMetrics(){
        String jsonGetMetricsResponse = getMetrics("/metrics/all", String.class);
        convertJsonToPodMetricsAndSave(jsonGetMetricsResponse);
    }

    private <T> T getMetrics(String path, Class<T> responseType) {
        RestTemplate rt = new RestTemplate();
        String targetPath = basePath + path;
        ResponseEntity<T> response = rt.exchange(basePath + path, HttpMethod.GET, authTestHeaders(), responseType);

        if (!HttpStatus.Series.valueOf(response.getStatusCode()).equals(HttpStatus.Series.SUCCESSFUL)) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    String.format("Could not retrieve data from %s. Reason: %s %s", targetPath,
                            response.getStatusCodeValue(), response.getBody()));
        }

        return response.getBody();
    }

    private HttpEntity authTestHeaders() {
        HttpHeaders headers = new HttpHeaders();
        String token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIwMDAwMDAwMC0wMDAwLTAwMDAtMDAwMC0wMDAwMDAwMDAwMDEiLCJleHAiOjE1NzkyNzAxOTQsInVzZXIiOnsiaWQiOiIwMDAwMDAwMC0wMDAwLTAwMDAtMDAwMC0wMDAwMDAwMDAwMDEiLCJuYW1lIjoiTW9ydGltZXIiLCJlbWFpbCI6IiIsImlzQWRtaW4iOnRydWV9LCJpYXQiOjE1NzkwOTczOTR9.IxpYyTDQiL65Mu2bvtH0WrE5wb62TtjGKZSltp7_PxRURPlAWK8qyYFFJas-g7DdiC9Oz8oJsebIbN6xr79Fnw";
        headers.set("Authorization", "Bearer " + token);
        return new HttpEntity<>("body", headers);
    }

    private void convertJsonToPodMetricsAndSave(String jsonGetMetricsResponse) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JSONArray jsonResponseArray = new JSONArray(jsonGetMetricsResponse);
            if (jsonResponseArray.length() >= 1) {
                for (int i = 0; i < jsonResponseArray.length(); i++) {
                    String jsonMetricsItem = jsonResponseArray.getJSONObject(i).toString();
                    System.out.println(jsonMetricsItem);
                    Podmetrics podMetrics = objectMapper.readValue(jsonMetricsItem, Podmetrics.class);
                    podmetricsService.savePodmetrics(podMetrics);
                }
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
