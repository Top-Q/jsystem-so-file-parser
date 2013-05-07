package com.aqua.fileParser;

import java.util.ArrayList;

import junit.framework.SystemTestCase;

public class FileParserTest extends SystemTestCase {

	FileParser fp;

	public void setUp() throws Exception {
		fp = (FileParser) system.getSystemObject("FP");
		fp.readFileToBuffer();

		// creating columns
		ArrayList<FPColumns> colArr = new ArrayList<FPColumns>();
		colArr.add(new FPColumns("Kiwi Date", "(\\d+-){2}\\d+"));
		colArr.add(new FPColumns("Kiwi Time", "(\\d{2}:){2}\\d{2}"));
		colArr.add(new FPColumns("Kiwi Priority", "Local\\d\\.[^\\d\\s]+"));
		colArr.add(new FPColumns("Kiwi Priority", "Local\\d\\.\\w+"));
		colArr.add(new FPColumns("IP", "(\\d{1,3}\\.){3}\\d{1,3}"));
		colArr.add(new FPColumns("Message",
				"[a-zA-Z]{3} +\\d{1,2} +(\\d{2}:){2}\\d{2}.*"));

		fp.setColRegArr(colArr);
	}

	public void testT() throws Exception {
		fp.analyze(new GetFindTextLines(10000, "Mooose MetroStar Remote device disconnected from slot 1, channel 1:"));
	}
}

// tested
// PSU installed in slot 33\n
// Chassis temperature back to normal/n
// Remote device over-heat on slot 444, channel %d\0
// Local Main TP link down on slot %d: %s\
