package de.unipassau.sep19.hafenkran.reportingservice.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@ComponentScan(basePackages = {
        "de.unipassau.sep19.hafenkran.reportingservice.controller",
        "de.unipassau.sep19.hafenkran.reportingservice.util",
        "de.unipassau.sep19.hafenkran.reportingservice.service.impl",
        "de.unipassau.sep19.hafenkran.reportingservice.clusterserviceclient"
})
@EntityScan(basePackages = {
        "de.unipassau.sep19.hafenkran.reportingservice.model"
})
@EnableJpaRepositories(basePackages = {
        "de.unipassau.sep19.hafenkran.reportingservice.repository"
})
@EnableAutoConfiguration
public class ConfigEntrypoint {

}
