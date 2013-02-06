package fr.esiea.sd.greenrobot.website;

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

import com.google.common.collect.Maps;

import fr.esiea.sd.greenrobot.pdf_analysis.PDF_Analyzer;

/**
 * Servlet implementation class GraphProvider
 */
@WebServlet("/GraphProvider")
public class GraphProvider extends HttpServlet {
	
	private static final Map<String, PDF_Analyzer> analyzers = Maps.newHashMap();
	
	private static final long serialVersionUID = 1L;

	private static int i;
    /**
     * Default constructor. 
     */
    public GraphProvider() {
        // TODO Auto-generated constructor stub
    }

    
    private static String bytesToHex(byte[] bytes) {
        final char[] hexArray = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
        char[] hexChars = new char[bytes.length * 2];
        int v;
        for ( int j = 0; j < bytes.length; j++ ) {
            v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		
		PrintWriter out = response.getWriter();
		String file;
		if((file=request.getParameter("file"))!=null) {
			
			byte md5[] = {};
			try {
				md5 = MessageDigest.getInstance("MD5").digest((file + "" + System.currentTimeMillis()).getBytes());
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
			
			String hash = bytesToHex(md5);
			
			i=20;
			out.printf("%s", hash);
		}
		else if(true) {
			
			if(i>0) {
				out.printf("<script type=\"text/javascript\"> function update() { getProgress('noob'); } setTimeout(update, 1500); </script>\n%d\n", i--);
				
			}
			else {
				out.printf("LOADED OMG\n");
			}
		}
		
		
			//

		
		out.flush();
		out.close();
				
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		
		
		
	}

}
