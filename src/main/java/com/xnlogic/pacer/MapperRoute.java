package com.xnlogic.pacer;

import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class MapperRoute<S, E>{
	
//	private Stream<S> starts;
	private Function<S, E> mapper;
	private boolean parallel;
	
	private Iterable<S> starts;
	
	public Stream<E> stream(){
		// FIXME: Default splitarator is probably not what we want
		// FIXME: Stream gotcha, with parallelism
		Stream<S> s = StreamSupport.stream(starts.spliterator(), parallel);  
		return s.map(mapper);
	}

	
	
	
	
	
	public class ConnectorOneToOne<S,E>{
		
	}
	
	
}
