package Algorithms;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import Database.CsvFile;
import Database.Record;
import Database.WifiData;
import Tools.Tools;

/**
 * This class responsible on the algorithm b part of the project.
 * Take a data csv(with gps), input csv(with no gps) and calculate a new csv file,
 *  which contains the input file with approximated gps information.
 */
public class Algo2 {

	// Some constant parameters
	private int min_diff = 3;
	private int no_signal = -120;
	private int diff_no_signal = 100;
	private double norm = 10000;
	private int power = 2;
	private float sig_diff = 0.4f;

	private int max_best_records = 4;

	
	// The CsvFile objects of the input and data csvs
	private CsvFile csv_input;
	private CsvFile csv_data;

	public Algo2(String input_path, String data_path) {
		readCsvFile(input_path, data_path);

	}

	/**
	 * Read the input and data csvs and store them.
	 * @param data_path
	 * @param input_path
	 */
	private void readCsvFile(String data_path, String input_path) {
		try {
			csv_data = new CsvFile(data_path, true);
			csv_input = new CsvFile(input_path, false);
		} catch (IOException e) {

		}
	}
	
	public CsvFile getCsvInputFile() {
		return csv_input;
	} 
	
	public CsvFile getCsvDataFile() {
		return csv_data;
	}

	/**
	 * The main function of the algorithm
	 * calculate and export a new csv file with gps coords.
	 * @return
	 */
	public CsvFile calculateNewGPSFile() {
		CsvFile csv_input_new = csv_input.duplicate(); // duplicate the input csv file (to redefine the records coords without touching the old input elements)
		for (int i = 0; i < csv_input_new.getRecords().size(); i++) { // foreach record in the csv input
			Record input_rec = csv_input_new.getRecords().get(i);
			ArrayList<WeightRecord> weighted_records = new ArrayList<>();

			for (int j = 0; j < csv_data.getRecords().size(); j++) { // foreach record in the csv data
				Record data_rec = csv_data.getRecords().get(j);
				double total_rec_weight = 1;
				for (int input_wifi_index = 0; input_wifi_index < input_rec.wifiList.size(); input_wifi_index++) { // foreach wifi in the input record
					WifiData input_wifi = input_rec.wifiList.get(input_wifi_index);
					String wifi_mac = input_wifi.MAC;
					WifiData data_wifi = getWifiDataByMac(wifi_mac, data_rec.wifiList); // check if there a match to a wifi in the data record by the MAC.
					double w = calculateWeightBetween(input_wifi, data_wifi); // and calculate the weight by a special formula.
					total_rec_weight *= w;
				}

				double lan = data_rec.lan;
				double lon = data_rec.lon;
				double alt = data_rec.alt;
				WeightRecord w_record = new WeightRecord(lan, lon, alt, WifiData.NON_SIGNAL);
				w_record.weight = total_rec_weight;
				weighted_records.add(w_record); // add the weighted record 

			}
			
			Collections.sort(weighted_records); // sort the list of weighted records 
			ArrayList<WeightRecord> best_records = Tools.reverseArrayList(weighted_records,max_best_records); // get only the best records
			WeightRecord location = Tools.calculateLocation(best_records); // calculate their location
			input_rec.lan = location.getLan();
			input_rec.lon = location.getLon();
			input_rec.alt = location.getAlt();
		}
		
		return csv_input_new; // return the whole new duplicated calculated CsvFile.
	}

	/**
	 * Run on the list and get if there is a match to the MAC.
	 * @param Mac
	 * @param wifiList
	 * @return
	 */
	private WifiData getWifiDataByMac(String Mac, ArrayList<WifiData> wifiList) {
		for (int i = 0; i < wifiList.size(); i++) {
			WifiData wifi = wifiList.get(i);
			if (wifi.MAC.equals(Mac)) {
				return wifi;
			}
		}
		return new WifiData();
	}

	/**
	 * Calculate and return the weight between two different wifi.
	 * @param one
	 * @param two
	 * @return
	 */
	private double calculateWeightBetween(WifiData one, WifiData two) {
		double diff = 0;
		if (two.Signal <= -120 || two.Signal > 0) { // NO_SIGNAL
			diff = diff_no_signal;
		} else {
			diff = Math.abs(one.Signal - two.Signal);
			if (diff < min_diff)
				diff = min_diff;
		}
		double first = Math.pow(diff, sig_diff);
		double second = Math.pow(one.Signal, power);
		double w = norm / (first * second);
		return w;
	}
}
