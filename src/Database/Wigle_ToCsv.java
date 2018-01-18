package Database;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class Wigle_ToCsv {
	
	public ArrayList<Record> records = new ArrayList<>(); // The Database list
	int row_num = 1;
	public String folder_path = "";
	/**
	 * This constructor initialize and create the csv by taking all the wigle csvs files in the folder path
	 * and sort them by their signal and store them in a single csv file.
	 * (which afterwords will become a kml file)
	 * @param folder_path
	 */
	public Wigle_ToCsv(String folder_path) {
		this.folder_path = folder_path;
		File folder = new File(folder_path);
		File[] listOfFiles = folder.listFiles();

	    for (int i = 0; i < listOfFiles.length; i++) {
	    	File file = listOfFiles[i];
		    String file_name = file.getName();
		    String ext = file_name.substring(file_name.length() - 3);
		    if (file.isFile() && ext.equals("csv")) {
		    	try {
		    		BufferedReader  br = new BufferedReader(new FileReader(file.getAbsolutePath()));
		    		ArrayList<Record> new_records = get_records_from_csv(br);
		    		addRecords(new_records);
		    	} catch(Exception ex) {
		    		
		    	}
		    	
		    }
	    }
	}
	
	public void addRecords(ArrayList<Record> list) {
		for(int i=0;i<list.size();i++) {
			records.add(list.get(i));
		}
	}
	
	
	/**
	 * taking a single csv wigle file and add his data as a record to our csv .
	 * @param br = the file BufferedReader object for reading the file.
	 * @throws NumberFormatException
	 * @throws IOException
	 */
	private ArrayList<Record> get_records_from_csv(BufferedReader  br) throws NumberFormatException, IOException {
		ArrayList<Record> records = new ArrayList<>();
		
		ArrayList<Wigle> wigles = new ArrayList<Wigle>();
		String line;
		int line_num = 1;
		boolean isWigleCsv = false;
		String device_info = "";
		while ((line = br.readLine()) != null) {
			if(line_num++ < 3) { // first 2 lines are not included as a network.
				if(isWigleCsv == false && line.startsWith("WigleWifi")) { // check if the csv file is in the correct format
					isWigleCsv = true;
					device_info = line.split(",")[2];
				}
				continue;
			}
			String[] row_info = line.split(",");
			Wigle wigle = new Wigle();
			
			wigle.MAC = row_info[0];
			wigle.SSID = row_info[1];
			wigle.AuthMode = row_info[2];
			wigle.FirstSeen = row_info[3];
			wigle.Channel = row_info[4];
			String rssi = row_info[5];
			wigle.RSSI = Integer.parseInt(rssi) ;
			wigle.Latitude = row_info[6];
			wigle.Longitude = row_info[7];
			wigle.Altitude = row_info[8];
			wigle.Accuracy = row_info[9];
			wigle.Type = row_info[10];
			wigles.add(wigle);
		}
		
		if(isWigleCsv) { // is in wigle-csv format
			Wigle[] wigles_arr = new Wigle[wigles.size()];
			wigles_arr = wigles.toArray(wigles_arr);
			
			int count_networks = wigles_arr.length;
			if(count_networks >= 1) { // if found at least one network
				Record record=new Record();
				Wigle prev_wigle = new Wigle();
				
				int i=0;
				for(i=0;i<count_networks;i++) {
					
					Wigle curr_wigle = wigles_arr[i];
					
					WifiData wifi = new WifiData();
					wifi.Name = curr_wigle.SSID;
					wifi.MAC = curr_wigle.MAC;
					wifi.Channel = curr_wigle.Channel;
					wifi.Signal = curr_wigle.RSSI;
					
					if(is_match_time_and_location(prev_wigle,curr_wigle) == false) {
						try {
							record.lan = Double.parseDouble(prev_wigle.Latitude);
							record.lon = Double.parseDouble(prev_wigle.Longitude);
							record.alt = Double.parseDouble(prev_wigle.Altitude);
							record.time = prev_wigle.FirstSeen;
							record.device_info = device_info;
							records.add(record);
							record = new Record();
						} catch(Exception ex) {
							
						}
						
						
					} 
					record.addWifi(wifi);
					
					prev_wigle = curr_wigle;
				}
				try {
					record.lan = Double.parseDouble(prev_wigle.Latitude);
					record.lon = Double.parseDouble(prev_wigle.Longitude);
					record.alt = Double.parseDouble(prev_wigle.Altitude);
					record.time = prev_wigle.FirstSeen;
					record.device_info = device_info;
					records.add(record);
				} catch(Exception ex) {
					
				}

			}

			
		}
		return records;

	}
	

	
	public boolean is_match_time_and_location(Wigle wigle1, Wigle wigle2) {

		long difference = 0;
		try {
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			Date date1 = (Date)formatter.parse(wigle1.FirstSeen);
			Date date2 = (Date)formatter.parse(wigle2.FirstSeen);
		    difference = date1.getTime() - date2.getTime();
		} catch(Exception ex) {
			return false;
		}
	    
		
		if(wigle1.Latitude.equals(wigle2.Latitude) && wigle1.Longitude.equals(wigle2.Longitude) && wigle1.Altitude.equals(wigle2.Altitude)
				&& Math.abs(difference) == 0 ) {
			return true;
		}
		return false;
	}
	
	public void printCsvTxt() {
		PrintWriter pw = null;
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd_HH.mm.ss");
		Date date = new Date();
		String date_txt = dateFormat.format(date); //2016/11/16 12:08:43
		String file_name = folder_path + "\\NewData_" + date_txt + ".csv";
		try {
		    pw = new PrintWriter(new File(file_name));
		} catch (FileNotFoundException e) {
		    e.printStackTrace();
		}
		
		String csv_build = getCsvTxt();
		
		pw.write(csv_build);
        pw.close();
	}
	
	public String getCsvTxt() {
		String csv_build = "";
		for(int i=0;i<records.size();i++) {
			Record record = records.get(i);
			csv_build +=  record.time + "," + record.device_info + "," + record.lan + "," + record.lon + "," + record.alt + "," + record.wifi_count;
			for(int wifi_index = 0;wifi_index<record.wifi_count;wifi_index++) {
				WifiData wifi = record.wifiList.get(wifi_index);
				csv_build += "," + wifi.Name + "," +  wifi.MAC + "," + wifi.Channel + "," + wifi.Signal;
			}
			csv_build += "\n";
		}
		return csv_build;
	}
}
