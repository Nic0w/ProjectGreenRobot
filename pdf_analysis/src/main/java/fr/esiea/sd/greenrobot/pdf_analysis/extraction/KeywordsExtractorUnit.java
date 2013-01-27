/**
 * 
 */
package fr.esiea.sd.greenrobot.pdf_analysis.extraction;

import java.util.Scanner;
import java.util.concurrent.Callable;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;

import com.google.common.collect.ContiguousSet;
import com.google.common.collect.DiscreteDomain;
import com.google.common.collect.DiscreteDomains;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Range;
import com.google.common.collect.Ranges;

import fr.esiea.sd.greenrobot.pdf_analysis.filtering.IgnoredWords;
import fr.esiea.sd.greenrobot.pdf_analysis.filtering.WordFilter;

/**
 * @author nic0w
 *
 */
public class KeywordsExtractorUnit implements Callable<Multimap<String, Integer>> {

	private final PDDocument pdfDocument;
	
	private final ContiguousSet<Integer> pagesRange;
	
	/**
	 * 
	 */
	public KeywordsExtractorUnit(PDDocument pdfDocument) {
		this(pdfDocument, 1, pdfDocument.getNumberOfPages());
	}

	/**
	 * 
	 */
	public KeywordsExtractorUnit(PDDocument pdfDocument, int page) {
		this(pdfDocument, page, page);
	}
	
	
	/**
	 * 
	 */
	public KeywordsExtractorUnit(PDDocument pdfDocument, int startPage, int endPage) {
		
		this.pdfDocument = pdfDocument;
		
		this.pagesRange = Ranges.closed(startPage, endPage).asSet(DiscreteDomains.integers());
	}
	
	@Override
	public Multimap<String, Integer> call() throws Exception {
		PDFTextStripper textExtractor = new PDFTextStripper();
		
		String rawText, word;
		Scanner words;
		
		int wordsCount = 0;
		
		HashMultimap<String, Integer> keywords = HashMultimap.create();
		
		WordFilter filter = new WordFilter(IgnoredWords.list);
		
		for(Integer page : this.pagesRange) {
			
			textExtractor.setStartPage(page);
			textExtractor.setEndPage(page);
			
			System.out.printf("Extracting keywords from page %d of %d.\n", page, this.pagesRange.last());
			
			rawText = textExtractor.getText(pdfDocument);
			
			words = new Scanner(rawText).useDelimiter(" ");
			
			while(words.hasNext()) {
				
				word = words.next().toLowerCase().trim();
				
				if(filter.apply(word)) {
					//System.out.println("Added : '" +word + "'");
					keywords.put(word, new Integer(wordsCount++));
				}
			}
			
			words.close();
		}
		
	
		

		
		return keywords;
	}



}
