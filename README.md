# Amazing Co's TreesShift

Modeling Amazing Company's structure, so we can do awesome stuff.

The company business units have been modelled as a collection of directed acyclic graphs — or, a forest of
trees, if you will. TreeShift is a microservice that provides HTTP endpoints for:
1. Retrieving all descendant nodes of a given node
2. Change the parent node of a given node

## Getting Started

**(To be done)**

Prerequisites: Java 11, Gradle (or use the Gradle wrapper), Docker, Docker Compose.

Getting up and running by running the following commands: 

```
./gradlew jibDockerBuild
docker-compose up
```

This will build the Spring Boot project, build a Docker image tagged as `awesomeco/treeshift:latest`, and
spin up docker containers for the database and the application. The application exposes services on port 9000,
and the database is exposed on port 5432.

For development, run `./gradlew bootRun --args='--spring.profiles.active=local'`. This will start the
application in development mode, exposed on port 8080. Spring Boot DevTools are enabled, so performing a
build in your IDE should trigger an application reload. 

Perform integration tests by doing *bar*, and see *qux* for generating a large amount of data in order to do
performance measurements.

Further documentation, including design considerations, is located in the **docs** directory.

## Scope

My focus for this project is to produce a functioning proof of concept, so I will be leaving out some parts
that are important for production code. There will be no authorization or authentication. The database
structure is simple enough I'll leave out migrations (e.g. [Flyway](https://flywaydb.org/)). I will leave out
a number of infrastructure concerns, such as continuous deployment pipeline, scaling, and secrets management
(i.e. there will be hardcoded database credentials).

## TODO

* Integration tests — unit tests don't make much sense for this project. Split conf in base+dev/test. 
* Add timing traces to reply HTTP headers.
* Endpoint for generating a large amount of test data. Large number of trees, fair amount of width+depth.
  Use a PRNG with static seed so test results are reproducible. 
* Document endpoints. Perhaps Swagger annotations or a yaml, perhaps just text here since they're simple.
* Index on OrgUnit.parentId?
* Index on OrgUnit.rootId? — probably a good idea to make a partial index on `(parentId = null)`, so tree
  roots can quickly be returned, but without creating a huge index we don't need.
* Throw business logic exceptions instead of IllegalArgumentException.

## Authors

* **Sune Marcher** - [snemarch](https://github.com/snemarch)
