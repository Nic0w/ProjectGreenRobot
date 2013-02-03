/**
 * 
 */
package fr.esiea.sd.greenrobot.pdf_analysis;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.FilterWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;

import fr.esiea.sd.greenrobot.pdf_analysis.extraction.KeywordsExtractor;

/**
 * @author nic0w
 *
 */
public class PDF_Analyzer implements Callable<Object> {

	private final PDDocument pdfDocument;
	private final HashMultimap<String, Integer> keywords;

	/**
	 * 
	 */
	public PDF_Analyzer(PDDocument pdf) {
		this.pdfDocument = pdf;
		this.keywords = HashMultimap.create();
	}

	private Multimap<String, Integer> extractKeywords(PDDocument doc) throws InterruptedException, ExecutionException {
		
		ExecutorService extractorExecutor = Executors.newSingleThreadExecutor();
		
		KeywordsExtractor extractor = new KeywordsExtractor(this.pdfDocument);
		
		Future<Multimap<String, Integer>> extractorResult = extractorExecutor.submit(extractor);
		
		return extractorResult.get();
	}
	
	private Multimap<String, Integer> filtrateKeywords(Multimap<String, Integer> keywords) {
		
		List<String> toDelete = Lists.newArrayList();
		
		for(Entry<String, Collection<Integer>> keyword : keywords.asMap().entrySet())
			if(keyword.getValue().size() < 10)
				toDelete.add(keyword.getKey());
		
		for(String keyword : toDelete)
			keywords.removeAll(keyword);
		
		return keywords;
	}
	
	@Override
	public Object call() throws Exception {
		
		Multimap<String, Integer> keywords = filtrateKeywords(extractKeywords(this.pdfDocument));
		
		

		int currentPage, wordsCount;



			System.out.println("Added " + keywords.asMap().keySet().size() + " unique words after filtering.");
			System.out.println();
			
			File out = new File("out.csv");
			
			BufferedWriter bw = new BufferedWriter(new FileWriter(out));
			
			for(Entry<String, Collection<Integer>> word : keywords.asMap().entrySet())
				bw.write(word.getKey()+"; "+ word.getValue().size() +"\n");
			
			bw.flush();
			bw.close();
			
		//	System.out.println(this.keywords.size() + " occurences." + wordsCount);
			
			int /*pos1, pos2,*/ i=0, keyword1_index, keyword2_index;
			
			/*keyword2_index = 0;
			String keywordsArray[] = this.keywords.keySet().toArray(new String[] {});
			
			for(keyword1_index = 0; keyword1_index < this.keywords.keySet().size(); keyword1_index++) {
			

				String keyword1 = keywordsArray[keyword1_index];
				Set<Integer> keyword1Positions = this.keywords.get(keyword1);
				
				for(keyword2_index = 0; keyword2_index < this.keywords.keySet().size(); keyword2_index++) {
					
					if(keyword2_index == keyword1_index)
						continue;
					
					
					String keyword2 = keywordsArray[keyword2_index];
					Iterator<Integer> keyword1PosIterator = this.keywords.get(keyword1).iterator();
					
					int sum = 0, count = 0;
					
					while(keyword1PosIterator.hasNext()) {
						
						int min = Integer.MAX_VALUE, d, keyword1Pos = keyword1PosIterator.next();
						
						//System.out.println("K1 pos = " + keyword1Pos);
						
						for(Integer keyword2Pos : this.keywords.get(keyword2)) {
						
						//	System.out.println("K2 pos = " + keyword2Pos);
							
							if((d=Math.abs(keyword1Pos - keyword2Pos)) < min) 
								min = d;
						}
							
						//System.out.println("Min distance : " + min);
						sum += min;
					}
					
					double mean = ((double)sum) / ((double)keyword1Positions.size());
					
					if(mean < 10.0)
					System.out.println("Mean distance betweeen '"+keyword1+"' & '"+keyword2 + "' = " + mean);
					
				}

				i++;
				//if(i==1) break;
			}*/
			
			
	
		return null;	
	}

}
