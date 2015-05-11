package com.xnlogic.pacer;

import java.util.Iterator;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;

public class Route<T> {
	
	
	public static Stream<Vertex> vertices(Graph g, boolean parallel){
		return StreamSupport.stream(g.getVertices().spliterator(), parallel);
	}
	
	public static Stream<Vertex> vertices(Graph g){
		return vertices(g, false);
	}

	public static Stream<Edge> edges(Graph g, boolean parallel){
		return StreamSupport.stream(g.getEdges().spliterator(), parallel);
	}

	public static Stream<Edge> edges(Graph g){
		return edges(g, false);
	}
	
	//=========================================================================

	protected Stream<T> elements;
	private boolean parallel;


	public Route(Stream<T> elements, boolean parallel) {
		this.elements = elements;
		this.parallel = parallel;
	}

	public Route(Stream<T> vertices) {
		this(vertices, false);
	}
	
	public boolean isParallel() {
		return parallel;
	}
	
	/**
	 * Terminal operation.
	 */
	public void forEach(Consumer<T> action){
		this.elements.forEach(action);
	}
	
	/**
	 * Terminal operation.
	 */
	public Iterator<T> iterator(){
		return this.elements.iterator();
	}
}
