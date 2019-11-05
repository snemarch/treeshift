# Amazing Co's TreesShift

Modeling Amazing Company's structure, so we can do awesome stuff.

The company business units have been modelled as a collection of directed acyclic graphs — or, a forest of
trees, if you will. TreeShift is a microservice that provides HTTP endpoints for:
1. Retrieving all descendant nodes of a given node
2. Change the parent node of a given node

## Getting Started

**(To be done)**

Prerequisites: Java 11, Gradle (or use the Gradle wrapper), Docker, Docker Compose.

Build with Gradle, do initial setup by running *foo*, perform integration tests by doing *bar*, and see *qux*
for generating a large amount of data in order to do performance measurements.

Further documentation, including design considerations, is located in the **docs** directory.

## Scope

My focus for this project is to produce a functioning proof of concept, so I will be leaving out some parts
that are important for production code. There will be no authorization or authentication. The database
structure is simple enough I'll leave out migrations (e.g. [Flyway](https://flywaydb.org/)). I will leave out
a number of infrastructure concerns, such as continuous deployment pipeline, scaling, and secrets management
(i.e. there will be hardcoded database credentials).

## Authors

* **Sune Marcher** - [snemarch](https://github.com/snemarch)
