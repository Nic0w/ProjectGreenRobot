package fr.esiea.sd.greenrobot.pdf_analysis.extraction;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.pdfbox.pdmodel.PDDocument;

import com.google.common.collect.Multimap;

public class KeywordsExtractor implements Callable<Multimap<String, Integer>> {

	private final PDDocument pdfDocument;
	
	public KeywordsExtractor(PDDocument doc) {
		
		this.pdfDocument = doc;
		
	}

	@Override
	public Multimap<String, Integer> call() throws Exception {
	
		int nbPages = this.pdfDocument.getNumberOfPages(); 
		int nbCores = Runtime.getRuntime().availableProcessors();
		
		int pagesBlock = nbCores >= nbPages ? 1 : nbPages/nbCores;

		
		ExecutorService unitExecutor = Executors.newFixedThreadPool(nbCores);
		
		//Mono thread !s
		Future<Multimap<String, Integer>> result = unitExecutor.submit(new KeywordsExtractorUnit(this.pdfDocument));
		
		
		return result.get();
	}

}
