# Design considerations for TreeShift

The coding challenge seems pretty straightforward at first, but with some subtleties. In a normal development
situation, I would ask questions about expected data sizes, tree width and depth, et cetera – but for the
challenge, I believe it's appropriate to choose some constraints based on (documented) assumptions, of course
with some notes on alternatives for other magnitudes of data.

## Data structure

The assignment mentions "*We have a root node (only one)*", but requirement #3 specifices each node must
record "*who is the root node*". I'm choosing to interpret this as the data structure being a collection of
directed acyclic graphs, rather than a single huge tree. This affects the solution - e.g. it would infeasible
to construct an entire in-memory graph of one huge tree before serving it to the client, but it's a realistic
scenario if we're dealing with a multitude of largeish trees. Also, depicting a large company with several
business units and geographical locations as a forest seems reasonable.

## Persistence

I'm choosing [PostgreSQL](https://spring.io/projects/spring-boot) as persistence layer. A relational database
might not be the best fit for a DAG, but it works, and I have experience with it. It has some implications on
performance - it's not easy to scale a relational database horizontally, manual sharding strategies easily
become complex and hard to maintain. And by choosing a database, most of the processing will be done in SQL
queries instead of application code. The advantages are that PostgreSQL is battle-tested, performs decently,
and that SQL is a well-known language.

If the scope were a real-world project with extreme scalability requirements, I would do research on alternate
persistence systems. A project like [Dgraph](https://dgraph.io/), for instance, sounds like it could be pretty
interesting.

## Services / Endpoints

The assignment says "We need two HTTP APIs", without specifying expected Content-Type or structure. There are
several ways this could be handled, with various performance and ease of use tradeoffs.

1. Get all descendant nodes of a given node

The first solution that comes to mind is serving the client a JSON representation of the DAG, along the lines
of:
```
{
		id: 1,
		parent: null,
		root: 1,
		height: 0,
		children: [
			{
				id: 2,
				parent: 1,
				root: 1,
				height: 1,
				children: [ ... ]
			},
			{ ... }
		]
	}
}
```
(Parent and root identifiers could be skipped for this representation, since they can be inferred from the
data structure). This is a format that's easy to consume from a client, as it can feed it to a deserializer
and get a ready-to-use object graph back. It has the downside of requiring processing on the server side, and
with a trivial implementation, the entire graph would be built in memory before serving it to the client.

An alternate method is to return a list of unstructured nodes to the client:
```
[{
	id: 1,
	parent: null,
	root: 1,
	height: 0
},
{
	id: 2,
	parent: 1,
	root: 1,
	height: 1
}, ...]
```

This data format is very simple to handle for the back-end - it doesn't need to do any processing except
per-node serialization/deserialization, and it can stream the results to the client rather than keeping the
entire resultset in memory. 

I plan on implementing both of these versions, with a limit on number of returned nodes to avoid memory
exhaustion. Both will return JSON data, since it's easy to deal with, but other things could be considered:
a binary format like [Protobuf](https://developers.google.com/protocol-buffers) for lesser overhead, or a
protocol like [RSocket](http://rsocket.io/) for full reactive stream capabilities.

2. Change the parent node of a given node

Changing the parent node of a given node (shifting a subtree), "*the given node can be anyone in the tree
structure*". I'm adding the constraint that the new parent node cannot be a descendant of the given node, as
that would create a cycle in the graph. If we were to allow moving a node to one of its descendant nodes,
we could walk up the tree until we find the node whose parent node is our source node, and update that node to
be our source node's parent, to avoid creating a cycle. But considering we're modelling an organization, that
kind of restructure seems a bit odd, and I believe that "*can't set a descendant as parent*" is a reasonable
constraint.

Performing the constraint check can be computationally heavy, but a couple of optimizations can be done for
the happy path: if `source.root != target.root` (different trees) or `source.depth <= target.depth` (target is
parent or sibling), the expensive check can be avoided.

Finally, depth (and possibly root) has to be updated for the entire subtree. And the operations have to be
done atomatically, since there could be concurrent attempts at modifying parts of the same tree.

## Choice of technology

Java was chosen as the programming language, since **Amazing Co** runs on the JVM, and everybody knows Java.
Version 11 was chosen, since it's the current Long Term Support version.
[Spring Boot](https://spring.io/projects/spring-boot) was chosen for the framework, since I'm familiar with it,
and it's an industry standard. For lower overhead and being able to quickly react to heavy load, perhaps a
mix of [Quarkus](https://quarkus.io) and [Vert.x](https://vertx.io/) would be worth pursuing.
[Gradle](https://gradle.org/) was chosen as build tool, it's well-known and reasonably pleasant to work with.

## Miscellaneous

Having some timing information available could be useful for determining bottlenecks. Instead of measuring
with a profiler (which can be hard to do in production, or in any kind of distributed system), I plan to
always do the relevant timing in program code. To reduce overhead, rather than writing timing information to
a log file, I plan to add it as X-*Something* HTTP headers.
