package Filters;

public class Date {

	public int year,month,day;
	
	public Date() {
		
	}
	
	public Date(int day,int month,int year) {
			this.day = day;
			this.month = month;
			this.year = year;
	}
	
	public static Date MAX_VALUE() {
		return new Date(1,1,3000);
	}
	
	public static Date MIN_VALUE() {
		return new Date(1,1,1970);
	}
	
	public static Date parseToDate(String date, String splitter,boolean isYearOnLeft) { // in format 10/10/2018
		try {
			String[] arr = date.split(splitter);
			Date ret_date = new Date();
			if(isYearOnLeft) {
				ret_date.day = Integer.parseInt(arr[2]);
				ret_date.month = Integer.parseInt(arr[1]);
				ret_date.year = Integer.parseInt(arr[0]);
			} else {
				ret_date.day = Integer.parseInt(arr[0]);
				ret_date.month = Integer.parseInt(arr[1]);
				ret_date.year = Integer.parseInt(arr[2]);
			}

			return ret_date;
		}catch(Exception ex) {
			return new Date(1,1,1970);
		}
	}
	
	@Override
	public String toString() {
		return day + "/" + month + "/" + year;
	}
}
