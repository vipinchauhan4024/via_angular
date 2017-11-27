package com.patternservices.util;

public final class SysLogger {
	
	public static void logMethod(String methodName, String value) {
		System.out.println(" Method: " + methodName + "() logs value =|" + value +"|");
	}
	
	public static void logException(String methodName, Exception exception) {
		System.out.println(" Method: " + methodName + "() logs exception as =|" + exception.getClass().getSimpleName() +"| with Message as " + exception.getLocalizedMessage());
	}

}
