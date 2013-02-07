package fr.esiea.sd.greenrobot.website;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import fr.esiea.sd.greenrobot.pdf_analysis.PDF_Analyzer;

public class ExtractionSession {

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
	
	public static String getUniqueSessionHash(String filename, long seed) {
		
		byte md5[] = {};
		
		try {
			md5 = MessageDigest.getInstance("MD5").digest((filename + "" + seed).getBytes());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		return bytesToHex(md5);	
	}

}
