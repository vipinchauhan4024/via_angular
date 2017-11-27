package com.patternservices.service.impl;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.patternservices.datatype.ApplicationType;
import com.patternservices.service.PatternDataService;
import com.patternservices.util.GenerateAllPaterns;
import com.patternservices.util.SysLogger;

public class PatternDataServiceImpl implements PatternDataService {

	public String fetchAllAppTypes() {

		String enumAsString = null;

		try {
			enumAsString = new ObjectMapper().writeValueAsString(ApplicationType
					.values());
			SysLogger.logMethod("fetchAllAppTypes", enumAsString);
		} catch (JsonGenerationException e) {
			SysLogger.logException("fetchAllAppTypes", e);
			e.printStackTrace();
		} catch (JsonMappingException e) {
			SysLogger.logException("fetchAllAppTypes", e);
			e.printStackTrace();
		} catch (IOException e) {
			SysLogger.logException("fetchAllAppTypes", e);
			e.printStackTrace();
		}

		return enumAsString;
	}

	public String submitPatternData(String pattern) {
		
	    System.out.println("Pattern" + pattern);
	    
	    GenerateAllPaterns gap = new GenerateAllPaterns();
        
        String s[]= new String[] {pattern};
      //  System.out.println("**"+getServletContext().getContextPath());
        String graph=gap.testmaincall(s);
	    
        System.out.println("GRAPH" + graph);
        
	    return graph;
	}

}
