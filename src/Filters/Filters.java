package Filters;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Database.Record;

public class Filters{
	public double min_lon,max_lon,min_lan,max_lan,min_alt,max_alt;
	public Date min_date,max_date;
	public Time min_time,max_time;
	public String device_info = "";
	
	public char logic_gate = '&';
	
	public Filters() {

	}
	
	/**
	 * Check if the record is pass the filter or not.
	 * @param record
	 * @return
	 */
	public boolean checkRecord(Record record) { 
		boolean isPosition = isPositionIsBetween(record.lon, record.lan, record.alt);
		Date date = null;
		Time time = null;
		String[] time_date_arr = record.time.split(" ");
		try {
			date = Date.parseToDate(time_date_arr[0],"-",true);
			time = Time.parseToTime(time_date_arr[1]);
		} catch(Exception ex) {
			return false;
		}
		
		boolean isTimeAndDate = isTimeDateBetween(date, time);
		if(isPosition && isTimeAndDate && record.device_info.contains(device_info))
			return true;
		return false;
	}
	
	
	public void readFiltersValues(String min_lon,String max_lon,String min_lan,String max_lan,String min_alt,String max_alt, 
			String min_date, String max_date, String min_time, String max_time,String device_info) {
			
		this.device_info = device_info;
		try {
			this.min_lon = Double.parseDouble(min_lon);
			this.max_lon = Double.parseDouble(min_lon);
		} catch (Exception ex) {
			this.min_lon = Double.MIN_VALUE;
			this.max_lon = Double.MAX_VALUE;
		}
		
		try {
			this.min_lan = Double.parseDouble(min_lan);
			this.max_lan = Double.parseDouble(max_lan);
		} catch (Exception ex) {
			this.min_lan = Double.MIN_VALUE;
			this.max_lan = Double.MAX_VALUE;
		}
		
		try {
			this.min_alt = Double.parseDouble(min_alt);
			this.max_alt = Double.parseDouble(max_alt);
		} catch (Exception ex) {
			this.min_alt = Double.MIN_VALUE;
			this.max_alt = Double.MAX_VALUE;
		}
		
		try {
			this.min_date = Date.parseToDate(min_date,"/",false);
			this.max_date = Date.parseToDate(max_date,"/",false);
		} catch (Exception ex) {
			this.min_date = Date.MIN_VALUE();
			this.max_date = Date.MAX_VALUE();
		}
		
		try {
			this.min_time = Time.parseToTime(min_time);
			this.max_time = Time.parseToTime(max_time);
		} catch (Exception ex) {
			
			
			this.min_time = Time.MIN_VALUE();
			this.max_time = Time.MAX_VALUE();
		}
	}
	
	private boolean isTimeBetween(Time curr_time) {
		int total_seconds_min = min_time.hours*3600 + min_time.minutes*60 + min_time.seconds;
		int total_seconds_max = max_time.hours*3600 + max_time.minutes*60 + max_time.seconds;
		int total_seconds_curr = curr_time.hours*3600 + curr_time.minutes*60 + curr_time.seconds; 
		if(total_seconds_max >= total_seconds_curr &&  total_seconds_min <= total_seconds_curr )
			return true;
		return false;
	}
	
	private boolean isDateBetween(Date curr_date) {
		
		LocalDate time = LocalDate.of(curr_date.year, curr_date.month, curr_date.day);
		LocalDate timeMin = LocalDate.of(min_date.year, min_date.month, min_date.day);
		LocalDate timeMax = LocalDate.of(max_date.year, max_date.month, max_date.day);
		if(time.isAfter(timeMin) && time.isBefore(timeMax)) {
			return true;
		}
		
		return false;
	}
	
	public boolean isTimeDateBetween(Date curr_date, Time curr_time) {
		if(isDateBetween(curr_date) && isTimeBetween(curr_time))
			return true;
		return false;
	}
	
	
	public boolean isPositionIsBetween(double lon,double lan,double alt) {
		if(lan <= max_lan && lan >= min_lan && lon>= min_lon && lon <= max_lon && alt >= min_alt && alt <= max_alt)
			return true;
		return false;
	}
	
	public boolean isDeviceInformation(String info) {
		if(info.contains(device_info))
			return true;
		return false;
	}
	
	public boolean import_from_string(String str) {
		try {
			char logic_gate = str.charAt(0);
			if(logic_gate != '|' && logic_gate != '&' && logic_gate != '!') {
				throw new Exception("filter not have a correct logic gate");
			}
			this.logic_gate = logic_gate;
			//String s = "p\\(([0-9,.;])+\\)t\\(([0-9;:])+\\)d\\(([0-9;\\/])+\\)";
			Pattern pattern = Pattern.compile("p\\((.*)\\)t\\((.*)\\)d\\((.*)\\)");
			Matcher matcher = pattern.matcher(str);
			if (matcher.find())
			{
				String str_group_1 = matcher.group(1);
			    String[] pArr = str_group_1.split(";");
			    String pMin = pArr[0];
			    String pMax = pArr[1];
			    
			    String[] pMinArr = pMin.split(",");
			    String pMinLon = pMinArr[0];
	    		String pMinLan =pMinArr[1];
				String pMinAlt =pMinArr[2];
				
				String[] pMaxArr = pMax.split(",");
			    String pMaxLon = pMaxArr[0];
	    		String pMaxLan =pMaxArr[1];
				String pMaxAlt =pMaxArr[2];
			    
			    String[] tArr = matcher.group(2).split(";");
			    String tMin = tArr[0];
			    String tMax = tArr[1];
			    
			    String[] dArr = matcher.group(3).split(";");
			    String dMin = dArr[0];
			    String dMax = dArr[1];
			    
			    min_lon = Double.parseDouble(pMinLon);
			    min_lan = Double.parseDouble(pMinLan);
			    min_alt = Double.parseDouble(pMinAlt);
			    
			    max_lon = Double.parseDouble(pMaxLon);
			    max_lan = Double.parseDouble(pMaxLan);
			    max_alt = Double.parseDouble(pMaxAlt);
			    
			    min_time = Time.parseToTime(tMin);
			    max_time = Time.parseToTime(tMax);
			    
			    min_date = Date.parseToDate(dMin,"/",false);
			    max_date = Date.parseToDate(dMax,"/",false);
			    
			    
			} else {
				throw new Exception("filter is not in the correct format");
			}
		} catch(Exception ex) {
			return false;
		}
		
		return true;
	}
	
	@Override
	public String toString() { // [p(min_lon,min_lon,min_alt;max_lon,max_lan,max_alt)t(10:10:10;12:12:12)d(1/1/2017;2/2/2018)]
		String str = "[";
		
		str += "p(" + min_lon + "," + min_lan + "," + min_alt + ";";
		str += max_lon + "," + max_lan + "," + max_alt + ")";
		
		str += "t(" + min_time + ";" + max_time +")";
		
		str += "d(" + min_date + ";" + max_date +")";
		
		str += "]";
		return str;
	}
}
