package com.ubs;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.concurrent.ConcurrentMap;

import com.ubs.queue.BoundedQueue;

public class WordCounter implements Runnable {

	private BoundedQueue<File> files;
	private ConcurrentMap<String, Integer> words;
	
	public WordCounter(BoundedQueue<File> files, ConcurrentMap<String, Integer> words) {
		this.files = files;
		this.words = words;
	}
	
	public void run() {
		while(true) {
			synchronized (files) {
				try {
					while (files.isEmpty()) {
						files.wait();
					}
					readFile();
					files.notifyAll();
					files.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}	
	}
	
	private void readFile() {
		File file = files.get();
		String line;
		String[] wordsFromLine;
		try {
			Scanner scanner = new Scanner(file);
			while(scanner.hasNextLine()) {
				line = scanner.nextLine();
				wordsFromLine = line.split("\\s+");
				for(String word : wordsFromLine) {
					words.merge(word, 1, Integer::sum);
				}
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
