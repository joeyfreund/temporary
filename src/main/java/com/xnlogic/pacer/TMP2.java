package com.xnlogic.pacer;

import com.tinkerpop.blueprints.Element;
import com.tinkerpop.blueprints.Vertex;

public class TMP2 {

	enum VorE {v, e};
	
	
	public static void useEnum(VorE vertex_or_edge){
		if(vertex_or_edge == VorE.v){
			// It's a vertex
		} else {
			// It's an edge
		}
	}
	
	public static void useAssignableFrom(Class<? extends Element> elementClass){
		if(Vertex.class.isAssignableFrom(elementClass)){
			// It's a vertex
		} else {
			// It's an edge
		}
	}
	
	
	public static void main(String[] args) {
		
		int n = 10 * 100 * 100 * 100;
		
		long t1 = System.currentTimeMillis();
		for (int i = 0; i < n; i++) {
			useEnum(VorE.v);
		}
		long t2 = System.currentTimeMillis();
		
		long t3 = System.currentTimeMillis();
		for (int i = 0; i < n; i++) {
			useAssignableFrom(Vertex.class);
		}
		long t4 = System.currentTimeMillis();
		
		
		System.out.println(String.format("Using enum: %6d ms\nUsing Class<?>: %6d ms", t2 - t1, t4 - t3));
	}
	
}
