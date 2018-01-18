package Algorithms;

import Tools.Tools;

public class WeightRecord implements Comparable {
	public double weight=1;
	private double lan,lon,alt;
	private double signal;
	
	public WeightRecord() {
		
	}
	
	public WeightRecord(double lan,double lon, double alt, double signal) {
		this.lan = lan;
		this.lon = lon;
		this.alt = alt;
		this.signal = signal;
	}
	
	
	public double getLan() {
		return lan;
	}
	
	public double getLon() {
		return lon;
	}
	
	public double getAlt() {
		return alt;
	}
	
	public double getLanWeighted() {
		if(weight > 0) {
			return lan * weight;
		}
		return lan;
	}
	
	public double getLonWeighted() {
		if(weight > 0) {
			return lon * weight;	
		}
		return lon;
	}
	
	public double getAltWeighted() {
		if(weight > 0) {
			return alt * weight;
		}
		return alt;
	}

	@Override
	public int compareTo(Object obj1) {
		WeightRecord weightRecord= (WeightRecord)obj1;
		
		if (this.weight > weightRecord.weight)
			return 1;
		if (this.weight < weightRecord.weight)
			return -1;
		return 0;
	
	}
	
	@Override
	public String toString() {
		String lan_format_str = Tools.doubleToString(getLanWeighted(), 5);
		String lon_format_str = Tools.doubleToString(getLonWeighted(), 5);
		String alt_format_str = Tools.doubleToString(getAltWeighted(), 5);
		
		String str = "Lan: " + lan_format_str + " Lon: " + lon_format_str + " Alt: " + alt_format_str;
		return str;
	}


}
