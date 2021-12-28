/*
 * Isabella Stone
 * I pledge my honor that I have abided by the Stevens Honor System.
 */

package anagrams;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Anagrams {
	final Integer[] primes =  
			{2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 
			31, 37, 41, 43, 47, 53, 59, 61, 67, 
			71, 73, 79, 83, 89, 97, 101};
	Map<Character,Integer> letterTable;
	Map<Long,ArrayList<String>> anagramTable;

	public Anagrams() {
		buildLetterTable();
		anagramTable = new HashMap<Long,ArrayList<String>>();
	}
	
	/**
	 * Builds letterTable for use of matching alphabet to
	 * prime numbers to help compute the hash codes
	 */
	public void buildLetterTable() {
		letterTable = new HashMap<Character, Integer>();
		for (int i = 0; i < 26; i++) {
			letterTable.put((char)(i + 97), primes[i]);
		}
	}

	/**
	 * Adds s to anagramTable
	 * @param s is the String to add into anagramTable
	 */
	public void addWord(String s) {
		long index = this.myHashCode(s);
		//ArrayList<String> values = anagramTable.get(index);
		if (anagramTable.get(index) == null) {
			ArrayList<String> values;
			values = new ArrayList<String>();
			values.add(s);
			anagramTable.put(index, values);
		}
		else { 
			//arrayList already exists so add in pre-existing array
			if (anagramTable.get(index).contains(s)) {
				throw new IllegalArgumentException("addWord: duplicate value");
			}
			anagramTable.get(index).add(s);
		}
	}
	
	/**
	 * Creates hash code to use for insertion into anagramTable
	 * @param s is String to compute hash code of
	 * @return hash code of s
	 */
	public long myHashCode(String s) {
		if (s.equals("")) {
			throw new IllegalArgumentException("String cannot be empty");
		}
		long hash = 1;
		for (int i = 0; i < s.length(); i++) {
			//get key for each letter
			hash *= letterTable.get(s.charAt(i));
		}
		return hash;
	}
	
	public void processFile(String s) throws IOException {
		FileInputStream fstream = new FileInputStream(s);
		BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
		String strLine;
		while ((strLine = br.readLine()) != null)   {
		  this.addWord(strLine);
		}
		br.close();
	}
	
	/**
	 * Finds entries with max amount of anagrams
	 * @return ArrayList of max entries
	 */
	public ArrayList<Map.Entry<Long,ArrayList<String>>> getMaxEntries() {
	    int max = 0;
	    ArrayList<Map.Entry<Long,ArrayList<String>>> output = new ArrayList<Map.Entry<Long,ArrayList<String>>>();
	   
	    for (Map.Entry<Long,ArrayList<String>> entry : anagramTable.entrySet()) {
	    	if (entry.getValue() != null) {
	    		//only check the keys that have values
	    		if (entry.getValue().size() > max) {
		    		//replace what's in output list
		    		output.clear();
		    		output.add(entry);
		    		//reset max
		    		max = entry.getValue().size();
		    	}
		    	else if (entry.getValue().size() == max) {
		    		//add entry to output list
		    		output.add(entry);
		    	}
	    	}
	    }
	    return output;
	}
	
	public static void main(String[] args) {		
		Anagrams a = new Anagrams();

		final long startTime = System.nanoTime();    
		try {
			a.processFile("words_alpha.txt");
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		ArrayList<Map.Entry<Long,ArrayList<String>>> maxEntries = a.getMaxEntries();
		final long estimatedTime = System.nanoTime() - startTime;
		final double seconds = ((double) estimatedTime/1000000000);
		System.out.println("Time: "+ seconds);
		System.out.println("List of max anagrams: "+ maxEntries);
		
		
		
		/*
		 * I created my own 'dictionaries' to further test these functions
		 *
		/*
		Anagrams a = new Anagrams();

		final long startTime = System.nanoTime();    
		try {
			a.processFile("words_g.txt");
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		ArrayList<Map.Entry<Long,ArrayList<String>>> maxEntries = a.getMaxEntries();
		final long estimatedTime = System.nanoTime() - startTime;
		final double seconds = ((double) estimatedTime/1000000000);
		System.out.println("Time: "+ seconds);
		System.out.println("List of max anagrams: "+ maxEntries);
		*/
		
	}
}
