package fr.esiea.sd.greenrobot.pdf_analysis;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import com.google.common.collect.TreeMultiset;

import fr.esiea.sd.greenrobot.pdf_analysis.graph.KeywordsGraphBuilder;

/**
 * Hello world!
 *
 */
public class Main {
    public static void main( String[] args ) throws Exception {
    	
    	PDDocument pdfDoc = null;
    	
    	pdfDoc = PDDocument.load(new File("bnp_2010.pdf"));
    	
    	PDFTextStripper stripper = new PDFTextStripper();
    	
    	PDF_Analyzer analyzer = new PDF_Analyzer(pdfDoc);
  
    	System.out.println("Cores availables : " + Runtime.getRuntime().availableProcessors());
    	
    	KeywordsGraphBuilder graph = analyzer.call();
    	

    	
    	/*for(String word : IgnoredWords.ignoreList)
    		System.out.println("'" + word + "'");*/
    	
    //	File out = new File("out.txt");
    	
    	//FileWriter writer = new FileWriter(out);
    	
    	//stripper.writeText(pdfDoc, writer);
    	
    	//writer.flush();
    //	writer.close();
    	
    	//Scanner words = new Scanner(stripper.getText(pdfDoc)).useDelimiter("\\. ");
    	
    	//TreeMultiset<String> histogram = TreeMultiset.create();
    	
    	//String word = "";
    	//int count = 0;
    	//while(words.hasNext()) {
    		//histogram.add(words.next());
    		//System.out.println("'"+words.next()+"'");
    		//count++; 
    	//}
    	
    //	System.out.println(/*histogram.entrySet().size() + " distinct words in this document. " + count + " words");
    	

    	
    	
        System.out.println( "Hello World!" );
    }
}
