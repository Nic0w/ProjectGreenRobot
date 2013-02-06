package fr.esiea.sd.greenrobot.website;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
		
		int i = 20;
		
		PrintWriter out = response.getWriter();
		
		while(i>0) {
			
			out.printf("<script type=\"text/javascript\"> progress(%d); </script>", i);
			out.flush();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				break;
			}
			i--;
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
