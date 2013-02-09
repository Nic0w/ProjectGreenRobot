/**
 * 
 */
package fr.esiea.sd.greenrobot.pdf_analysis.concurrent;

import java.io.File;
import java.util.concurrent.Callable;

import org.apache.pdfbox.pdmodel.PDDocument;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;

/**
 * @author nic0w
 *
 */
public class AsynchronousPDDocumentLoader {

	private final ListeningExecutorService executorService;
	/**
	 * 
	 */
	public AsynchronousPDDocumentLoader(ListeningExecutorService executor) {
		this.executorService = executor;
	}
	
	public ListenableFuture<PDDocument> loadAsync(final File pdf) {
		return this.executorService.submit(new Callable<PDDocument>() {
			@Override
			public PDDocument call() throws Exception {
				return PDDocument.load(pdf);
			}
		});
	}

}
