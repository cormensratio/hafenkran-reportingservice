package de.unipassau.sep19.hafenkran.reportingservice.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ComponentScan(basePackages = {
        "de.unipassau.sep19.hafenkran.reportingservice.util"
})
@EntityScan(basePackages = {
})
@EnableJpaRepositories(basePackages = {
})
@EnableAutoConfiguration
public class ConfigEntrypoint {

}
