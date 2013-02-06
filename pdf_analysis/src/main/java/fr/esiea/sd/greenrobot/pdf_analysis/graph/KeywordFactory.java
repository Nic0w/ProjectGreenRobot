package fr.esiea.sd.greenrobot.pdf_analysis.graph;

import java.util.Collection;
import java.util.Map.Entry;

import com.google.common.base.Function;

public class KeywordFactory {
	
	private static Function<Entry<String, Collection<Integer>>, Keyword> internalFactory = null;
	
	public static Function<Entry<String, Collection<Integer>>, Keyword> asFunction() {
		
		if(internalFactory == null)
			internalFactory = new Function<Entry<String, Collection<Integer>>, Keyword>() {
				
				@Override
				public Keyword apply(Entry<String, Collection<Integer>> mapEntry) {
					
					return new Keyword(mapEntry.getKey(), mapEntry.getValue());
					
				}
			};
				
			return internalFactory;
	}
	
	public static Keyword newKeyword(Entry<String, Collection<Integer>> mapEntry) {
		return asFunction().apply(mapEntry);
	}

}
