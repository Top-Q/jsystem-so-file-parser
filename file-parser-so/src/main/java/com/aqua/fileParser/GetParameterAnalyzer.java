package com.aqua.fileParser;

import jsystem.extensions.analyzers.text.FindText;

/**
 * this analyzer designed to find specific parameter is string and to return
 * it's value for example of we have this string : 
 * Local Main TP link down on slot 2: Test .
 * and we want to find the parameter string , we should get "2"
 * 
 * @author Yuval Ohayon
 * 
 */
public class GetParameterAnalyzer extends FindText {
	private boolean isFound = false;

	private String param = null;

	public GetParameterAnalyzer(String param) {
		super("\\p{Blank}" + param
				+ "\\p{Blank}\\p{Digit}+[\\p{Space}\\p{Punct}]", true, false);
		this.param = param;
	} // of constructor

	public void analyze() {
		super.analyze();
		isFound = status;
		if (isFound) {
			status = true;
			title = "The text:<" + param + " [Number]> was found";
		} else {
			status = false;
			title = "The text: <" + param + " [Number]> wasn't found";
		}

	} // of analyze

	/**
	 * returns the value of the parameter that we search.
	 * for example if we had this text in our testAgainstObject :
	 * Local Main TP link down on slot 2: Test .
	 * and we searched the word slot , this counter will return "2"
	 */
	public String getCounter() {
		// String tmp = super.getCounter();
		//
		// tmp = tmp.replaceAll("\\p{Blank}" + param + "\\p{Blank}", "");
		// tmp = tmp.replaceAll("[\\p{Space}\\p{Punct}]", "");
		// tmp = tmp.trim();

		return super.getCounter().replaceAll(
				"\\p{Blank}" + param + "\\p{Blank}", "").replaceAll(
				"[\\p{Space}\\p{Punct}]", "").trim();
	} // of getCounter

} // of class
