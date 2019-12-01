package com.ubs;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class OutputPresenterTest {

	OutputPresenter outputPresenter;
	static ConcurrentMap<String, Integer> allWords;
	
	@BeforeAll
	static void initializeWordsMap() {
		allWords = new ConcurrentHashMap<>();
		allWords.put("one", 1);
		allWords.put("two", 2);
		allWords.put("three", 3);
	}
	
	@Test
	void shouldReturnEmptyStringIfNoWordsAreDefined() {
		//given
		String[] selectedWords = {};
		outputPresenter = new OutputPresenter(allWords, selectedWords);
		
		//then
		Assertions.assertEquals("", outputPresenter.getResult());
	}
	
	@Test
	void shouldReturnOneWord() {
		//given
		String[] selectedWords = {"one"};
		outputPresenter = new OutputPresenter(allWords, selectedWords);
		
		//then
		Assertions.assertEquals("one=1", outputPresenter.getResult());
	}
	
	@Test
	void shouldReturnTwoWords() {
		//given
		String[] selectedWords = {"one", "two"};
		outputPresenter = new OutputPresenter(allWords, selectedWords);
		
		//then
		Assertions.assertEquals("one=1, two=2", outputPresenter.getResult());
	}

	@Test
	void shouldReturnTwoWordsForEmptyMap() {
		//given
		String[] selectedWords = {"one", "two"};
		ConcurrentMap<String, Integer> emptyMap = new ConcurrentHashMap<String, Integer>();
		outputPresenter = new OutputPresenter(emptyMap, selectedWords);
		
		//then
		Assertions.assertEquals("one=0, two=0", outputPresenter.getResult());
	}
	
	@Test
	void shouldReturnOneWordForMapAndAnotherOneWhichIsNotInTheMap() {
		//given
		String[] selectedWords = {"one", "zero"};
		outputPresenter = new OutputPresenter(allWords, selectedWords);
		
		//then
		Assertions.assertEquals("one=1, zero=0", outputPresenter.getResult());
	}
	
}
