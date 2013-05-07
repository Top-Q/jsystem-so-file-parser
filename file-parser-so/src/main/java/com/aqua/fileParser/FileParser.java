package com.aqua.fileParser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jsystem.extensions.analyzers.text.FindText;
import jsystem.framework.system.SystemObjectImpl;

/**
 * this system object can parse text files
 * 
 * @author Yuval Ohayon
 * 
 */
public class FileParser extends SystemObjectImpl {

	/**
	 * log file name (with full path)
	 */
	private String logFile;

	/**
	 * contains the new data that added to the parsed file
	 */
	private String[] buff = null;

	private BufferedReader br = null;

	/**
	 * data of each column (regullar expression, column name and ect.)
	 */
	private ArrayList<FPColumns> colRegArr = new ArrayList<FPColumns>();

	public String getLogFile() {
		return logFile;
	}

	public void setLogFile(String logFile) {
		this.logFile = logFile;
	}

	public void setColRegArr(ArrayList<FPColumns> colRegExpArr) {
		this.colRegArr = colRegExpArr;
	}

	/**
	 * close the log file (the log file is opened as long the system object is
	 * alive)
	 */
	public void close() {
		super.close();
		try {
			report.report("Closing file");
			br.close();

		} catch (Exception e) {
			report.step("Error : can't close log file");
		} // of catch
	} // of close

	/**
	 * read the new data from the text file and put it on the buffer the older
	 * data will be removed from the buffer
	 * 
	 * @throws IOException
	 */
	public void readFileToBuffer() throws IOException {
		if (br == null)
			br = new BufferedReader(new FileReader(getLogFile()));

		StringBuffer sb = new StringBuffer();
		String line = null;

		while ((line = br.readLine()) != null) {
			sb.append(line).append('\n');
		}

		buff = sb.toString().split("\n");
		setTestAgainstObject(sb.toString());
	} // of enterDataToBuff

	/**
	 * this function runs on all the buffer and it prints the fields
	 * 
	 * @throws IOException
	 */
	public void printColWithCounter() throws IOException {

		if ((buff == null) || ((buff[0].equals("") && (buff.length == 1))))
			return;

		for (int i = 0; i < buff.length; i++) {

//			String match = null;
			for (int j = 0; j < colRegArr.size(); j++) {
				// read columnn in regullar expression
				// Pattern pattern =
				// Pattern.compile(colRegArr.get(j).getRegExp());
				// Matcher matcher = pattern.matcher(buff[i]);
				// boolean matchFound = matcher.find();
				// if (colRegArr.get(j).getGroup() == -1)
				// match = matcher.group();
				// else
				// match = matcher.group(colRegArr.get(j).getGroup());
				//
				// report.report("Column " + j + " "
				// + colRegArr.get(j).getColName() + " match " + match);
				// // matchFound = matcher.find();

				// read columns with analyzer

				FindText ft = null;
				analyze(ft = new FindText(colRegArr.get(j).getRegExp(), true,
						false));
				report.report(ft.getCounter());
			} // of for
		}
	} // of printColWithCounter

	public void printColWithReg() throws IOException {

		if ((buff == null) || ((buff[0].equals("") && (buff.length == 1))))
			return;

		for (int i = 0; i < buff.length; i++) {

			String match = null;
			for (int j = 0; j < colRegArr.size(); j++) {
				// read columnn in regullar expression
				Pattern pattern = Pattern.compile(colRegArr.get(j).getRegExp());
				Matcher matcher = pattern.matcher(buff[i]);
//				boolean matchFound = matcher.find();
				if (colRegArr.get(j).getGroup() == -1)
					match = matcher.group();
				else
					match = matcher.group(colRegArr.get(j).getGroup());

				report.report("Column " + j + " "
						+ colRegArr.get(j).getColName() + " match " + match);
				// matchFound = matcher.find();

				// read columns with analyzer

				// FindText ft = null;
				// analyze(ft = new FindText(colRegArr.get(j).getRegExp(), true,
				// false));
				// report.report(ft.getCounter());
			} // of for
		}
	} // of printColWithReg

	/**
	 * delete all the content of the syslog log file
	 *
	 */
	public void fetch() {
		try {
			BufferedWriter out = new BufferedWriter(
					new FileWriter(getLogFile()));
			out.write("");
			out.close();
		} catch (IOException e) {
			System.err.println(e.getStackTrace());
		}
	}

} // of syslog class
