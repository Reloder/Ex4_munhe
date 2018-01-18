package Filters;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import Database.CsvFile;
import Database.Record;

public class Merging_Filters {
	private ArrayList<Filters> filters = new ArrayList<Filters>();
	private CsvFile original_csv_file = null; // for "or" filters
	private CsvFile current_csv_file = null;
	
	
	public Merging_Filters(CsvFile csv_file) {
		this.original_csv_file = csv_file;
		this.current_csv_file = csv_file.duplicate();
	}

	
	public ArrayList<Filters> getFilters() {
		return filters;
	}
	
	public CsvFile getCsvFile() {
		return this.current_csv_file;
	}
	
	public void reset() {
		filters.clear();
		this.current_csv_file = this.original_csv_file.duplicate();
	}
	
	public void addFilters(Filters filter) {
		filters.add(filter);
		if(filter.logic_gate == '&') {
			merge_and_filters(filter);
		} else if(filter.logic_gate == '|') {
			merge_or_filters(filter);
		} else if(filter.logic_gate == '!') {
			merge_not_filters(filter);
		}
	}
	
	private void merge_or_filters(Filters filter) {
		ArrayList<Record> records_org = original_csv_file.duplicate().getRecords();
		ArrayList<Record> records_to_add = new ArrayList<>();
		for(int i=0;i<records_org.size();i++) {
			Record curr = records_org.get(i);
			if(filter.checkRecord(curr)) {
				boolean isExists = current_csv_file.isRecordExists(curr);
				if(isExists == false) {
					records_to_add.add(curr);
				}
			}
		}
		current_csv_file.addRecords(records_to_add);
		
	}
	
	private void merge_and_filters(Filters filter) {
		ArrayList<Record> new_list = new ArrayList<>();
		for(int i=0;i<current_csv_file.getRecords().size();i++) {
			Record curr = current_csv_file.getRecords().get(i);
			if(filter.checkRecord(curr)) {
				new_list.add(curr);
			}
		}
		current_csv_file.getRecords().clear();
		current_csv_file.addRecords(new_list);
	}
	
	private void merge_not_filters(Filters filter) {
		ArrayList<Record> new_list = new ArrayList<>();
		for(int i=0;i<current_csv_file.getRecords().size();i++) {
			Record curr = current_csv_file.getRecords().get(i);
			if(filter.checkRecord(curr) == false) {
				new_list.add(curr);
			}
		}
		current_csv_file.getRecords().clear();
		current_csv_file.addRecords(new_list);
	}
	
	public String toString() {
		String str = "";
		for(int i=0;i<filters.size();i++) {
			Filters filter = filters.get(i);
			if(i != 0)
				str += "+";
			str += filter.logic_gate + filter.toString();
		}
		return str;
	}
	
	public void export_file(String path) {
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(new File(path));
		} catch (FileNotFoundException e) {
		    e.printStackTrace();
		}
		
		String file_text = toString();		
	
		pw.write(file_text);
        pw.close();
	}
	
	public void import_filters(String file_path) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(file_path));
		try {
		    StringBuilder sb = new StringBuilder();
		    String line = br.readLine();

		    while (line != null) {
		        sb.append(line);
		        sb.append(System.lineSeparator());
		        line = br.readLine();
		    }
		    String str = sb.toString();
		    
		    String[] matches = str.split("+");
		   
		    for(int i=0;i<matches.length;i++) {
		    	String match = matches[i];
		    	
		    	Filters filter = new Filters();
		    	if(filter.import_from_string(match)) {
		    		addFilters(filter);
		    	}
		    		
		    }
		    
		    
		} finally {
		    br.close();
		}
	}
	
	public  void merge_filters(String file_path,char logic_char) throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(file_path));
		try {
		    StringBuilder sb = new StringBuilder();
		    String line = br.readLine();

		    while (line != null) {
		        sb.append(line);
		        sb.append(System.lineSeparator());
		        line = br.readLine();
		    }
		    String str = logic_char + sb.toString();

	    	Filters filter = new Filters();
	    	if(filter.import_from_string(str)) {
	    		addFilters(filter);
	    	}
		    
		    
		    
		} finally {
		    br.close();
		}
		
	}
}
