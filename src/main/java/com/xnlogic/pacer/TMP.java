package com.xnlogic.pacer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.Random;
import java.util.Set;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.Stream.Builder;
import java.util.stream.StreamSupport;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Element;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.tg.TinkerGraph;

public class TMP {

	static boolean parallel = true;

	// One-to-many mapping

	private static Function<Vertex, Stream<Edge>> edges(Direction direction, String... labels){
		return (v) -> StreamSupport.stream(v.getEdges(direction, labels).spliterator(), parallel);
	}

	public static Function<Vertex, Stream<Edge>> outE(String... labels){
		return edges(Direction.OUT, labels);
	}

	public static Function<Vertex, Stream<Edge>> inE(String... labels){
		return edges(Direction.IN, labels);
	}

	public static Function<Vertex, Stream<Edge>> bothE(String... labels){
		return edges(Direction.BOTH, labels);
	}
	
	
	private static <T extends Element> Function<T, Stream<T>> loop(Function<T, Stream<T>> loopBody, int n){
		return loop(loopBody, (element, i) -> i < n);
	}
	
	
	private static <T extends Element> Function<T, Stream<T>> loop(Function<T, Stream<T>> loopBody, BiPredicate<T, Integer> whileCondition){
		return _loop(loopBody, whileCondition, 0);
	}
	
	private static <T extends Element> Function<T, Stream<T>> _loop(final Function<T, Stream<T>> loopBody, final BiPredicate<T, Integer> whileCondition, final int iterationNumber){
		return (e) -> {
			if(whileCondition.test(e, iterationNumber)){
				return loopBody.apply(e).flatMap(_loop(loopBody, whileCondition, iterationNumber + 1));
			} else {
				return Stream.of(e);
			}
		};
	}

	
	static Function<Edge, Stream<Vertex>> bothV 		= (e) -> Arrays.asList(e.getVertex(Direction.IN), e.getVertex(Direction.OUT)).stream();  // FIXME: Parallel?

	// One-to-one mapping
	static Function<Edge, Vertex> inV 		= (e) -> e.getVertex(Direction.IN);
	static Function<Edge, Vertex> outV 	= (e) -> e.getVertex(Direction.OUT);

	// Filtering
	public static Predicate<Vertex> propertyMatch(final String key, final Object value){
		return (v) -> Objects.equals(v.getProperty(key), value);
	}


	static Function<Vertex, Stream<Vertex>> out = (v) -> outE().apply(v).map(inV);
	static Function<Vertex, Stream<Vertex>> in 	= (v) -> inE().apply(v).map(outV);


	// Starting point
	public static Stream<Vertex> v(Graph g, boolean parallel){
		return StreamSupport.stream(g.getVertices().spliterator(), parallel);
	}

	public static Stream<Vertex> v(Graph g){
		return v(g, parallel);
	}

	public static Stream<Edge> e(Graph g, boolean parallel){
		return StreamSupport.stream(g.getEdges().spliterator(), parallel);
	}

	public static Stream<Edge> e(Graph g){
		return e(g, parallel);
	}



	public static void main(String[] args) {

		Graph g = createTestGraph();

		//		printElements(
		//				v(g).filter(propertyMatch("name", "Alex"))
		//				);
		//
		//		printElements(
		//				v(g).filter(propertyMatch("name", "Alex"))
		//				.flatMap(outE())
		//				);
		//
		//		printElements(
		//				v(g).filter(propertyMatch("name", "Alex"))
		//				.flatMap(outE())
		//				.map(inV)
		//				);

		//		printElements(
		//				v(g).filter(propertyMatch("name", "Alex"))
		//				.flatMap(outE("likes"))
		//				.peek(e -> System.out.println(e))
		//				.map(inV)
		//				.peek(e -> System.out.println(e))
		//				.flatMap(outE())
		//				.peek(e -> System.out.println(e))
		//				.map(inV)
		//				.distinct()
		////				.count()
		//				);


		//		printElements(
		//				v(g).filter(propertyMatch("name", "Alex"))
		//				.flatMap(outE("likes")).map(inV)
		//				.map(Element::getId)
		//				.collect(Collectors.toList())
		//				);


		//		Stream<Vertex> route = v(g).filter(propertyMatch("name", "Alex"))
		//		.peek(e -> System.out.println(e))
		//		.flatMap(outE("likes"))
		//		.peek(e -> System.out.println(e))
		//		.map(inV)
		//		.peek(e -> System.out.println(e))
		//		.flatMap(outE("follows"))
		//		.peek(e -> System.out.println(e))
		//		.map(inV)
		//		.peek(e -> System.out.println(e))
		//		.flatMap(outE("likes"))
		//		.peek(e -> System.out.println(e))
		//		.map(inV)
		//		.peek(e -> System.out.println(e));

		//		Stream<Vertex> r = v(g).filter(propertyMatch("name", "Alex"));
		//		Stream<Vertex> r1 = loop(r, 
		//				(Stream<Vertex> s) -> s.flatMap(outE("likes")).map(inV).distinct(), 
		//				1);
		//
		//		r = v(g).filter(propertyMatch("name", "Alex"));
		//		Stream<Vertex> r2 = loop(r, 
		//				(Stream<Vertex> s) -> s.flatMap(outE("likes")).map(inV).distinct(), 
		//				2);
		//
		//		r = v(g).filter(propertyMatch("name", "Alex"));
		//		Stream<Vertex> r3 = loop(r, 
		//				(Stream<Vertex> s) -> s.flatMap(outE("likes")).peek(e -> System.out.println(e)).map(inV).distinct(), 
		//				3);
		//
		//		printElements(r);
		//		printElements(r1);
		//		printElements(r2);
		//		printElements(r3);
		//
		//
		//		r = Stream.of(g.getVertex(0));
		//		r3 = loop(r, 
		//				(Stream<Vertex> s) -> s.flatMap(outE("likes")).peek(e -> System.out.println(e)).map(inV).distinct(), 
		//				3);
		//		printElements(r3);


		//		r = v(g).filter(propertyMatch("name", "Alex"));





		//		v.flatMap(outE).map(inV).forEach(vertex -> System.out.println(vertex.getProperty("name")));
		//		v.filter(StartsWithA).flatMap(outE).map(inV).forEach(vertex -> System.out.println(vertex));
		//		v.map(outE).forEach(action);;






//		dfs();
		testLoop();
	}

	
	//=========================================================================
	// LOOP
	//=========================================================================
	
	private static void testLoop(){
		Graph g = createTestGraph();
		
		Function<Vertex, Stream<Vertex>> loopBody = (vertex) -> {
			return Stream.of(vertex).flatMap(out).distinct();
		}; 
		int n = 3;
		
		ConcurrentMap<String, Integer> m = v(g).filter(propertyMatch("name", "Alex"))
			.flatMap(loop(loopBody, n))
//			.distinct()
			.collect(Collectors.toConcurrentMap(
					(u) -> (String) u.getProperty("name"), 
					(u) -> 1, 
					(i1, i2) -> i1 + i2
				)
			)
//			.forEach((e) -> System.out.println(e + " : " + e.getProperty("name")));
			;
		System.out.println(m);
	}
	
	
	private static void testLoop2(){
		Graph g = createTestGraph();
		
		Function<Vertex, Stream<Vertex>> loopBody = (vertex) -> {
			return Stream.of(vertex).flatMap(out).distinct();
		}; 
		BiPredicate<Vertex, Integer> whileCondition = (e, i) -> i < 3;
		
		ConcurrentMap<String, Integer> m = v(g).filter(propertyMatch("name", "Alex"))
			.flatMap(loop(loopBody, whileCondition))
//			.distinct()
			.collect(Collectors.toConcurrentMap(
					(u) -> (String) u.getProperty("name"), 
					(u) -> 1, 
					(i1, i2) -> i1 + i2
				)
			)
//			.forEach((e) -> System.out.println(e + " : " + e.getProperty("name")));
			;
		System.out.println(m);
	}

	
	
	
	
	private static <T> void dummy(){
		Builder<Stream<Vertex>> builder = Stream.builder();
		//		 Stream.concat(a, b)
		builder.andThen((Stream<Vertex> v) -> {
			v.flatMap(outE("follows"));
		});
	}


	public static void reuseStreamsExample(){
		Supplier<Stream<String>> streamSupplier = () -> Stream.of("d2", "a2", "b1", "b3", "c")
				.filter(s -> s.startsWith("a"));

		streamSupplier.get().anyMatch(s -> true);   // ok
		streamSupplier.get().noneMatch(s -> true);  // ok
	}

	// COLLECTORS EXAMPLE
	// ==================
	//	
	//	Collector<Vertex, StringJoiner, String> personNameCollector =
	//    Collector.of(
	//        () -> new StringJoiner(" | "),          // supplier (supply string joiners)
	//        (j, p) -> j.add(p.name.toUpperCase()),  // accumulator (add one Person to a new StringJoinder)
	//        (j1, j2) -> j1.merge(j2),               // combiner (Combine the new and so-far String joiners)
	//        StringJoiner::toString);                // finisher
	//

	public static void bfs(){
		Graph g = createTestGraph();
		Vertex start = v(g).findAny().get();

		Consumer<Vertex> visitor = (v) -> System.out.println(v + " : " + v.getProperty("name"));

		Set<Vertex> visited = new HashSet<Vertex>();
		Queue<Vertex> q = new ConcurrentLinkedQueue<Vertex>();
		q.add(start);

		while(! q.isEmpty()){
			Vertex current = q.remove(); 
			Stream.of(current)
			.flatMap(outE())
			.map(inV)
			.filter((vertex) -> ! visited.contains(vertex))
			.forEach((vertex) -> {
				if(! q.contains(vertex)){
					q.add(vertex);
				}
			});

			visitor.accept(current);
			visited.add(current);
		}

		System.out.println("Done");
	}


	
	
	public static class MySpliterator extends Spliterators.AbstractSpliterator<Vertex> {
		
		private int time = 0;
		private Map<Vertex, Integer> discoveryTimes = new ConcurrentHashMap<Vertex, Integer>();
		private Stack<Vertex> q = new Stack<Vertex>();

		public MySpliterator(Vertex start)
		{
			super(Long.MAX_VALUE, 0);
			q.push(start);
			discoveryTimes.put(start,  time);
		}

		private void next()
		{
			Vertex vertex = null;
			
			vertex.getEdges(Direction.OUT).forEach(edge -> {
				
			});
		}

		@Override
		public boolean tryAdvance(Consumer<? super Vertex> action) {
			if(action == null){
				throw new NullPointerException();
			}
			// TODO Auto-generated method stub
			return false;
		}
	
	}


	public static void dfs(){

		Graph g = createTestGraph();
		Vertex start = v(g).findAny().get();

		BiConsumer<Vertex, Integer> visitor = (vertex, discoveryTime) -> {
			String prefix = IntStream.range(0, discoveryTime).mapToObj(i -> "   ").collect(Collectors.joining());
			System.out.println(prefix + vertex + " : " + vertex.getProperty("name"));
		}; 


		int time = 0;
		Map<Vertex, Integer> discoveryTimes = new ConcurrentHashMap<Vertex, Integer>();
		//		Map<Vertex, Integer> finishTimes = new ConcurrentHashMap<Vertex, Integer>();

		//		Queue<Vertex> q = new ConcurrentLinkedQueue<Vertex>();

		Stack<Vertex> q = new Stack<Vertex>();
		q.add(start);
		discoveryTimes.put(start,  time);


		while(! q.isEmpty()){
			Vertex currentV = q.pop();
			//			Vertex currentV = q.remove();
			int    currentD = discoveryTimes.get(currentV);
			time++;

			Stream.of(currentV)
			.flatMap(outE())
			.map(inV)
			.filter((vertex) -> ! discoveryTimes.containsKey(vertex))
			.forEach((vertex) -> {
				if(! discoveryTimes.containsKey(vertex)){
					q.push(vertex);
					//						q.add(vertex);
					discoveryTimes.put(vertex, currentD + 1);
				}
			});

			visitor.accept(currentV, currentD);
		}

		System.out.println("Done");
	}



	private static <T> Stream<T> loop(Stream<T> stream, Function<Stream<T>, Stream<T>> body, int times){
		for (int i = 0; i < times; i++) {
			stream = body.apply(stream);
		}
		return stream;
	}


	@SuppressWarnings("unchecked")
	public static void printElements(Object o){
		try{
			printStreamElements((Stream<? extends Element>) o);
		} catch (Exception e){
			System.out.println(o);
		} finally {
			System.out.println("\n");
		}
	}

	public static void printStreamElements(Stream<? extends Element> elements){
		Iterator<? extends Element> itr = elements.iterator();
		int count = 0;
		while(itr.hasNext()){
			Element e = itr.next();
			count++;
			System.out.println(e.toString() + 
					e.getPropertyKeys().stream()
					.map(key -> key + " : " + e.getProperty(key))
					.reduce((s1, s2) -> s1 + ", " + s2)
					.orElse("")
					);


		}

		System.out.println(String.format("Total: %d", count));
	}



	public static Graph createTestGraph(){
		Graph g = new TinkerGraph();
		Random random = new Random();

		List<String> names = Arrays.asList("Alex", "Bobby", "Charlie", "Daniel", "Eva", "Frank", "George", 
				"Hannah", "Iris", "Jack", "Kate", "Lily", "Mona", "Nathan", 
				"Olga","Perry", "Quentin", "Rachel", "Sam", "Tom", "Ulrich", "Veronica", 
				"Will", "Xao", "Yuri", "Zane");

		names.stream().forEach(name -> g.addVertex(null).setProperty("name", name));

		List<Vertex> vertices = new ArrayList<Vertex>();
		g.getVertices().forEach(v -> vertices.add(v));

		g.getVertices().forEach(v -> {
			vertices.remove(v);
			Collections.shuffle(vertices);
			vertices.stream().limit(random.nextInt(7)).forEach(v2 -> v.addEdge(random.nextBoolean() ? "follows" : "likes", v2));
			vertices.add(v);
		});

		return g;
	}

	
	
	
	public static class Dummy implements Spliterator<Vertex>{
		
		Vertex current;
		int loopCount = 0;
		int maxLoops = 10;
		

		@Override
		public boolean tryAdvance(Consumer<? super Vertex> action) {
			if(loopCount < maxLoops){
				action.accept(current);
				loopCount++;
//				current 
				return true;
			} else {
				return false;
			}
			
		}

		@Override
		public Spliterator<Vertex> trySplit() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long estimateSize() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public int characteristics() {
			// TODO Auto-generated method stub
			return 0;
		}
		
	}

}

