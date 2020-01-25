# Getting started with REST API testing with Serenity and Cucumber 4

### The project directory structure
The project has build script for Maven, and follows the standard directory structure used in most Serenity projects:
```Gherkin
src
  + main
  + test
    + java                                Test runners and supporting code
    + resources
      + features                          Feature files 
         signin.feature
         user_management.feature
      serenity.conf                       Configuration setting
```

## Executing the tests

You use the `environment` system property to determine which environment to run against.
For example to run the tests in the staging environment, you could run:
```json
$  mvn clean verify -Denvironment=staging
```
For example to run the tests with specific tags, you could run:
```json
$ mvn clean verify -Dcucumber.options="--tags @smoke"
```
For example to run the tests in the local dev with local url environment with specific tags, you could run:
```json
mvn clean verify -Drestapi.baseurl=http://localhost:8080/idemo-admin -Dcucumber.options="--tags @smoke" 
```
To force updating of snapshots and re-download all the dependencies
```json
$ mvn clean install -U -Dmaven.test.failure.ignore=true 
```

The test results will be recorded in the `target/site/serenity` directory.
