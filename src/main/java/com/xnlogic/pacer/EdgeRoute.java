package com.xnlogic.pacer;

import java.util.Arrays;
import java.util.stream.Stream;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;

public class EdgeRoute extends Route<Edge>{

	public EdgeRoute(Stream<Edge> elements, boolean parallel) {
		super(elements, parallel);
	}

	public EdgeRoute(Stream<Edge> elements) {
		this(elements, false);
	}

	
	public VertexRoute inV(){
		return new VertexRoute(this.elements.map((e) -> e.getVertex(Direction.IN)), isParallel());
	}
	
	public VertexRoute outV(){
		return new VertexRoute(this.elements.map((e) -> e.getVertex(Direction.OUT)), isParallel());
	}
	
	public VertexRoute bothV(){
		return new VertexRoute(this.elements.flatMap((e) -> Arrays.asList(e.getVertex(Direction.IN), e.getVertex(Direction.OUT)).stream()), 
				isParallel());
	}
	  
	
}
