package com.xnlogic.pacer;


public class Example {

	public static void main(String[] args) {
		boolean parallel = true;
		VertexRoute r = new VertexRoute(Route.vertices(TMP.createTestGraph(), parallel));
		r.outE("follows").inV().forEach((v) -> System.out.println(v + " " + v.getProperty("name")));
	}
}
