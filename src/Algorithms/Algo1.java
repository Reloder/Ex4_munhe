package Algorithms;



import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Dictionary;
import java.util.TreeMap;

import Database.CsvFile;
import Database.Record;
import Database.WifiData;
import Tools.Tools;


/**
 * This class responsible on the algorithm a part of the project.
 * 
 */
public class Algo1 {

	private final int max_best_records = 3;
	private CsvFile csv;
	public String MAC;

	/**
	 * Initialize the object elements
	 * @param file_path = file path to the csv file
	 * @param MAC = MAC to seek for matches
	 */
	public Algo1(String file_path, String MAC) {
		readCsvFile(file_path);
		this.MAC = MAC;
	}
	
	/**
	 * Start the calculation and return a weighted record contain the coords of the approximated wifi MAC
	 * by the csv file we inserted in the constructor.
	 * @return
	 */
	public WeightRecord getAlgoResult() {
		ArrayList<WeightRecord> best_records = getBestRecordsByMac(csv, MAC);
		return Tools.calculateLocation(best_records);
	}

	/**
	 * Read the csv file and store it in the CsvFile object.
	 * @param file_path
	 */
	private void readCsvFile(String file_path) {
		try {
			csv = new CsvFile(file_path, true);
		} catch (IOException e) {

		}
	}

	/**
	 * The main function of the algorithm.
	 * @param csv
	 * @param mac
	 * @return
	 */
	private ArrayList<WeightRecord> getBestRecordsByMac(CsvFile csv, String mac) {
		ArrayList<WeightRecord> list = new ArrayList<>();
		for (int i = 0; i < csv.getRecords().size(); i++) {
			Record rec = csv.getRecords().get(i);
			for (int j = 0; j < rec.wifiList.size(); j++) {
				WifiData wifi = rec.wifiList.get(j);
				if (wifi.MAC.equals(mac)) {
					WeightRecord w_rec = new WeightRecord(rec.lan, rec.lon, rec.alt, wifi.Signal);
					w_rec.weight = (double) 1 / (wifi.Signal * wifi.Signal); // Formula to calculate the weight 
					list.add(w_rec);
				}
			}
		}
		
		Collections.sort(list);
		return Tools.reverseArrayList(list,max_best_records);

	}
	



}
