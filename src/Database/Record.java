package Database;

import java.util.ArrayList;
import java.util.Arrays;

import Tools.Tools;


/**
 * This class represent a single record in our csv file. 
 */
 
public class Record {
	public ArrayList<WifiData> wifiList = new ArrayList<WifiData>(); // An ArrayList of wifi devices in the record
	public int wifi_count = 0; // how many wifi devices found

	public double lan, lon, alt; // the gps coordinates when the record was taken
	public boolean isColumnSpace; // if there is a space column between wifi in the csv
	public String device_info; // some information string about the device that take the scan
	public String time; // the time the scan was taken

	public Record() {
	}
	
	/**
	 * Constructor which take a line string from the csv and export it to this Record object.
	 * @param line = the line string
	 * @param isColumnSpace
	 */
	public Record(String line, boolean isColumnSpace) {
		this.isColumnSpace = isColumnSpace;
		fillRecordByCsvLine(line, isColumnSpace);
	}

	
	/**
	 * the code that fill the object from the csv line string.
	 * @param line = the csv line string
	 * @param isColummSpace
	 */
	private void fillRecordByCsvLine(String line, boolean isColummSpace) {
		String[] fields = line.split(",");

		if (fields.length < 6) {
			return;
		}

		this.time = fields[0];
		this.device_info = fields[1];

		try {
			this.lan = Double.parseDouble(fields[2]);
			this.lon = Double.parseDouble(fields[3]);
			this.alt = Double.parseDouble(fields[4]);
		} catch (Exception ex) {
			this.lan = 0;
			this.lon = 0;
			this.alt = 0;
		}

		this.wifi_count = Integer.parseInt(fields[5]);
		if (wifi_count < 0 || wifi_count > 10) {
			return;
		}

		int columns_per_wifi = 4;
		if (isColummSpace)
			columns_per_wifi = 5;

		for (int i = 0; i < wifi_count; i++) {
			int field_index = 6 + i * columns_per_wifi;
			if (isColummSpace)
				field_index++;
			String name = fields[field_index];
			String ssid = fields[field_index + 1];
			String channel = fields[field_index + 2];
			String signal = fields[field_index + 3];

			WifiData wifi = new WifiData(name, ssid, channel, signal);
			wifiList.add(wifi);
		}
	}
	
	public void addWifi(WifiData wifi) {
		
		wifi_count++;
		wifiList.add(wifi);
		sortWifiList();
		if(wifi_count >= 10)
			wifi_count = 10;
			

	}
	
	private void sortWifiList() { // DESC BY SIGNAL
		
		WifiData[] wifi_arr = new WifiData[wifiList.size()];
		wifi_arr=wifiList.toArray(wifi_arr);
		
		Arrays.sort(wifi_arr);
		
		wifiList.clear();
		
		for(int i = wifi_arr.length -1; i >=0 && i>=  wifi_arr.length -1 - 10; i--) // reverse the array from asc to desc: closer to 0 first..
		{
			wifiList.add(wifi_arr[i]);
		}
	}
	
	
	/**
	 * This toString function return a csv formatted string, use it to build your own csv file.
	 */
	@Override
	public String toString() {
		ArrayList<String> params = new ArrayList<>();
		params.add(time);
		params.add(device_info);
		params.add(Tools.doubleToString(lan, 5));
		params.add(Tools.doubleToString(lon, 5));
		params.add(Tools.doubleToString(alt, 5));
		params.add(Integer.toString(wifi_count));
		for (int i = 0; i < wifi_count; i++) {
			WifiData wifi = wifiList.get(i);
			if(isColumnSpace) {
				params.add("");
			}
			params.add(wifi.Name);
			params.add(wifi.MAC);
			params.add(wifi.Channel);
			params.add(Tools.doubleToString(wifi.Signal,1));
		}
		
		String str = Tools.implode(params,',');
		return str;
	}
	
	public boolean isWifiExist(WifiData wifi) {
		for(int i=0;i<wifiList.size();i++) {
			WifiData curr = wifiList.get(i);
			if(curr.isEquals(wifi))
				return true;
		}
		return false;
	}
	
	public boolean isEquals(Record record) {
		if(record.lan == lan && record.alt == alt && record.lon == lon && record.device_info.equals(device_info) && record.time.equals(time) && record.wifiList.size() == wifiList.size()) {
			for(int i=0;i<record.wifiList.size();i++) {
				WifiData curr = record.wifiList.get(i);
				if(isWifiExist(curr) == false)
					return false;
			}
			return true;
		}
		return false;
	}

}
