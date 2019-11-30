package com.ubs;

import java.util.concurrent.ConcurrentMap;

public class OutputPresenter implements Runnable {

	private ConcurrentMap<String, Integer> allWords;
	private String[] selectedWords;
	private static final int SLEEP_TIME = 3000;
	
	public OutputPresenter(ConcurrentMap<String, Integer> allWords, String[] selectedWords) {
		this.allWords = allWords;
		this.selectedWords = selectedWords;
	}
	
	@Override
	public void run() {
		while(true) {
			try {
				System.out.println(getResult());
				Thread.sleep(SLEEP_TIME);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private String getResult() {
		StringBuilder result = new StringBuilder();
		for (String word : selectedWords) {
			result.append(word).append("=");
			result.append(allWords.containsKey(word) ? allWords.get(word) : 0).append(", ");
		}
		return result.toString().replaceAll(", $", "");
	}
}
