package se.sundsvall.installedbase.integration.datawarehousereader.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("integration.datawarehousereader")
public record DataWarehouseReaderProperties(int connectTimeout, int readTimeout) {
}
