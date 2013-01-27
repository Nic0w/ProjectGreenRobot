/**
 * 
 */
package fr.esiea.sd.greenrobot.pdf_analysis.filtering;

import java.io.FilterWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

import com.google.common.base.CharMatcher;
import com.google.common.base.Predicate;

/**
 * @author nic0w
 *
 */
public class WordFilter implements Predicate<String> {

	private final List<String> ignoreList;
	
	
	public WordFilter(List<String> ignoreList) {
		
		this.ignoreList = ignoreList;
		
	}

	
	private boolean containsDigits(String input) {

		return CharMatcher.JAVA_DIGIT.matchesAnyOf(input);
	}
	
	private boolean containsOnlyLetters(String input) {

		return !CharMatcher.JAVA_LETTER.matchesAllOf(input);
	}
	
	@Override
	public boolean apply(String input) {
	
		if(input == null) 
			return false;
		
		if(input.isEmpty()) 
			return false;
		
		/*if(containsDigits(input)) 
			return false;*/
		
		if(!CharMatcher.JAVA_LETTER.matchesAllOf(input)) 
			return false;
		
		if(input.endsWith("ons") || input.endsWith("ent"))
			return false;
		
		if(this.ignoreList.contains(input)) 
			return false;
		
		return true;
	}
	
	
	
	
}
