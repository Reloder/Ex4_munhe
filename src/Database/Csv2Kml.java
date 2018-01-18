package Database;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import de.micromata.*;
import de.micromata.opengis.kml.v_2_2_0.Document;
import de.micromata.opengis.kml.v_2_2_0.Kml;

public class Csv2Kml {

	
	/**
	 * The function produce a kml file from a csv file as needed.
	 * @param csv = get the csv ready to become a kml file
	 * @throws IOException
	 */
	public static void csv2kml(File csv) throws IOException {
		Kml kml = new Kml();
		Document doc =kml.createAndSetDocument();
	
		FileReader filereader=new FileReader(csv);
		@SuppressWarnings("resource")
		BufferedReader br=new BufferedReader(filereader);
		String str;
		double latitude=0;
		double lontitude=0;
		boolean isFirstLine = true;
		while((str = br.readLine()) != null)
		{
			if(isFirstLine) {
				isFirstLine =false;
				continue;
			}
			String[] srr=str.split(",");
			latitude=Double.parseDouble((srr[2].trim()));
			lontitude=Double.parseDouble((srr[3].trim()));
			
			// create the coordinate of the record we took.
			doc.createAndAddPlacemark().withName("Record_" + srr[1]).withOpen(Boolean.TRUE).createAndSetPoint().addToCoordinates(latitude, lontitude);
		
		}
		String folder = csv.getParent();
		File file = new File(folder + "//New_KML.kml"); // create the file with the name "google_earth_kml"
		kml.marshal(file); // parse the kml to the file created.
	}
}
