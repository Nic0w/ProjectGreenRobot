package fr.esiea.sd.greenrobot.website;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.pdfbox.pdmodel.PDDocument;

import com.google.common.collect.Maps;
import com.google.common.util.concurrent.AsyncFunction;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.gson.Gson;

import fr.esiea.sd.greenrobot.pdf_analysis.concurrent.AsynchronousPDDocumentLoader;
import fr.esiea.sd.greenrobot.pdf_analysis.graph.KeywordsGraphBuilder;
import fr.esiea.sd.greenrobot.website.handler.PDFRetrievalHandler;
import fr.esiea.sd.greenrobot.website.json.Response;

/**
 * Servlet implementation class PDFReceiver
 */
@WebServlet("/PDFReceiver")
public class PDFReceiver extends HttpServlet {
	
	private enum RequestType {
		
		PDF_RETRIEVAL_REQUEST,
		JSON_REQUEST
	}
	
	private static final long serialVersionUID = 1L;
	
	private static final ListeningExecutorService threadExecutor = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(10));
    private static final Map<HttpSession, PDFAnalysisTask> tasks = Maps.newHashMap();
    
	private final PDFRetrievalHandler retrievalHandler;
	private final AsynchronousPDDocumentLoader pdfLoader;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PDFReceiver() {
        super();
        
        this.retrievalHandler = new PDFRetrievalHandler();
        this.pdfLoader = new AsynchronousPDDocumentLoader(threadExecutor);
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//GET Method is used when we send an URL.
		response.getWriter().printf("Downloading a PDF from somewhere !");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession userSession = request.getSession();
		
		switch(getRequestType(request)) {
		case JSON_REQUEST:

			break;
			
		case PDF_RETRIEVAL_REQUEST: {

			PDFAnalysisTask newTask = new PDFAnalysisTask();
			
			//Beware : Asynchronous black magic in use !
			ListenableFuture<PDDocument> pdfDocRetrieval = Futures.transform(
					this.retrievalHandler.retrieveFile(request, threadExecutor), //First we retrieve the File

					new AsyncFunction<File, PDDocument>() { //Then we load the PDDocument

						@Override
						public ListenableFuture<PDDocument> apply(File pdfFile) throws Exception { 

							return Futures.immediateFuture(PDDocument.load(pdfFile)); //This Future will be executed in the executor of our choice !
							//return pdfLoader.loadAsync(pdfFile);
						}
					}, threadExecutor); //We choose here the executor we want.
			
			//newTask.
			
			tasks.put(userSession, newTask);
			
			new Response().
				execute("updateProgress").
				addArg("state", "OPENING_PDF_DOC").
				writeTo(response.getWriter());
			
			break;
		}

		default:
			/* DAFUQ §? */
			//throw new DafuqException(new BugInTheMatrixException(new DividedByZeroException("Impossibruuuuuu")));
			break;

		}
	}

	private RequestType getRequestType(HttpServletRequest request) {
	
		if(request.getContentType().startsWith("multipart/form-data"))
			return RequestType.PDF_RETRIEVAL_REQUEST;
		
		return RequestType.JSON_REQUEST;
	}

}
