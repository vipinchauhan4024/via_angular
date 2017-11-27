package com.patternservices.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;


public class FileOperations {
    private final static Logger LOGGER = Logger.getLogger(FileOperations.class.getName());
	public String [] readFile(String filename) {
		 BufferedReader br = null;
		 FileReader fr = null;
		 String[] nfrs = new String[100];
		 try {
		         ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
				//br = new BufferedReader(new FileReader(FILENAME));
		         LOGGER.log(Level.SEVERE, filename);
				//fr = new FileReader(filename);
			//	br = new BufferedReader(fr);
				InputStream inputStream = classLoader.getResourceAsStream(filename);
				br = new BufferedReader(new InputStreamReader(inputStream));
				String sCurrentLine;
				
				int i =0;

				while ((sCurrentLine = br.readLine()) != null) {
					nfrs[i] = sCurrentLine;
					i= i + 1;
					
					
				}

			} catch (IOException e) {
			    LOGGER.log(Level.SEVERE, e.getMessage());
				e.printStackTrace();

			} finally {

				try {

					if (br != null)
						br.close();

					if (fr != null)
						fr.close();

				} catch (IOException ex) {

					ex.printStackTrace();

				}

			}
		 return nfrs;
	}

}
