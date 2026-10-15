# virtuoso-web-security

> **Archived.** This library moved to [`positivinh/virtuoso`](https://github.com/positivinh/virtuoso/tree/main/virtuoso-web-security),
> with its history, and is built and released there from 2.0.0 on. Its groupId and artifactIds are unchanged. This
> repository is read-only; open issues and pull requests on `positivinh/virtuoso`.

Software factory components related to application security aspects

## Commands

### Install

```shell
mvn clean install
```

### Run sonarqube analysis

Set environment variable `SONAR_TOKEN`

```shell
export SONAR_TOKEN=[...]
```

Run sonarqube analysis

```shell
mvn clean verify sonar:sonar -P sonarqube
```