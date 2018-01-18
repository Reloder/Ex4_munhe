package Tools;

import java.text.DecimalFormat;
import java.util.ArrayList;

import Algorithms.WeightRecord;
import Database.Record;

public class Tools {
	
	
	/**
	 * Reverse a list from end to start, and store no more than max_elements elements.
	 * @param list
	 * @param max_elements
	 * @return
	 */
	public static ArrayList<WeightRecord> reverseArrayList(ArrayList<WeightRecord> list, int max_elements) {
		ArrayList<WeightRecord> return_list = new ArrayList<>();
		int end = list.size() - 1;
		for (int i = end; i >= 0 && end - i < max_elements; i--) {
			return_list.add(list.get(i));
		}

		return return_list;
	}
	
	
	/**
	 * Get the calculated weighted location of some records with coords.
	 * @param records
	 * @return
	 */
	public static WeightRecord calculateLocation(ArrayList<WeightRecord> records) {
		double w_sum = 0;
		double lan_sum = 0;
		double lon_sum = 0;
		double alt_sum = 0;

		for (int i = 0; i < records.size(); i++) {
			WeightRecord rec = records.get(i);
			w_sum += rec.weight;
			lan_sum += rec.getLanWeighted();
			lon_sum += rec.getLonWeighted();
			alt_sum += rec.getAltWeighted();
		}
		
		double lan_result = lan_sum / w_sum;
		double lon_result = lon_sum / w_sum;
		double alt_result = alt_sum / w_sum;
		
		WeightRecord data_result = new WeightRecord(lan_result,lon_result,alt_result,-100);
		return data_result;
	}
	
	
	/**
	 * Take a double number and format it to string.
	 * @param d
	 * @param numOfDigitsAfter = how many digits represent in the string after the decimal.
	 * @return
	 */
	public static String doubleToString(double d,int numOfDigitsAfter) {
		String s = "";
		for(int i=0;i<numOfDigitsAfter;i++) {
			s+= "#";
		}
		String the_str = new DecimalFormat("##." + s).format(d);
		return the_str;
	}
	
	/**
	 * Implode a list of strings to one string with a char between the list elements.
	 * @param list = list of String elements.
	 * @return
	 */
	public static String implode(ArrayList<String> list,char splitter) {
		String the_str = "";
		int count = list.size();
		for(int i=0;i<count ;i++) {
			String s = list.get(i);
			if(i + 1 != count) {
				s += splitter;
			}
			the_str += s;
		}
		return the_str;
	}
	
	public static ArrayList<Record> mergeRecordArrayList(ArrayList<Record> list1,ArrayList<Record> list2) {
		ArrayList<Record> ret = new ArrayList<>();
		for(int i=0;i<list1.size();i++) {
			ret.add(list1.get(i));
		}
		
		for(int i=0;i<list2.size();i++) {
			ret.add(list2.get(i));
		}
		return ret;
	}
}
