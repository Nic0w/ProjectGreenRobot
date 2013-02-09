package fr.esiea.sd.greenrobot.pdf_analysis;

import java.io.File;
import java.io.FileWriter;
import java.io.FilterWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Scanner;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
import org.apache.pdfbox.util.PDFTextStripperByArea;

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
    	
    //	pdfDoc.getDocument().
    	
    	PDFTextStripper stripper = new PDFTextStripper();
    
    	PrintWriter  w= null;
    	
    	stripper.writeText(pdfDoc, new Writer() {
			
    		
    		
			@Override
			public Writer append(char c) throws IOException {
				System.out.println("a");
				return this;
			}

			@Override
			public Writer append(CharSequence csq, int start, int end)
					throws IOException {
				System.out.println("b");
				return this;
			}

			@Override
			public Writer append(CharSequence csq) throws IOException {
				System.out.println("c");
				return this;
			}

			@Override
			public void write(char[] cbuf) throws IOException {
				System.out.println("d");
			}

			@Override
			public void write(int c) throws IOException {
				System.out.println("e");
			}

			@Override
			public void write(String arg0, int arg1, int arg2)
					throws IOException {
				System.out.println("f");
			}

			@Override
			public void write(String str) throws IOException {
				System.out.println("g");
			}

			@Override
			public void write(char[] arg0, int arg1, int arg2) throws IOException {
				System.out.println("h");			
			}
			
			@Override
			public void flush() throws IOException {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void close() throws IOException {
				// TODO Auto-generated method stub
				
			}
		});
        	
    	/*PDF_Analyzer analyzer = new PDF_Analyzer(pdfDoc);
  
    	System.out.println("Cores availables : " + Runtime.getRuntime().availableProcessors());
    	
    	KeywordsGraphBuilder graph = analyzer.call();*/
    	

    	
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
