package com.xnlogic.pacer;

import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;

public class VertexRoute extends Route<Vertex> {


	public VertexRoute(Stream<Vertex> vertices, boolean parallel) {
		super(vertices, parallel);
	}

	public VertexRoute(Stream<Vertex> vertices) {
		this(vertices, false);
	}


	public EdgeRoute outE(String... labels){
		return new EdgeRoute(this.elements.flatMap(edges(Direction.OUT, isParallel(), labels)), isParallel());
	}

	public EdgeRoute inE(String... labels){
		return new EdgeRoute(this.elements.flatMap(edges(Direction.IN, isParallel(), labels)), isParallel());
	}

	public EdgeRoute bothE(String... labels){
		return new EdgeRoute(this.elements.flatMap(edges(Direction.BOTH, isParallel(), labels)), isParallel());
	}
	
	private Function<Vertex, Stream<Edge>> edges(Direction direction, boolean parallel, String... labels){
		return (v) -> StreamSupport.stream(v.getEdges(direction, labels).spliterator(), parallel);
	}
	
	
	
	public VertexRoute out(String... labels){
		return outE().inV();
	}
	
	public VertexRoute in(String... labels){
		return inE().outV();
	}
	
}
