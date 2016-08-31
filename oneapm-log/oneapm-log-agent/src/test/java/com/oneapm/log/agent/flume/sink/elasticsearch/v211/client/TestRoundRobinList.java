package com.oneapm.log.agent.flume.sink.elasticsearch.v211.client;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.powermock.reflect.Whitebox;


public class TestRoundRobinList{
	
	
	@Test
	public <T> void testGet(){
	List list = new ArrayList();
	list.add("hello");
	Iterator<T> iterator = list.iterator();
	Collection<T> elements = new ArrayList();
	RoundRobinList roundRobinList = new RoundRobinList(elements);
	Whitebox.setInternalState(roundRobinList, "iterator", iterator);
	Whitebox.setInternalState(roundRobinList, "elements", elements);
	roundRobinList.get();
	}
	
	@Test
	public <T> void testGet1(){
	List list = new ArrayList();
	Iterator<T> iterator = list.iterator();
	Collection<T> elements = new ArrayList();
	elements.add((T) "e");
	RoundRobinList roundRobinList = new RoundRobinList(elements);
	Whitebox.setInternalState(roundRobinList, "iterator", iterator);
	Whitebox.setInternalState(roundRobinList, "elements", elements);
	roundRobinList.get();
	}
	
	@Test
	public <T> void testSize(){
		List list = new ArrayList();
		list.add("hello");
		Iterator<T> iterator = list.iterator();
		Collection<T> elements = new ArrayList();
		RoundRobinList roundRobinList = new RoundRobinList(elements);
		Whitebox.setInternalState(roundRobinList, "iterator", iterator);
		Whitebox.setInternalState(roundRobinList, "elements", elements);
		int size = roundRobinList.size();
		Assert.assertEquals(0, size);
	}
}