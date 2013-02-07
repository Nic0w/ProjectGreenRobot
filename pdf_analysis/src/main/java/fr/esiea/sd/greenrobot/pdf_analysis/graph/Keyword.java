/**
 * 
 */
package fr.esiea.sd.greenrobot.pdf_analysis.graph;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import com.google.common.collect.Maps;

/**
 * @author nic0w
 *
 */
public class Keyword {

	private final Map<Keyword, Float> nearbyKeywords;
	
	private final String word;
	
	private final Collection<Integer> occurences;
	
	/**
	 * 
	 */
	public Keyword(String word, Collection<Integer> occurences) {

		this.nearbyKeywords = Maps.newHashMap();
		
		this.word = word;
		
		this.occurences = occurences;
	}

	public void addNeighbor(Keyword k, Float d) {
		
		this.nearbyKeywords.put(k, d);
		
	}
	
	public String getWord() {
		return this.word;
	}
	
	public int getCount() {
		return this.occurences.size();
	}
	
	public Collection<Integer> getOccurences() {
		return this.occurences;
	}

	public void clearNeighbors() {
		this.nearbyKeywords.clear();
	}
	
	@Override
	public boolean equals(Object obj) {
		
		Keyword other;
		
		if(obj instanceof Keyword) {
			other = (Keyword) obj;
			
			return this.word.contentEquals(other.word);
		}
		
		return false;
	}

	public Map<Keyword, Float> getNearbyKeywords() {
		return this.nearbyKeywords;
	}
	
	@Override
	public String toString() {
		return this.getWord() + "(" + this.occurences.size() +")";
	}

	

}
