# Hafenkran

## ReportingService
The ReportingService for the Hafenkran project is responsible for fetching and persisting the results and metrics of an execution for retrieval after the execution has finished.


### Setup
Make sure that the required parameters in `application.yml` are configured, when deploying the application, otherwise startup will fail.

### Development
- Use the `dev` Spring profile configured in `application-dev.yml`to use the default configuration. 
    - In IntelliJ set the profile under `Active Profiles` in your build configuration
    - In maven use:
        > mvn spring-boot:run -Dspring-boot.run.profiles=dev
- This project requires `Lombok`. Please check that your IDE has the latest Lombok plugin installed.
- For logging use `@Slf4j` provided by Lombok.
- To use Spring devtools in IntelliJ set `On Update` and `On Frame deactivation` actions to `Update classes and resources` in your build configuration.

### Config
The following settings can be configured for a production system:

```
spring.datasource.url:          # location of the database
spring.datasource.username:     # user for the database
spring.datasource.password:     # password for the database user

jwt.secret:                     # JWT secret used for signing and validation
jwt.validity:                   # time after creation until a JWT is marked invalid

cluster-service-uri:            # uri to the ClusterService
results-storage-path:           # storage path for the result files
results-storage-path:

service-user:
  name:                         # name of the user used for microservice communication
  password:                     # password of the user used for microservice communication
  secret:                       # secret token used as request parameter for internal service calls

metrics:
  frequency:                    # frequency of metrics updates
  initial-delay:                # initial delay for the first metrics retrieval
```