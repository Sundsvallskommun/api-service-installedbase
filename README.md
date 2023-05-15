# InstalledBase

## Leverantör

Sundsvalls kommun

## Beskrivning
InstalledBase är en tjänst som presenterar data för aktuella engagemang som kunden har.

## Tekniska detaljer

### Integrationer
Tjänsten integrerar mot:

* Mikrotjänst DataWarehouseReader

### Starta tjänsten

|Konfigurationsnyckel|Beskrivning|
|---|---|
|`add.key`|Add description|


### Paketera och starta tjänsten
Applikationen kan paketeras genom:

```
./mvnw package
```
Kommandot skapar filen `api-service-installedbase-<version>.jar` i katalogen `target`. Tjänsten kan nu köras genom kommandot `java -jar target/api-service-installedbase-<version>.jar`.

### Bygga och starta med Docker
Exekvera följande kommando för att bygga en Docker-image:

```
docker build -f src/main/docker/Dockerfile -t api.sundsvall.se/ms-installedbase:latest .
```

Exekvera följande kommando för att starta samma Docker-image i en container:

```
docker run -i --rm -p8080:8080 api.sundsvall.se/ms-installedbase

```

#### Kör applikationen lokalt

Exekvera följande kommando för att bygga och starta en container i sandbox mode:  

```
docker-compose -f src/main/docker/docker-compose-sandbox.yaml build && docker-compose -f src/main/docker/docker-compose-sandbox.yaml up
```


## 
Copyright (c) 2021 Sundsvalls kommun