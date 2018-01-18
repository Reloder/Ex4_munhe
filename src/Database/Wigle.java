package Database;

/**
 * The information of each device;
 * @author maor_
 *
 */
public class Wigle implements Comparable<Wigle> {
	int RSSI = 0;
 	String MAC="", SSID="", AuthMode="", FirstSeen="", Channel="", Latitude="", Longitude="", Altitude="", Accuracy="", Type=""; 

	 public Wigle(){
		 
	 }
	 

	 /*
	  * implementaion of the comparbale interface by the strongest signal of the wigle record
	  * @see java.lang.Comparable#compareTo(java.lang.Object)
	  */
	@Override
	public int compareTo(Wigle obj1) {
		if (this.RSSI > obj1.RSSI)
			return 1;
		if (this.RSSI < obj1.RSSI)
			return -1;
		return 0;
	
	}
 }
 
 


