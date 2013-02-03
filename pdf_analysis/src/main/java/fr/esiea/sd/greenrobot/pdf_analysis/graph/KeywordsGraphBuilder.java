package fr.esiea.sd.greenrobot.pdf_analysis.graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;

public class KeywordsGraphBuilder {

	private int nbOfOccurencesFilter=10;
	
	
	
	/**
	 * @param min
	 * @return An instance of the current graph builder.
	 */
	public KeywordsGraphBuilder requiredKeywordAppearance(int min) {
	
		this.nbOfOccurencesFilter = min;
		
		return this;
	}
	
	private Function<Entry<String, Collection<Integer>>, Keyword> keywordFactory() {
		
		return new Function<Entry<String, Collection<Integer>> , Keyword>() {
			
			public Keyword apply(Entry<String, Collection<Integer>> keywordPositions) {
				
				return new Keyword(keywordPositions.getKey(), keywordPositions.getValue().size());
			}
		};
	}
	
	public List<Keyword> build(Multimap<String, Integer> keywordsPositions) {	
		
		ArrayList<Keyword> nodeList = Lists.newArrayList(
			Iterables.transform(
				keywordsPositions.asMap().entrySet(), 
				keywordFactory()
			)
		);
		
		
		
		
		
		
		
		
		
		
		return null;
	}

}
