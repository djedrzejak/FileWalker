package com.ubs;

import java.io.File;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.ubs.queue.BoundedLinkedQueue;
import com.ubs.queue.BoundedQueue;

class ApplicationIT {

	static final int QUEUE_CAPACITY = 5;
	static final int NUMBER_OF_WORD_COUNTER_THREADS = 3;
	
	String location;
	int wordCounterThreadsNumber;
	
	Runnable fileWalkerRunnable;
	Runnable wordCounterRunnable;
	
	BoundedQueue<File> files = new BoundedLinkedQueue<>(QUEUE_CAPACITY);
	ConcurrentMap<String, Integer> allWords = new ConcurrentHashMap<>();
	
	@BeforeEach
	void initializeObjects() {
		files = new BoundedLinkedQueue<>(QUEUE_CAPACITY);
		allWords = new ConcurrentHashMap<>();
		location = "src/test/resources";
		wordCounterThreadsNumber = NUMBER_OF_WORD_COUNTER_THREADS;
		fileWalkerRunnable = new FileWalker(files, location);
		wordCounterRunnable = new WordCounter(files, allWords);
	}
	
	@Test
	void shouldFulfillQueueAndGetAllValuesFromIt() throws InterruptedException {
		//given
		Thread fileWalker = new Thread(fileWalkerRunnable, "FileWalker");
		Runtime.getRuntime().addShutdownHook(fileWalker);
		fileWalker.start();
	
		//when
		Thread.sleep(2000);
		Assertions.assertTrue(!files.isEmpty());
		for (int i=1; i<=wordCounterThreadsNumber; i++) {
			Thread wordCounter = new Thread(wordCounterRunnable, "WordCounter " + i);
			Runtime.getRuntime().addShutdownHook(wordCounter);
			wordCounter.start();
		}
		
		//then
		Thread.sleep(2000);
		//every file from list was taken by wordCounter threads
		Assertions.assertTrue(files.isEmpty());
	}

	@Test
	void shouldPresentGivenValues() throws InterruptedException {
		//given
		Thread fileWalker = new Thread(fileWalkerRunnable, "FileWalker");
		Runtime.getRuntime().addShutdownHook(fileWalker);
		fileWalker.start();
		for (int i=1; i<=wordCounterThreadsNumber; i++) {
			Thread wordCounter = new Thread(wordCounterRunnable, "WordCounter " + i);
			Runtime.getRuntime().addShutdownHook(wordCounter);
			wordCounter.start();
		}
		OutputPresenter outputPresenter = new OutputPresenter(allWords, new String[] {"one", "test"});
		
		//then
		Thread.sleep(3000);
		Assertions.assertEquals("one=1, test=4", outputPresenter.getResult());
	}
	
}






