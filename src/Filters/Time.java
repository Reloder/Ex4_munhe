package Filters;

public class Time {

	public int hours,minutes,seconds;
	
	public Time() {
		
	}
	
	public Time(int hours, int minutes, int seconds) {
		this.hours = hours;
		this.minutes = minutes;
		this.seconds = seconds;
	}
	
	public static Time MIN_VALUE() {
		return new Time(0,0,0);
	}
	
	public static Time MAX_VALUE() {
		return new Time(23,59,59);
	}
	
	public static Time parseToTime(String time) { // in format 10:10:56
		try {
			String[] arr = time.split(":");
			Time ret_time = new Time();
			ret_time.hours = Integer.parseInt(arr[0]);
			ret_time.minutes = Integer.parseInt(arr[1]);
			ret_time.seconds = Integer.parseInt(arr[2]);
			return ret_time;
		}catch(Exception ex) {
			return new Time(0,0,0);
		}
	}
	
	@Override
	public String toString() {
		return hours + ":" + minutes + ":" + seconds;
	}
}
