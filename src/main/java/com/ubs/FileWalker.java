package com.ubs;

import java.io.File;

import com.ubs.queue.BoundedQueue;

public class FileWalker implements Runnable {

	public static final String extension = ".txt";
	
	private BoundedQueue<File> files;
	private String location;
	
	public FileWalker(BoundedQueue<File> files, String location) {
		this.files = files;
		this.location = location;
	}
	
	public void run() {
		listFilesFromLocation(location);
	}
	
	public void listFilesFromLocation(String location) {
		File folder = new File(location);
		if(!folder.exists()) {
			throw new RuntimeException("Invalid filepath");
		}
		for(File fileEntry : folder.listFiles()) {
			synchronized (files) {
				while (files.getSize() == Application.QUEUE_CAPACITY) {
					try {
						files.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				if (fileEntry.isDirectory()) {
					listFilesFromLocation(fileEntry.getAbsolutePath());
				} else if (fileEntry.getName().endsWith(extension)) {
					files.add(fileEntry);
					files.notifyAll();
				}
			}
		}
	}
	
	public BoundedQueue<File> getFiles() {
		return files;
	}
}
