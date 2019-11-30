package com.ubs.queue;

public class BoundedLinkedQueue<T> implements BoundedQueue<T> {

	private BoundedQueueNode<T> firstNode;
	private final int maxSize;
	private int size;
	
	public BoundedLinkedQueue(int maxSize) {
		if(maxSize < 1) {
			throw new RuntimeException("BoundedQueue can not have size less then 1");
		}
		this.maxSize = maxSize;
		this.size = 0;
	}
	
	@Override
	public void add(T element) {
		if(firstNode != null) {
			BoundedQueueNode<T> tmp = firstNode;
			int currentElementIndex = 1;
			while (tmp.getNext() != null) {
				tmp = tmp.getNext();
				currentElementIndex++;
				if(currentElementIndex >= maxSize) {
					throw new IndexOutOfBoundsException("BoundedQueue is full");
				}
			}
			tmp.setNext(new BoundedQueueNode<T>(element));
		} else {
			firstNode = new BoundedQueueNode<T>(element);
		}
		size++;
	}
	
	@Override
	public T get() {
		if(firstNode != null) {
			T value = firstNode.getValue();
			firstNode = firstNode.getNext();
			size--;
			return value;			
		}
		return null;
	}
	
	@Override
	public int getSize() {
		return size;
	}
	
	@Override
	public boolean isEmpty() {
		return firstNode == null;
	}
	
	@Override
	public void printAllElements() {
		if(firstNode != null) {
			BoundedQueueNode<T> tmp = firstNode;
			while (tmp != null) {
				System.out.println(tmp.getValue().toString());
				tmp = tmp.getNext();
			}
		} else {
			System.out.println("BoundedQueue is empty");
		}
	}
}
