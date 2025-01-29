# InstalledBase

_The service provides resources for retrieving citizen engagements towards various suppliers within Sundsvall municipality._

## Getting Started

### Prerequisites

- **Java 21 or higher**
- **Maven**
- **MariaDB**
- **Git**
- **[Dependent Microservices](#dependencies)**

### Installation

1. **Clone the repository:**

```bash
git clone https://github.com/Sundsvallskommun/api-service-installedbase.git
cd api-service-installedbase
```

2. **Configure the application:**

   Before running the application, you need to set up configuration settings.
   See [Configuration](#configuration)

   **Note:** Ensure all required configurations are set; otherwise, the application may fail to start.

3. **Ensure dependent services are running:**

   If this microservice depends on other services, make sure they are up and accessible. See [Dependencies](#dependencies) for more details.

4. **Build and run the application:**

   - Using Maven:

   ```bash
   mvn spring-boot:run
   ```

   - Using Gradle:

   ```bash
   gradle bootRun
   ```

## Dependencies

This microservice depends on the following services:

- **DataWarehouseReader**
  - **Purpose:** Used for reading installed base information.
  - **Repository:** [https://github.com/Sundsvallskommun/api-service-datawarehousereader](https://github.com/Sundsvallskommun/api-service-datawarehousereader)
  - **Setup Instructions:** See documentation in repository above for installation and configuration steps.

Ensure that these services are running and properly configured before starting this microservice.

## API Documentation

See `openapi.yaml` located in directory `src\integration-test\resources\openapi` or access the API documentation via Swagger UI:

- **Swagger UI:** [http://localhost:8080/api-docs](http://localhost:8080/api-docs)

## Usage

### API Endpoints

See the [API Documentation](#api-documentation) for detailed information on available endpoints.

### Example Request

```bash
curl -X GET http://localhost:8080/2281/installedbase/5565112233?partyId=cafe247e-8187-4bd3-a131-9cbe3c610a2b
```

## Configuration

Configuration is crucial for the application to run successfully. Ensure all necessary settings are configured in `application.yml`.

### Key Configuration Parameters

- **Server Port:**

```yaml
server:
  port: 8080
```

- **External Service URLs**

```yaml
  integration:
    datawarehousereader:
      url: <service-url>

  spring:
    security:
      oauth2:
        client:
          registration:
            datawarehousereader:client-id: <client-id>
            client-secret: <client-secret>
          provider:
            datawarehousereader:
              token-uri: <token-url>
```

### Additional Notes

- **Application Profiles:**

  Use Spring profiles (`dev`, `prod`, etc.) to manage different configurations for different environments.

- **Logging Configuration:**

  Adjust logging levels if necessary.

## Contributing

Contributions are welcome! Please see [CONTRIBUTING.md](https://github.com/Sundsvallskommun/.github/blob/main/.github/CONTRIBUTING.md) for guidelines.

## License

This project is licensed under the [MIT License](LICENSE).

## Status

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=Sundsvallskommun_api-service-installedbase&metric=alert_status)](https://sonarcloud.io/summary/overall?id=Sundsvallskommun_api-service-installedbase)
[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=Sundsvallskommun_api-service-installedbase&metric=reliability_rating)](https://sonarcloud.io/summary/overall?id=Sundsvallskommun_api-service-installedbase)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=Sundsvallskommun_api-service-installedbase&metric=security_rating)](https://sonarcloud.io/summary/overall?id=Sundsvallskommun_api-service-installedbase)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=Sundsvallskommun_api-service-installedbase&metric=sqale_rating)](https://sonarcloud.io/summary/overall?id=Sundsvallskommun_api-service-installedbase)
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=Sundsvallskommun_api-service-installedbase&metric=vulnerabilities)](https://sonarcloud.io/summary/overall?id=Sundsvallskommun_api-service-installedbase)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=Sundsvallskommun_api-service-installedbase&metric=bugs)](https://sonarcloud.io/summary/overall?id=Sundsvallskommun_api-service-installedbase)

## 

&copy; 2023 Sundsvalls kommun
