package fr.esiea.sd.greenrobot.pdf_analysis.extraction;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.pdfbox.pdmodel.PDDocument;

import com.google.common.collect.Multimap;

public class KeywordsExtractor implements Callable<Multimap<String, Integer>>, PagesCounter {

	private final PDDocument pdfDocument;
	
	private int nbPages;
	private AtomicInteger pagesTreated;
	
	public KeywordsExtractor(PDDocument doc) {
		
		this.pdfDocument = doc;
		
	}

	@Override
	public Multimap<String, Integer> call() throws Exception {
	
		this.nbPages      = this.pdfDocument.getNumberOfPages(); 
		this.pagesTreated = new AtomicInteger(0);
		
		int nbCores  = Runtime.getRuntime().availableProcessors();
		
		int pagesBlock = nbCores >= nbPages ? 1 : nbPages/nbCores;

		
		ExecutorService unitExecutor = Executors.newFixedThreadPool(nbCores);
		
		//Mono thread !
		Future<Multimap<String, Integer>> result = unitExecutor.submit(new KeywordsExtractorUnit(this.pdfDocument, this));
		//TODO Parallelize this shit !
		
		return result.get();
	}

	@Override
	public void addOnePage() {
		this.pagesTreated.incrementAndGet();
	}
	
	public int getExtractionProgress() {
		return Math.round(((float) this.pagesTreated.get()) / ((float) this.nbPages) * 100.0f);
	}

}
