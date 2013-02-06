package fr.esiea.sd.greenrobot.website;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.pdfbox.pdmodel.PDDocument;

import com.google.common.collect.Maps;

import fr.esiea.sd.greenrobot.pdf_analysis.PDF_Analyzer;

/**
 * Servlet implementation class GraphProvider
 */
@WebServlet("/GraphProvider")
public class GraphProvider extends HttpServlet {
	
	private static final Map<String, PDF_Analyzer> analyzers = Maps.newHashMap();
	
	private static final long serialVersionUID = 1L;
	
    /**
     * Default constructor. 
     */
    public GraphProvider() {
        // TODO Auto-generated constructor stub
    }
 
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		PrintWriter out = response.getWriter();
		String file, hash;
		PDDocument pdfDoc;
		PDF_Analyzer analyzer;
		
		if((file=request.getParameter("file"))!=null) {
			
			hash = ExtractionSession.getUniqueSessionHash(file);
			
			pdfDoc = PDDocument.load(new File("/tmp/tomcat6-tmp/"+file));
			
			analyzer = new PDF_Analyzer(pdfDoc);
			
			analyzers.put(hash, analyzer);
	
			out.printf("%s", hash);
		}
		else if((hash=request.getParameter("hash"))!=null) {
			
			int progress = analyzers.get(hash).getExtractionProgress();
			
			if(progress<99) {
				out.printf(
					"<script type=\"text/javascript\">" +
					"function update() { getProgress('%s'); } " +
					" setTimeout(update, 1000); "+
					"</script>\n"+ 
					"%d % du document traité...\n", hash, progress);
				
			}
			else {
				out.printf("LOADED OMG\n");
			}
		}
		
		out.flush();
		out.close();
				
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		
		
		
	}

}
