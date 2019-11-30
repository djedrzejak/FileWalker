package com.ubs.queue;

class BoundedQueueNode<T> {

	private T value;
	private BoundedQueueNode<T> next;
	
	public BoundedQueueNode(T value) {
		this.value = value;
	}

	public BoundedQueueNode<T> getNext() {
		return next;
	}

	public void setNext(BoundedQueueNode<T> next) {
		this.next = next;
	}

	public T getValue() {
		return value;
	}
	
}
