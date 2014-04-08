package uk.bris.esserver.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class ESSDateUtil {
	
	public static SimpleDateFormat formatter;
	public static SimpleDateFormat parser;
	public static SimpleDateFormat testFormatter;
	
	static{
		formatter = new SimpleDateFormat("EE MMM dd yyyy HH:mm z");
		formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
		
		parser = new SimpleDateFormat("MM/dd/yyyy HH:mm");
		parser.setTimeZone(TimeZone.getTimeZone("GMT"));
		
		testFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

	}
	
	public static String formatTimeStamp(Timestamp ts){
		return formatter.format(ts.getDate());
	}
	
	public static String formatDate(Timestamp ts){
		return parser.format(ts);
	}
	
	public static Date parseDateString(String date){
		try {
			return parser.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return  null;
	}
	
	public static String getUseableDate(Date date){
		return parser.format(date);
	}
}
