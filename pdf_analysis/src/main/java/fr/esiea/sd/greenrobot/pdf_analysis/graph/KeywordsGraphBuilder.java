package fr.esiea.sd.greenrobot.pdf_analysis.graph;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Multimap;

public class KeywordsGraphBuilder {
	
	private final List<Keyword> keywords;
	
	public KeywordsGraphBuilder(List<Keyword> keywords) {
		
		this.keywords = keywords;
		
	}
	
	private void linkKeywords(Keyword a, Keyword b, Float distance) {
		
		a.addNeighbor(b, distance);
		b.addNeighbor(a, distance);
	}
	
	private int computeMinDistance(int posA, Keyword b) {
		
		int min = Integer.MAX_VALUE, d=0;
		
		for(Integer posB : b.getOccurences())
			if((d=Math.abs(posA - posB)) < min) min = d;
			
		return min;
	}
	
	private float computeMeanDistance(Keyword a, Keyword b) {
	
		float mean = 0;
		
		for(Integer posA : a.getOccurences())
			mean += computeMinDistance(posA, b);
	
		mean /= ((float)a.getOccurences().size());
		
		return mean;
	}
	
	public List<Keyword> buildGraphFor(int maxDepth, float neighborMaxDistance, Keyword ...selectedKeywords) {
		
		float distance;
		
		for(Keyword a : selectedKeywords) {
			
			a.clearNeighbors();
			
			for(Keyword b : this.keywords) {
				
				if(b.equals(a)) continue;
				
				b.clearNeighbors();
				
				distance = computeMeanDistance(a, b);
				
				if(distance <= neighborMaxDistance)
					linkKeywords(a, b, distance);
			}	
		}
		
		
		return Arrays.asList(selectedKeywords);
	}
	
	
	
	
}
