import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DAO_Client {
	private final static String URL_DB ="jdbc:mysql://localhost:3306/Chat";
	private final static String DRIVER ="com.mysql.jdbc.Driver";
	private final static String ID ="root";
	private final static String PASSWORD ="root";
	
	static {
		try {
			Class.forName(DRIVER);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Connection getConn() {
		Connection conn = null;
		try {
			
			 conn = DriverManager.getConnection(URL_DB, ID, PASSWORD);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return conn;
	} 
	
}
