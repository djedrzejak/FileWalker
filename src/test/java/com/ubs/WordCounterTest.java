package com.ubs;

import java.io.File;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.ubs.queue.BoundedLinkedQueue;
import com.ubs.queue.BoundedQueue;

class WordCounterTest {

	final int queueCapacity = 2;
	BoundedQueue<File> queue;
	ConcurrentMap<String, Integer> allWords;
	
	@BeforeEach
	void initializeQueue() {
		queue = new BoundedLinkedQueue<File>(queueCapacity);
		allWords = new ConcurrentHashMap<>();
	}
	
	@Test
	void shouldThrowNullPointerExceptionWhenTryToGetFileFromEmptyQueue() {
		//given
		WordCounter counter = new WordCounter(queue, allWords);
		
		//then
		Assertions.assertThrows(NullPointerException.class, () -> counter.readFile());
	}
	
	@Test
	void shouldThrowIllegalArgumentExceptionIfFileInQueueIsIncorrect() {
		//given
		queue.add(new File("A:\\FolderWhichNotExists\\nofile.txt"));
		WordCounter counter = new WordCounter(queue, allWords);
		
		//then
		Assertions.assertThrows(IllegalArgumentException.class, () -> counter.readFile());
	}
	
	@Test
	void shouldAddWordsFromFileToMap() {
		//given
		queue.add(new File("src/test/resources/testfile.txt"));
		WordCounter counter = new WordCounter(queue, allWords);
		
		//when
		Assertions.assertTrue(allWords.size() == 0);
		counter.readFile();
		
		//then
		Assertions.assertTrue(allWords.size() > 0);
	}
	
	@Test
	void shouldFindSpecifiedWordXTimesInFile() {
		//given
		queue.add(new File("src/test/resources/testfile.txt"));
		WordCounter counter = new WordCounter(queue, allWords);
		String testedWord = "test";
		Integer numberOfOccurences = 3;
		
		//when
		counter.readFile();
		
		//then
		Assertions.assertEquals(numberOfOccurences, allWords.get(testedWord));
	}
	
	
	@Test
	void shouldNotAddToMapWordThatWasNotFoundInFile() {
		//given
		queue.add(new File("src/test/resources/testfile.txt"));
		WordCounter counter = new WordCounter(queue, allWords);
		String testedWord = "someLongWord";
		
		//when
		Assertions.assertNull(allWords.get(testedWord));
		counter.readFile();
		
		//then
		Assertions.assertNull(allWords.get(testedWord));
	}
	
	@Test
	void shouldCountSelectedWordFromTwoFiles() {
		//given
		queue.add(new File("src/test/resources/testfile.txt"));
		queue.add(new File("src/test/resources/secondtestfile.txt"));
		WordCounter counter = new WordCounter(queue, allWords);
		String testedWord = "test";
		Integer numberOfOccurences = 4;
		
		//when
		while (queue.getSize() > 0) {
			counter.readFile();
		}
		
		//then
		Assertions.assertEquals(numberOfOccurences, allWords.get(testedWord));
	}
	
}
