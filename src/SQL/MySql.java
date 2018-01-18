package SQL;
import Database.CsvFile;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.Statement;
import Database.Record;

public class MySql {
	private static String _ip = "127.0.0.1";
	private static String _url = "jdbc:mysql://"+_ip+":3306/csv_db";
	private static String _user = "maor_user";
	private static String _password = "Maor100%";
	private Connection conn = null;
	private static MySql instance = null;
	
	private MySql(String ip,String url,String user,String pass) throws SQLException {
		conn = DriverManager.getConnection(url, user, pass);
	}
	
	public static MySql getInstance() {
		if(instance == null) {
			try {
				instance  = new MySql(_ip,_url,_user, _password);
			} catch(Exception ex) {
				
			}
			
		}
		return instance;
	}
	
	
	
	public void UpdateSqlTable(ArrayList<Record> records)  {
		try {
			  Statement stmt = conn.createStatement();

			    // Use TRUNCATE
			    String drop_query = "TRUNCATE my_table";
			    // Execute deletion
			    stmt.executeUpdate(drop_query);
			    // Use DELETE
			    drop_query = "DELETE FROM my_table";
			    // Execute deletion
			    stmt.executeUpdate(drop_query);
			    stmt.close();
			    
			    stmt = conn.createStatement();
			for(int i=0;i<records.size();i++) {
				Record rec = records.get(i);
				String insert_query = "INSERT INTO Records VALUES(" + rec.toString() + ")";
				stmt.executeQuery(insert_query);
			}
			
		} catch(Exception ex ) {
			
		}
		
	}
	
	public void Close() {
		try{
			conn.close();
			
			conn = null;
		}catch(Exception ex) {
			
		}
		
	}
	
	
}