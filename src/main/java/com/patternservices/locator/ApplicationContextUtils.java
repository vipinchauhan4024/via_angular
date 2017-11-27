package com.patternservices.locator;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.patternservices.util.SysLogger;

public class ApplicationContextUtils implements ApplicationContextAware {

	private static ApplicationContext CONTEXT;

	public void setApplicationContext(ApplicationContext context)
			throws BeansException {
		SysLogger.logMethod("setApplicationContext", (null != context) ? context.toString() : "null");
		CONTEXT = context;
	}

	public static ApplicationContext getApplicationContext() {
		return CONTEXT;
	}

}
