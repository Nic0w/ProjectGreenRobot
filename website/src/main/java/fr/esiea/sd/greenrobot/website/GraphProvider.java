package fr.esiea.sd.greenrobot.website;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multiset.Entry;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

import fr.esiea.sd.greenrobot.pdf_analysis.PDF_Analyzer;
import fr.esiea.sd.greenrobot.pdf_analysis.graph.Keyword;
import fr.esiea.sd.greenrobot.pdf_analysis.graph.KeywordsGraphBuilder;

/**
 * Servlet implementation class GraphProvider
 */
@WebServlet("/GraphProvider")
public class GraphProvider extends HttpServlet {

	private static final Map<String, PDF_Analyzer> analyzers = Maps.newHashMap();
	private static final Map<String, ListenableFuture<KeywordsGraphBuilder>> graphBuilders = Maps.newHashMap();

	private static final ListeningExecutorService executor = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(10));

	private static final long serialVersionUID = 1L;

	private static int dotLoadingAnim = 0;
	/**
	 * Default constructor. 
	 */
	public GraphProvider() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */		PDDocument pdfDoc;
	 PDF_Analyzer analyzer;
	 protected void doGet(final HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		 PrintWriter out = response.getWriter();



		 final String hash, file;

		 if((file=request.getParameter("file"))!=null) {

			 hash = ExtractionSession.getUniqueSessionHash(file, System.currentTimeMillis());

			 analyzers.put(hash, null);

			 ListenableFuture<PDDocument> loadDoc = executor.submit(new Callable<PDDocument>() {
				 @Override
				 public PDDocument call() throws Exception {

					 return PDDocument.load(new File("/tmp/tomcat6-tmp/"+file));
				 }
			 });

			 Futures.addCallback(
					 loadDoc, 
					 new FutureCallback<PDDocument>() {

						 @Override
						 public void onFailure(Throwable t) {
							 analyzers.remove(hash);
						 }

						 @Override
						 public void onSuccess(PDDocument pdfDoc) {

							 PDF_Analyzer analyzer = new PDF_Analyzer(pdfDoc);

							 analyzers.put(hash, analyzer);

							 graphBuilders.put(hash, executor.submit(analyzer));

						 }
					 }, 
					 executor);

			 out.printf("%s", hash);
		 }
		 else if((hash=request.getParameter("hash"))!=null) {

			 if(!analyzers.containsKey(hash)) {
				 throw new ServletException("Failure : impossible to load the document !");
			 }

			 PDF_Analyzer analyzer = analyzers.get(hash);
			 ListenableFuture<KeywordsGraphBuilder> graphBuilder = graphBuilders.get(hash);

			 int progress;

			 if(analyzer==null) { //PDDocument.load() not finished yet !

				 out.
				 append("<script type=\"text/javascript\">").
				 append("function update() { getProgress('"+hash+"'); } ").
				 append(" setTimeout(update, 500); ").
				 append("</script>\n").
				 append("Ouverture du ficher, merci de patienter");

				 for(int i=0; i<dotLoadingAnim;i++)
					 out.append('.');

				 dotLoadingAnim++;
				 dotLoadingAnim %=3;

				 out.append('\n');
			 }
			 else {
				 if(graphBuilder.isDone()) {
					 //Loading & extraction are done !

					 KeywordsGraphBuilder builder = null;
					 try {
						 builder = graphBuilder.get();
					 } catch (InterruptedException | ExecutionException e) {
						 throw new ServletException(e);
					 }

					 out.
					 append("<script type=\"text/javascript\">\n").
					 append(" loadKeywordSelector({ array: ["); //+builder.getAllKeywords().toString() + " });\n").

					 Iterator<Keyword> keywordIterator =  builder.getAllKeywords().iterator();

					 String keyword;
					 while(keywordIterator.hasNext()) {

						 keyword = keywordIterator.next().getWord();
						 out.append("'"+keyword+"'");
						 if(keywordIterator.hasNext())
							 out.append(',');			 
					 }

					 out.
					 append("]}); \n").
					 append("loadGraph(); \n").
					 append("</script>\n");

					 //out.append("LOADED OMG !");
				 } 
				 else {
					 progress = analyzer.getExtractionProgress();

					 out.
					 append("<script type=\"text/javascript\">").
					 append("function update() { getProgress('"+hash+"'); } ").
					 append(" setTimeout(update, 500); ").
					 append("</script>\n").
					 append("Extraction : " + progress + " %<br>\n");
				 }
			 }
		 }

		 out.flush();
		 out.close();

	 }

	 /**
	  * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	  */
	 protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		 BufferedReader reader = request.getReader();

		 List<Keyword> selectedWords = Lists.newArrayList();

		 String jsonData = reader.readLine();

		 try {
			 JSONObject jsonObject = new JSONObject(jsonData);

			 String hash = jsonObject.getString("hash");

			 KeywordsGraphBuilder graphBuilder = graphBuilders.get(hash).get();

			 JSONArray array = jsonObject.getJSONArray("selected");

			 int count = 0;
			 for(Keyword k : graphBuilder.getAllKeywords()) {
				 for(int i=0; i< array.length();i++)
					 if(array.getString(i).contentEquals(k.getWord())) {
						 selectedWords.add(k);
						 count++;
						 break;
					 }
				 if(count == array.length()) break;
			 }

			 JSONArray jsonResponse = new JSONArray();
			 
			 JSONObject formatData = new JSONObject().
					 put("$color", "#83548B").
					 put("$type", "circle").
					 put("$dim", 10);
			 
			 /*
			  * 
		{
			"id" : "aUniqueIdentifier",
			"name" : "usually a nodes name",
			"data" : {
				"$color" : "#83548B",
				"$type" : "circle",
				"$dim" : 10
			},
			"adjacencies" : [ "anOtherNode" ]
		}
			  * 
			  */
			 
			 JSONObject node;
			 JSONArray adjacencies, nodes = new JSONArray();
			 int id=0;
			 for(Keyword k : selectedWords) {
				 node = new JSONObject().
						 put("id", id++).
						 put("name", k.getWord()).
						 put("data", formatData);
				 
				 adjacencies = new JSONArray();
				 
				 for(Map.Entry<Keyword, Float> e : k.getNearbyKeywords().entrySet())
					 adjacencies.put(e.getKey().getWord());
		
				 node.put("adjacencies", adjacencies);

				 nodes.put(node);
			 }
			 
			 response.getWriter().println(nodes.toString());

		 } catch (JSONException | InterruptedException | ExecutionException e) {
			 throw new ServletException(e);
		 }

	 }

}
