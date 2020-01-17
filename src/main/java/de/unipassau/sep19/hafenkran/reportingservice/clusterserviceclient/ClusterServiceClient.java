package de.unipassau.sep19.hafenkran.reportingservice.clusterserviceclient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.unipassau.sep19.hafenkran.reportingservice.dto.CsPodmetricsDTO;
import de.unipassau.sep19.hafenkran.reportingservice.service.PodmetricsService;
import de.unipassau.sep19.hafenkran.reportingservice.util.SecurityContextUtil;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
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

    private final PodmetricsService podmetricsService;

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

    private <T> T getMetrics(@NonNull String path, Class<T> responseType) {
        RestTemplate rt = new RestTemplate();
        String targetPath = basePath + path;
        HttpHeaders token = authHeadersMicroserviceCommunication();
        token.add("Content-Type", "application/json");
        HttpEntity httpEntity = new HttpEntity<>("", token);
        ResponseEntity<T> response = rt.exchange(basePath + path, HttpMethod.GET, httpEntity, responseType);

        if (!HttpStatus.Series.valueOf(response.getStatusCode()).equals(HttpStatus.Series.SUCCESSFUL)) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    String.format("Could not retrieve data from %s. Reason: %s %s", targetPath,
                            response.getStatusCodeValue(), response.getBody()));
        }

        return response.getBody();
    }

    private void convertJsonToPodMetricsAndSave(@NonNull String jsonGetMetricsResponse) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JSONArray jsonResponseArray = new JSONArray(jsonGetMetricsResponse);
            if (jsonResponseArray.length() >= 1) {
                for (int i = 0; i < jsonResponseArray.length(); i++) {
                    String jsonMetricsItem = jsonResponseArray.getJSONObject(i).toString();
                    System.out.println(jsonMetricsItem);
                    CsPodmetricsDTO csPodMetricsDTO = objectMapper.readValue(jsonMetricsItem, CsPodmetricsDTO.class);
                    podmetricsService.savePodmetrics(csPodMetricsDTO);
                }
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String post(String path, String body) {
        RestTemplate rt = new RestTemplate();
        HttpHeaders header = new HttpHeaders();
        header.add("Content-Type", "application/json");
        ResponseEntity<String> response = rt.exchange(path, HttpMethod.POST,
                new HttpEntity<>(body, header), String.class);

        if (!HttpStatus.Series.valueOf(response.getStatusCode()).equals(HttpStatus.Series.SUCCESSFUL)) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    String.format("Could not retrieve data from %s. Reason: %s %s", path,
                            response.getStatusCodeValue(), response.getBody()));
        }

        return response.getBody();
    }

    private String getAuthToken() {
        String loginResponse = post("http://localhost:8081/authenticate",
                String.format("{\"name\":\"%s\", \"password\":\"%s\"}", "service", "test"));
        final String jwtToken;
        try {
            jwtToken = (String) new JSONObject(loginResponse).get("jwtToken");
            return jwtToken;
        } catch (JSONException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not retrieve JWT from login.");
        }
    }

    private HttpHeaders authHeadersMicroserviceCommunication() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + getAuthToken());
        return headers;
    }
}
