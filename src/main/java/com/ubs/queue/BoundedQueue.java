package com.ubs.queue;

public interface BoundedQueue<T> {

	void add(T element);
	T get();
	int getSize();
	boolean isEmpty();
	void printAllElements();
	
}
