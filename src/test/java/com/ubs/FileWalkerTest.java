package com.ubs;

import java.io.File;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.ubs.queue.BoundedLinkedQueue;
import com.ubs.queue.BoundedQueue;

class FileWalkerTest {

	final int queueCapacity = 2;
	BoundedQueue<File> queue;
	String location;
	
	@BeforeEach
	void initializeQueue() {
		queue = new BoundedLinkedQueue<File>(queueCapacity);
		location = "src/test/resources";
	}
	
	@Test
	void exceptionShouldBeThrownWhenFolderLocationDoNotExist() {
		//given
		location = "A:\\FolderWhichNotExists";
		FileWalker walker = new FileWalker(queue, location);
		
		//then
		Assertions.assertThrows(RuntimeException.class, () -> walker.listFilesFromLocation(location));
	}
	
	@Test
	void shouldNotBeEmptyAfterAddingToEmptyQueue() {
		//given
		FileWalker walker = new FileWalker(queue, location);
		
		//then
		Assertions.assertTrue(walker.getFiles().isEmpty());
		walker.listFilesFromLocation(location);
		Assertions.assertTrue(!walker.getFiles().isEmpty());
	}
	
	@Test
	void shouldThrowExceptionWhenAddingToQueueWhichIsFull() {
		//given
		FileWalker walker = new FileWalker(queue, location);

		//when
		for(int i=0; i<queueCapacity; i++) {
			walker.getFiles().add(new File("testEntry " + i));	
		}
		
		//then
		Assertions.assertTrue(queue.getSize() == queueCapacity);
		Assertions.assertThrows(IndexOutOfBoundsException.class, () -> walker.listFilesFromLocation(location));
	}

}
