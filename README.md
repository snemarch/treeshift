# AmazingCo's TreeShift

Modeling Amazing Company's structure, so we can do awesome stuff.

The company business units have been modelled as a collection of directed acyclic graphs — or, a forest of
trees, if you will. TreeShift is a microservice that provides HTTP endpoints for:
1. Retrieving all descendant nodes of a given node
2. Change the parent node of a given node

Further documentation can be found in the **doc** directory. See `design.md` for design considerations, and
`status.md` for the current status of the project.

## Scope

The focus of this project is to produce a functioning proof of concept, so I will be leaving out some parts
that are important for production code. There will be no authentication or authorization. The database
structure is simple enough I'll leave out migrations (e.g. [Flyway](https://flywaydb.org/)). I will also leave
out a number of infrastructure concerns, such as continuous deployment pipeline, container scaling, and
secrets management (i.e. there will be hardcoded database credentials).

## Getting Started

Prerequisites: Java 11, Gradle (or use the Gradle wrapper), Docker, Docker Compose.

Get up and running by issuing the following commands: 

```
./gradlew jibDockerBuild
docker-compose up
```

This will build the Spring Boot project, build a Docker image tagged as `amazingco/treeshift:latest`, and
spin up docker containers for the database and the application. The application exposes services on port 9000,
and the database accepts connections on port 5432.

For development, run `./gradlew bootRun --args='--spring.profiles.active=local'`. This will start the
application in development mode, exposed on port 8080. Be sure to define your development version ending in
"-SNAPSHOT" in `build.gradle`. This enables Spring Boot DevTools, so performing a build in your IDE triggers
an application reload.

Perform integration tests by doing `./gradlew cleanTest test`, which will print test results to the console as
well as generate a report in `build/reports/tests/test/index.html`.

The application initially has no data in its database. You can load a sample organisational tree using the "import-tree"
endpoint, e.g. `curl -X POST -H "Content-Type: application/json" http://localhost:9000/api/org-units/import-tree --data
 @doc/sample-tree.json`.

## Endpoints
    
* `GET "/api/org-units/{id}/flat"`
    Returns the node specified by {id} and all its descendants as a flat list of
    nodes. 
* `GET "/api/org-units/{id}/tree"`
    Returns the node specified by {id} and all its descendants as a tree
    structure. 
* `GET "/api/org-units/{id}/is-descendant-of/{parentId}"`
    Returns 'true' if the node specified by {id} is a descendant of {parentId},
    otherwise 'false'.
* `POST "/api/org-units/{id}/move-to/{newParentId}"`
    Changes the parent of the node specified by {id} to the new parent specified
    by {newParentId}. Updates `height` and `rootId` for all descendants.  
* `POST /api/org-units/import-tree"`
    Imports a tree in JSON format to the database. This is a very low-level
    endpoint with no validation, so make sure the data is correct! See the
    `doc/sample-tree.json` file for an example of the tree structure.

## Authors

* **Sune Marcher** - [snemarch](https://github.com/snemarch)
