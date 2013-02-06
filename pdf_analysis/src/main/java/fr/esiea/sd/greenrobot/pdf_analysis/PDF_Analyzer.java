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
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;

import fr.esiea.sd.greenrobot.pdf_analysis.extraction.KeywordsExtractor;
import fr.esiea.sd.greenrobot.pdf_analysis.graph.KeywordFactory;
import fr.esiea.sd.greenrobot.pdf_analysis.graph.KeywordsGraphBuilder;

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
	public KeywordsGraphBuilder call() throws Exception {

		Multimap<String, Integer> keywords = filtrateKeywords(extractKeywords(this.pdfDocument));

//		System.out.println("Added " + keywords.asMap().keySet().size() + " unique words after filtering.");

		return new KeywordsGraphBuilder(
			Lists.newArrayList(
				Iterables.transform(
					keywords.asMap().entrySet(), 
					KeywordFactory.asFunction()
				)	
			)
		);	
	}

}
