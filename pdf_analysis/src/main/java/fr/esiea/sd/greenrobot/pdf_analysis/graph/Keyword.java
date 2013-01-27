/**
 * 
 */
package fr.esiea.sd.greenrobot.pdf_analysis.graph;

import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import com.google.common.collect.Maps;

/**
 * @author nic0w
 *
 */
public class Keyword {

	private final Map<Keyword, Double> nearbyKeywords;
	
	private final String word;
	
	private final int occurences;
	
	/**
	 * 
	 */
	public Keyword(String word, int occurence) {

		this.nearbyKeywords = Maps.newHashMap();
		
		this.word = word;
		
		this.occurences = occurence;
	}


}
