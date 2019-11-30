package com.ubs;

import java.io.File;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.ubs.queue.BoundedLinkedQueue;
import com.ubs.queue.BoundedQueue;

public class Application {
	
	public static final int QUEUE_CAPACITY = 5;
	
	public static void main(String[] args) {
		BoundedQueue<File> files = new BoundedLinkedQueue<>(QUEUE_CAPACITY);
		ConcurrentMap<String, Integer> allWords = new ConcurrentHashMap<>();

		String location = args[0];
		int wordCounterThreadsNumber = Integer.valueOf(args[1]);
		String[] selectedWords = Arrays.copyOfRange(args, 2, args.length);
		
		Runnable fileWalkerRunnable = new FileWalker(files, location);
		Runnable wordCounterRunnable = new WordCounter(files, allWords);
		Runnable outputPresenterRunnable = new OutputPresenter(allWords, selectedWords);
		
		Thread fileWalker = new Thread(fileWalkerRunnable, "FileWalker");
		Runtime.getRuntime().addShutdownHook(fileWalker);
		fileWalker.start();

		for (int i=1; i<=wordCounterThreadsNumber; i++) {
			Thread wordCounter = new Thread(wordCounterRunnable, "WordCounter " + i);
			Runtime.getRuntime().addShutdownHook(wordCounter);
			wordCounter.start();
		}
		
		Thread outputPresenter = new Thread(outputPresenterRunnable);
		Runtime.getRuntime().addShutdownHook(outputPresenter);
		outputPresenter.start();
	}
}
