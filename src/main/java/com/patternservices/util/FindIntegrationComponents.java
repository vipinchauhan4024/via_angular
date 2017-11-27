package com.patternservices.util;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Scanner;



public class FindIntegrationComponents {

	public String E2EintegrationString = "";
	public String rootFilename = "/configuration/";
    String basePath = new File("").getAbsolutePath();
    
	public String  selectedSendingConnectivity = "";
	public String  selectedSendingConversion = "";
	public String  selectedSendingTarsformer= "";
	public String  selectedSendingCrossRef= "";
	public String  selectedReceivingConnectivity = "";
	public String  selectedReceivingConversion = "";
	public String  selectedReceivingTarsformer= "";
	public String  selectedReceivingCrossRef= "";
	public String  checkConnectivityOption= "";
	String sendingApplicationType = "";
	String receivingApplicationType = "";
	
	public FindIntegrationComponents(){
	/*    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String rel = "/configuration/Conversion.txt";
        URL url1 = null;
        url1 = classLoader.getResource(rel);
        rootFilename = url1.getPath();
        rootFilename = rootFilename.substring(1, rootFilename.lastIndexOf("Conversion.txt"));
        System.out.println(url1.getPath());
        String fileName = rootFilename+"MISP.txt";
        InputStream input = null;
        while (input == null) {
         input = classLoader.getResourceAsStream(rel);
        }*/
	  //  rootFilename = "../../configuration/";
	}
	public void getCoreCapability()
	{
		
		 userInterface();
		 		 
	 
	}
	public void userInterface() {
		
		welcomeMessage();
				
		System.out.println("What is the kind of the Sending application");
		
	
		int nfrChoice[] = new int[5];
		String integrationChoiceSending []= new String[10];
		HashMap<String,String> integrationOptionsSending = new HashMap<String, String>();
		String NFRSQuestions[] = new String [10];
		String filename = rootFilename + "ApplicationQuestionaire.txt";	
		int choice = displayApplicationQuestions(filename);
		if(choice== -1) {
			System.out.println("Entered Value for application is not correct");
			return;
		}
		// Interpret the application selected
		sendingApplicationType = findApplicationType(choice, filename);
		if(sendingApplicationType.equals("COTS") || sendingApplicationType.equals("External")) {
			checkConnectivityOption = "DetermineConnectivity";
		}
		integrationChoiceSending =  chooseIntegrationComponents(sendingApplicationType);
			// Extract the integration components for selected sending application
			integrationOptionsSending = loadDataToArray(integrationChoiceSending);
			filename = rootFilename + "NFRSQuestionaire.txt";
			FileOperations fileObj = new FileOperations();			
			NFRSQuestions = fileObj.readFile(filename);
			int i =0;
			
			 while(NFRSQuestions[i] != null) {
					 System.out.println(NFRSQuestions[i]);
					 Scanner reader = new Scanner(System.in);
					 String inputValue = reader.next();
					 int inputValueAsInt = ValidateUserInputs(inputValue, NFRSQuestions[i],1);
					 if(inputValueAsInt == -1) {
						 System.out.println("The entered value is not correct for the program");
						 return;
					 }
					 
					 nfrChoice [i] = inputValueAsInt;			 
					 i=i+1;
			 }
		// Different process to determine COTS/external connectivity
		if(checkConnectivityOption.equals("DetermineConnectivity")) {
			filename = rootFilename + "CotsQuestionairetxt.txt";
			System.out.println("What is the connectivity supported for Sending Application: ");
			choice = displayApplicationQuestions(filename);
			selectedSendingConnectivity = findCotsConnectivity(choice);
			checkConnectivityOption = "";
			
		}else {
		
			 selectedSendingConnectivity =  chooseNFRSBasedIntegrationObjects( nfrChoice,integrationOptionsSending,NFRSQuestions,"connectivity");
		}
		 if(selectedSendingConnectivity.equals("")) {
			 selectedSendingConnectivity = "None of the connectivity option alone fits, contact Support ADF Integration CoE DRS <Support.ADF.Integration@volvo.com>";
		 }
		 
		 // Find about the receiving application
		 System.out.println("What is the kind of the Receiving application");
		 filename = rootFilename + "ApplicationQuestionaire.txt";
		 choice = displayApplicationQuestions(filename);
		 receivingApplicationType = findApplicationType(choice,filename);
		 if(receivingApplicationType.equals("COTS") || receivingApplicationType.equals("External")) {
				checkConnectivityOption = "DetermineConnectivity";
			}
		 int checkTransformFlag =0;
		 if(receivingApplicationType.equals("COTS") || selectedSendingConnectivity.equals("VCOM")) {
			 checkTransformFlag =1; 
		 }
		 // If both sending and receiving application connectivity is same, find the transformation needed
		 if (sendingApplicationType.equals(receivingApplicationType) && checkTransformFlag==0) {
			 
			 selectedReceivingConnectivity = selectedSendingConnectivity;
			 selectedReceivingTarsformer =findTheTransformer(nfrChoice,integrationOptionsSending,NFRSQuestions,"transformation");
			 checkForSomeSpecialRules();
			 DisplayTheIntegration(sendingApplicationType,receivingApplicationType);					 
		 }
		 else {
			 	String integrationChoiceReceiving []=  chooseIntegrationComponents(receivingApplicationType);
				// Extract the integration components for selected receiving application
				 HashMap<String,String> integrationOptionsReceiving = loadDataToArray(integrationChoiceReceiving);			 
				 // Directly ask the connectivity capability for COTS
				 if(checkConnectivityOption.equals("DetermineConnectivity")) {
					 filename = rootFilename + "CotsQuestionairetxt.txt";	
					 System.out.println("What is the connectivity supported for receiving Application: ");
					 choice = displayApplicationQuestions(filename);
					 selectedReceivingConnectivity = findCotsConnectivity(choice); 
					 
				 }else { 
	
				 selectedReceivingConnectivity = chooseNFRSBasedIntegrationObjects( nfrChoice,integrationOptionsReceiving,NFRSQuestions,"connectivity");
				 }		 
			 selectedReceivingTarsformer =findTheTransformer(nfrChoice,integrationOptionsReceiving,NFRSQuestions,"transformation");
			 if (!(selectedReceivingConnectivity.equals(selectedSendingConnectivity))) {
				 selectedReceivingConversion =  findTheConversionOption(); 
			 }
			 
			 checkForSomeSpecialRules();
			 DisplayTheIntegration(sendingApplicationType,receivingApplicationType);
			 
		 }
		 
		}
	public void welcomeMessage() {
		System.out.println("My name is VIA, Virtual Integration Assistant. I can help with deciding the End to End Integration patterns. We will start with some basic questions");
		
		System.out.println("--------------------------------------------------------------------");
		
	}
	public int ValidateUserInputs(String inputValue, String type, int count) {
		
		int outPutValue = -1;
		 
			if (checkIfDigit(inputValue)) {
				
				outPutValue = Integer.parseInt(inputValue);
			}
			else {
				inputValue = getTheinputAgain();
				outPutValue=ValidateUserInputs(inputValue,type,1);
			}		   		 
		
		if (type.equalsIgnoreCase("Application")) {
			if(outPutValue < 1 || outPutValue > count ) {
				//outPutValue = -1;
				inputValue = getTheinputAgain();
				outPutValue=ValidateUserInputs(inputValue,type, count);
			}
		}
		if(type.contains("Type 1 if Yes Type 0 if No")){
			if(!(outPutValue == 1 || outPutValue == 0) ) {
				//outPutValue = -1;
				inputValue = getTheinputAgain();
				outPutValue=ValidateUserInputs(inputValue,type,1);
			}
		}
		return outPutValue;
		
	}
	public String getTheinputAgain() {
		
		System.out.println("Entered value not correct try again");
		Scanner reader = new Scanner(System.in);
		String inputValue = reader.next();
		
		return inputValue;
		
	}
	public boolean checkIfDigit(String value) {
		
		try {
			
				Integer.parseInt(value);
			}catch(NumberFormatException nfe)  
			{  
			
				return false;  
		  } 
		return true;
	}
	public void checkForSomeSpecialRules() {
		
	
		if(receivingApplicationType.equals("External")) {
			if(selectedSendingConnectivity.equals("Webservices") && selectedReceivingConnectivity.equals("Webservices")) {
				selectedReceivingConversion = "SWS";
			}
		}
		if(selectedSendingConnectivity.equals("SAPPI") && selectedSendingConnectivity.equals(selectedReceivingConnectivity)) {
			if(!selectedReceivingTarsformer.equalsIgnoreCase("GimR")) {
				selectedReceivingTarsformer = "";
			}			
		}
		// SAP PI always needs MQ to communicate with non SAP system
		if(selectedSendingConnectivity.equals("SAPPI") && !selectedReceivingConnectivity.equals("SAPPI")) {
			selectedSendingConnectivity = selectedSendingConnectivity + "-->MQ";
		}
		if(selectedReceivingConnectivity.equals("SAPPI") && !selectedSendingConnectivity.equals("SAPPI")) {
			selectedReceivingConnectivity = "MQ-->" + selectedReceivingConnectivity;
		}
		// If the sending is Sap and the transformer chosen is IIB, it is not needed.
				if(selectedSendingConnectivity.contains("SAPPI") && (selectedReceivingTarsformer.equals("IIB") || selectedReceivingTarsformer.equals("GEBIS")) ) {
					selectedReceivingTarsformer = "";
				}
		
		// To ensure the Broker is in the middle with MQ as the input mainly for MQFT cases
		if((selectedReceivingTarsformer.equals("IIB") || selectedReceivingTarsformer.equals("GimR")) &&  selectedReceivingConversion.contains(">MQ")) {
			 
			String  conversionArray []  = selectedReceivingConversion.split("-->");
			selectedSendingConversion = conversionArray [0] + "-->" + conversionArray [1];
			selectedReceivingConversion = conversionArray [2];
			//selectedReceivingTarsformer = conversionArray [1] + "-->" + selectedReceivingTarsformer;
			
		}
		// If GEBIS is present and transformation needed remove, IIB if prsent
		if(selectedReceivingTarsformer.equals("IIB") &&  (selectedReceivingConversion.equals("GEBIS") || selectedSendingConversion.contains("GEBIS")) ) {
			selectedReceivingTarsformer = "";
		}
		if(!(sendingApplicationType.equals("External") || receivingApplicationType.equals("External"))) {
			if(selectedReceivingTarsformer.equals(selectedReceivingConversion) || (selectedReceivingTarsformer.equals("IIB") && selectedReceivingConversion.equals("HMG") ) ) {
				selectedReceivingConversion = "";
			}
		}		
		// MQFT and VCOMMQGW should be close to the file/VCOM 
		if((selectedSendingConnectivity.equals("File") || selectedSendingConnectivity.equals("VCOM") ) && (selectedReceivingTarsformer.equals("IIB") || selectedReceivingTarsformer.equals("GEBIS") || selectedReceivingTarsformer.equals("GimR") || selectedReceivingTarsformer.equals("SAPPI")) && !selectedReceivingConversion.equals("")  && selectedSendingConversion.equals("")) {
			selectedSendingConversion = selectedReceivingConversion + "-->MQ";
			selectedReceivingConversion = "";
			//selectedReceivingTarsformer =  selectedReceivingTarsformer;
		}
		// A MQ is needed when transformation is included along with file or VCOM
		if((selectedReceivingConnectivity.equals("File") || selectedReceivingConnectivity.equals("VCOM") ) && (selectedReceivingTarsformer.equals("IIB") || selectedReceivingTarsformer.equals("GEBIS") || selectedReceivingTarsformer.equals("GimR") || selectedReceivingTarsformer.equals("SAPPI")) && !selectedReceivingConversion.equals("") ) {
			selectedReceivingConversion = "MQ-->" + selectedReceivingConversion;
			
		}
		// If on the sending side the transformer is IIB and conversion is HMG, and this is done for non external scenario.
		if(!(sendingApplicationType.equals("External") || receivingApplicationType.equals("External"))) {
			if(selectedReceivingTarsformer.equals(selectedSendingConversion) || (selectedReceivingTarsformer.equals("IIB") && selectedSendingConversion.contains("HMG") ) ) {
				selectedSendingConversion = "";
			}
		}
		
		//Some changes for External scenario with webservices
		if(receivingApplicationType.equals("External") && selectedReceivingConnectivity.equals("Webservices") && !selectedReceivingTarsformer.equals("")) {
			selectedReceivingTarsformer = selectedReceivingTarsformer + "-->MQ";
		}
		
		//Some changes for External scenario with webservices
		if(sendingApplicationType.equals("External") && selectedSendingConnectivity.equals("Webservices") && !selectedReceivingTarsformer.equals("")) {
			selectedSendingConversion = selectedReceivingConversion + "-->MQ";
			selectedReceivingConversion = "";
					
		}	
		// only in SAPPI cases the Transformer and the connectivity could be same
		if(selectedReceivingConnectivity.contains(selectedReceivingTarsformer)) {
			selectedReceivingTarsformer = "";
		}
		
		//If both end is VCOM and transformation is needed, conversion gateway is required.
		if(selectedSendingConnectivity.equals(selectedReceivingConnectivity) && selectedSendingConnectivity.equals("VCOM") && selectedReceivingTarsformer.equals("IIB")) {
			selectedReceivingTarsformer = "VCOMMQGW-->MQ-->" + selectedReceivingTarsformer + "-->MQ-->VCOMMQGW";
		}
		
		//if GEBIS is present as both transformer and conversion reset transformer
		if((selectedReceivingTarsformer.equals("GEBIS") ||selectedSendingTarsformer.equals("GEBIS")) && selectedReceivingConversion.contains("GEBIS") ) {
			selectedReceivingTarsformer = "";
			selectedSendingTarsformer = "";
			
		}
	}
	public String findCotsConnectivity(int choice) {
		String connectivity = "";
		switch(choice){
		
		case 1 : connectivity = "MQ";
				 break;
		case 2 : connectivity = "Webservices";
		 		break;
		case 3 : connectivity = "SFTP";
		 		break;
		case 4 : connectivity = "File";
 				break; 		
		}
		return connectivity;
	}
	public String findTheConversionOption() {
		
		String selectedConversion= "";
		FileOperations fileObj = new FileOperations();
		String filename = rootFilename + "conversion.txt";
		String conversion [] = fileObj.readFile(filename);	
		int lengthVal = conversion.length;
		int i =0;
		//while(conversion[i] != null) {
		while(i < lengthVal) {
			String conversionOptions[] = conversion[i].split(":");
			if(conversionOptions[0].equals(selectedSendingConnectivity) && conversionOptions[1].equals(selectedReceivingConnectivity)) {
				selectedConversion = conversionOptions[2];
				return selectedConversion;
			}
			i = i +1;			
		}
		if(selectedConversion.equals("")) {
			selectedConversion = "Sorry VIA can't find a conversion component here. Please contact Support.ADF.Integration@volvo.com";
		}
				
		return selectedConversion;
	}
	
	public String DisplayTheIntegration(String sendingApplicationType, String receivingApplicationType) {
		
		String integrationArray []= new String[10];
		integrationArray[0] = sendingApplicationType;
		integrationArray[1] = selectedSendingConnectivity;
		integrationArray[2] = selectedSendingConversion;
		integrationArray[3] = selectedSendingTarsformer;
		integrationArray[4] = selectedSendingCrossRef;
        integrationArray[5] = selectedReceivingCrossRef;
        integrationArray[6] = selectedReceivingTarsformer;
        integrationArray[7] = selectedReceivingConversion;
        integrationArray[8] = selectedReceivingConnectivity;
        integrationArray[9] = receivingApplicationType;
		
		System.out.println("-------------------------------------------------------\n");
		String  integrationString = "";
		for(int i=1; i < 10; i++) {			
			if(!(integrationArray[i].equals("") || integrationArray[i].equals(" ") )) {		
				integrationString= integrationString + "-->" + integrationArray[i];
			}
		}	
		  E2EintegrationString = sendingApplicationType  + integrationString;
		  System.out.println(E2EintegrationString);
          System.out.println("-------------------------------------------------------\n");
          return E2EintegrationString;
		}
		
		
	
	public String findTheTransformer(int []  maxValue, HashMap<String,String> integrationOptions,String [] nfrChoice, String componentchoice) {
		
		String readerString = "Is the sending format same as the recieveing format Type 1 if Yes Type 0 if No";
		System.out.println(readerString);
		Scanner reader = new Scanner(System.in);
		String choice = reader.next();
		int mediationChoice =ValidateUserInputs(choice,readerString,1);
		String selectedTransformer = "";
		 if (mediationChoice == 0) {
			selectedTransformer = chooseNFRSBasedIntegrationObjects( maxValue,integrationOptions,nfrChoice,"transformation");
		 }
		 if (mediationChoice ==1) {
			 readerString = "Is there a need for Routing. Type 1 if Yes Type 0 if No";
			 System.out.println(readerString); 
			 choice = reader.next();
			 mediationChoice =ValidateUserInputs(choice,readerString,1);
			 if (mediationChoice == 1) {
				 if(selectedSendingConnectivity.equals("MQ") || selectedSendingConnectivity.equals("File") || selectedSendingConnectivity.equals("VCOM")) {
					 selectedTransformer = "GimR";
				 }else {
					 selectedTransformer = "IIB";
				 }
			 }
		 }
		 //reader.close(); 
		return selectedTransformer;
	}
	public int  displayApplicationQuestions( String filename) {
		FileOperations fileObj = new FileOperations();
					
		String appQuestions [] = fileObj.readFile(filename);
		int i =0;
		 while(appQuestions[i] != null) {
			 System.out.println(appQuestions[i]);
			 i=i+1;
		 }		
		Scanner reader = new Scanner(System.in);
		String appChoice = reader.next();
		int choice=ValidateUserInputs(appChoice, "Application", i);
		return choice;
	}
	public String findApplicationType(Integer choice, String filename) {
		String application = "";
		String choiceOption = "Type " + choice.toString();
		
		FileOperations fileObj = new FileOperations();
		
		String appQuestions [] = fileObj.readFile(filename);
		int i =0;
		 while(appQuestions[i] != null) {
			 if(appQuestions[i].contains(choiceOption)) {
				 String applicationChoice[]  = (appQuestions[i]).split("-");
				 application = applicationChoice[1];
				 return application;
			 }
			 i=i+1;
		 }	
			
		return application;
	}
	public String [] chooseIntegrationComponents(String application) {
		
		FileOperations fileObj = new FileOperations();
		String[] connectivityOptions = null;
		connectivityOptions = fileObj.readFile(rootFilename  + "Application" + "/" + application + ".txt");
		return connectivityOptions;
		
	}
	public String chooseNFRSBasedIntegrationObjects(int []  maxValue, HashMap<String,String> integrationOptions,String [] nfrChoice, String componentchoice ) {
		
		String choosenComponent = "";
		FileOperations fileObj = new FileOperations();
		String componentOptions [] = integrationOptions.get(componentchoice).split(",");
		int i =0;
		int j =0;
		HashMap<String,Integer> connectivityComparison = new HashMap<String,Integer>();
		int nfrCounter = 0;
		while(componentOptions.length > i) {
			String filename = rootFilename  + componentchoice + "/" +  componentOptions[i]  +".txt";
			
			
			String nfrsValue [] = fileObj.readFile(filename);
			 j =0;
			
			 while(nfrsValue [j] != null) {
				 String nfrs[] = nfrsValue[j].split(":");
				 if (nfrChoice[j].contains(nfrs[0])){
					 connectivityComparison.put(componentOptions[i]+nfrs[0], 0);
					 int choice = Integer.parseInt(nfrs[2]);
					 if (choice == 1) {
						 if(maxValue [j] <= Integer.parseInt(nfrs[1]) ) {
								//selectedConnectivity = connectivityOptions[i]; 
								connectivityComparison.put(componentOptions[i]+nfrs[0], 1);
								nfrCounter = nfrCounter + 1;								
							 }
					 }
						 
					 if (choice == 2) {
						 if(maxValue [j] >= Integer.parseInt(nfrs[1])  ) {
								//selectedConnectivity = connectivityOptions[i]; 
								connectivityComparison.put(componentOptions[i]+nfrs[0], 1);
								nfrCounter = nfrCounter + 1;
								}
					 }
					 if (choice == 0) {
						 /**if(maxValue [j] == Integer.parseInt(nfrs[1]) || maxValue [j] == 0) {
								//selectedConnectivity = connectivityOptions[i]; 
								connectivityComparison.put(componentOptions[i]+nfrs[0], 1);
								nfrCounter = nfrCounter + 1;								
							}**/
						 if(maxValue [j] == 0) {
							 connectivityComparison.put(componentOptions[i]+nfrs[0], 1);
								nfrCounter = nfrCounter + 1; 
						 }else {
							 if(maxValue [j] == Integer.parseInt(nfrs[1])){
								 int value = nfrs.length;
								 if(value > 4) {
									 if(Integer.parseInt(nfrs[3]) >= maxValue[0])  {
									 connectivityComparison.put(componentOptions[i]+nfrs[0], 1);
										nfrCounter = nfrCounter + 1; 
									 }
								 }else {
									 connectivityComparison.put(componentOptions[i]+nfrs[0], 1);
									 nfrCounter = nfrCounter + 1;
								 }
							 }
						 }
					 }		
					  
				 } 				 
				 j=j+1; 
				
			 }
			 if (nfrCounter > 0) {
					if (nfrCounter == j) {
						choosenComponent = componentOptions[i]; 
						return choosenComponent;
					}
				}
				nfrCounter = 0;
			i =i+1;
		}
		
		if (choosenComponent.equals("")) {
			choosenComponent = "No standard Integration component available for the chossen NFR contact Support.ADF.Integration@volvo.com";
		}
		return choosenComponent;
	}

	public HashMap<String,String> loadDataToArray(String[] oneDimensionalArray) {
		
		HashMap<String,String> hm=new HashMap<String,String>(); 
		int i =0;
		 while(oneDimensionalArray[i] != null) {
			 
			 String nfrsValue [] = oneDimensionalArray[i].split(":");
			 hm.put(nfrsValue [0], nfrsValue [1]);
			 i=i+1;
		 }
		
		return hm;
		
	}
}
