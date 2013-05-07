package com.aqua.fileParser;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jsystem.extensions.analyzers.text.AnalyzeTextParameter;

/**
 * 
 * this class can parse the syslog messages that fibrolan machines creates
 * 
 * note :
 * 
 * your operating system language and settings should be set to US english
 * otherwise the date parsing may not work.
 * @author Yuval Ohayon
 * 
 */

// @ TODO change the name of the class
public class GetFindTextLines extends AnalyzeTextParameter {

	private static final String GMT = "GMT+02:00";

	private static final String DATE_FORMAT = "MMM dd HH:mm:ss";

	private final static String MSG_REG = "[a-zA-Z]{3} +\\d{1,2} +(\\d{2}:){2}\\d{2}.*";

	private String param = MSG_REG;

	private String counter = null;

	/**
	 * the delta time between the current time and the message time that was
	 * read from the log file
	 */
	private long timeDelta = -1;

	/**
	 * the expected message that we search
	 */
	private String expectedMsg = null;
	
	
	private Date dateToCompareWith = null;

	
	public GetFindTextLines(long timeDelta, String expectedMsg) {
		this(timeDelta,expectedMsg,new Date());
	} // of constructor
	

	public GetFindTextLines(long timeDelta, String expectedMsg,Date date) {
		// super(param);
		super(MSG_REG);
		this.timeDelta = timeDelta;
		// this.param = param;
		this.expectedMsg = expectedMsg;
		this.dateToCompareWith = date;
	} // of constructor

	/**
	 * parse the time (as calander object) from the syslog message string
	 * 
	 * @param msg
	 *            the syslog message
	 * @return calendar object with the appropriate fime
	 * @throws Exception
	 */
	private Calendar getTimeFromMessage(String msg) throws Exception {

		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		Date date = (Date)sdf.parse(msg);
		TimeZone tz = TimeZone.getTimeZone(GMT);
		Calendar cal = Calendar.getInstance(tz);
		cal.setTime(date);
		cal.set(Calendar.YEAR, Calendar.getInstance(tz).get(Calendar.YEAR));
		return cal;
	} // of getTimeFromMessage

	/**
	 * parse the message from the syslog message and removes the time data (date
	 * and hour)
	 * 
	 * @param totalMsg
	 * @return only the text message
	 */
	private String getMessagefromTotalMsg(String totalMsg) {
		return totalMsg.replaceAll(
				"[a-zA-Z]{3} +\\d{1,2} +(\\d{2}:){2}\\d{2} *", "");
	} // of getMessagefromTotalMsg

	public void analyze() {
		if (testText == null) {
			title = "Text to analyze is null";
			status = false;
		}
		// message = testText;
		message = "";
		// String found = toFind;

		String[] buff = testText.split("\n"); // testText = testAgaistObject

		title = "No appropriate message was found";
		try {
			for (int i = 0; i < buff.length; i++) {
				Pattern pattern = Pattern.compile(param);
				Matcher matcher = pattern.matcher(buff[i]);
				boolean matchFound = matcher.find();
				if (matchFound == false)
					continue;
				String match = matcher.group();

				CheckTimeDiff ctf = new CheckTimeDiff(dateToCompareWith,
						getTimeFromMessage(match.substring(0,15).trim()).getTime(), timeDelta);
				ctf.analyze();

				if ((ctf.getStatus())
						&& (getMessagefromTotalMsg(match).equals(expectedMsg))) {
					// Message was found, with appropriate time";
					status = true;
					message += "Time : " + getTimeFromMessage(match).getTime()
							+ " " + getMessagefromTotalMsg(match) + "<br>";
					title = "Expected message is equal to actual message <"+match+">";
				}

			} // of for
			counter = message;
		} catch (Exception e) {
			title = "Exception : " + e.getLocalizedMessage();
			System.out.println(e);
		}
	} // of analyze /**

	public String getCounter() {
		return counter;
	}

	public String getMessage() {
		return message;
	}
} // of class

// * returns the value of the parameter that we search. for example if we
// had
// * this text in our testAgainstObject : Local Main TP link down on slot 2:
// * Test . and we searched the word slot , this counter will return "2"
// */
// public String getCounter() {
// return super.getCounter().trim();
// }
// // String tmp = super.getCounter();
// //
// // tmp = tmp.replaceAll("\\p{Blank}" + param + "\\p{Blank}", "");
// // tmp = tmp.replaceAll("[\\p{Space}\\p{Punct}]", "");
// // tmp = tmp.trim();
//
// return super.getCounter().replaceAll(
// "\\p{Blank}" + param + "\\p{Blank}", "").replaceAll(
// "[\\p{Space}\\p{Punct}]", "").trim();
// } // of getCounter

