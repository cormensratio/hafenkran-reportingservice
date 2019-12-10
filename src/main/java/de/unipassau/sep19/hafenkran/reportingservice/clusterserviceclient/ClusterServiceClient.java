package de.unipassau.sep19.hafenkran.reportingservice.clusterserviceclient;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class ClusterServiceClient {

    private final WebClient client;

    ClusterServiceClient() {
        client = WebClient.create("http://localhost:8082");
    }

    public Mono<String> get(String path) {
        client.get()
                .uri(path)
                .exchange()
                .then(response -> response.bodyToMono());
    }
}
