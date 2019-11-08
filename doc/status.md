# Project status for TreeShift

TreeShift is "mostly done". It should fulfill the requirements of the coding challenge, but it's not
*entirely* in the state I'd like it to be. A prioritized list of known issues follows below.  

## TODO

* Better integration tests — the current ones are pretty rudimentary. 
* Endpoint for generating a large amount of test data. Large number of trees, fair amount of width+depth.
  Random tree shapes. Use a PRNG with static seed so test results are reproducible. 
* Add timing traces to reply HTTP headers.
* Better endpoint documentation. Perhaps Swagger annotations on the controller, or a swagger.yaml file.
* Move from Hibernate DDL auto-create to schema.sql.
* Endpoint for returning all tree root node ids, so we can iterate the forest. 
* Throw business logic exceptions instead of IllegalArgumentException.
* Split `docker-compose.yml` into a base file and versions for "prod" and "local". This isn't super important,
  but it would be nice not running the application container for local development, and not exposing the
  database outside the internal Docker network in "prod".
* It would be nice to code a tool for visualizing the trees. Should be simple to hack together a tool in Go,
  Kotlin or whatever that converts a tree.json to Graphviz.dot format, but that would be gold-plating :-)
