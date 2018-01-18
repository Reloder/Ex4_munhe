package Database;

/**
 * This class represents the wifi data of a router device.
 */
public class WifiData implements Comparable<WifiData> {
	public String Name,MAC,Channel;
	public double Signal;
	
	public static final int NON_SIGNAL = -120; // if there is no signal the minimum number is this.
	
	public WifiData() {
		Signal = NON_SIGNAL;
	}
	
	public WifiData(String Name,String SSID,String Channel,double Signal) {
		this.Name = Name;
		this.MAC = SSID;
		this.Channel = Channel;
		this.Signal = Signal;
		
	}
	
	public WifiData(String Name,String SSID,String Channel,String Signal) {
		this.Name = Name;
		this.MAC = SSID;
		this.Channel = Channel;
		try {
			this.Signal = Double.parseDouble(Signal);
		} catch(Exception ex) {
			this.Signal = NON_SIGNAL;
		}
	}
	
	@Override
	public int compareTo(WifiData obj1) {
		if (this.Signal > obj1.Signal)
			return 1;
		if (this.Signal < obj1.Signal)
			return -1;
		return 0;
	
	}
	
	public boolean isEquals(WifiData wifi) {
		if(wifi.Name.equals(Name) && 
			wifi.MAC.equals(MAC) && 
			wifi.Signal == Signal && 
			wifi.Channel.equals(Channel))
			return true;
		return false;
	}
	

}
