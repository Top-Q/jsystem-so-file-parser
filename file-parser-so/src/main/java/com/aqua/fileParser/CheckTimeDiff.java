package com.aqua.fileParser;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import jsystem.framework.analyzer.AnalyzerParameterImpl;

/**
 * this analyzer get 2 time variables (as Date objects or as long int object)
 * and it checks if the delta between them is smaller than specific delta (that
 * the user gave)
 * 
 * @author Yuval Ohayon
 * 
 */
public class CheckTimeDiff extends AnalyzerParameterImpl {

	private long delta = -1;

	private long ltime2 = 0;

	private long ltime1 = 0;

	/**
	 * 
	 * @param date1
	 * @param date2
	 * @param delta in milliseconds
	 */
	public CheckTimeDiff(Date date1, Date date2, long delta) {
		this.delta = delta;

		// israel , jeruslaem
		TimeZone tz = TimeZone.getTimeZone("GMT+02:00");

		Calendar cal1 = Calendar.getInstance(tz);
		Calendar cal2 = Calendar.getInstance(tz);

		// different date might have different offset
		cal1.setTime(date1);
		ltime1 = date1.getTime() + cal1.get(Calendar.ZONE_OFFSET)
				+ cal1.get(Calendar.DST_OFFSET);
		//
		cal2.setTime(date2);
		cal2.set(Calendar.YEAR, 2006);
		ltime2 = date2.getTime() + cal2.get(Calendar.ZONE_OFFSET)
				+ cal2.get(Calendar.DST_OFFSET);
	}

	/**
	 * 
	 * @param time1
	 * @param time2 
	 * @param delta in milliseconds
	 */
	public CheckTimeDiff(long time1, long time2, long delta) {
		this.ltime1 = time1;
		this.ltime2 = time2;
		this.delta = delta;
	}

	public void analyze() {
		title = "Check time differences analyzer";
		status = false;

		long diff = (Math.abs(ltime1 - ltime2));

		message = "difference in milliseconds between the two Date objects is : "
				+ diff;
		if (diff > delta) {
			status = false;
			message += " insteed of " + delta;
		} else {
			status = true;
			message += " , it's in the range of " + delta;
		} // of if

		// take care of title variable
	} // of analyze

} // of analyzer class
