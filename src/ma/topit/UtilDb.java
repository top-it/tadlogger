package ma.topit;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;




public class UtilDb {
	
	private String driver;
	private String url;
	private String user;
	private String password;
	
	
	
	

	public UtilDb(String driver, String url, String user, String password) {
		super();
		this.driver = driver;
		this.url = url;
		this.user = user;
		this.password = password;
	}

	//public static Logger LOGGER = Logger.getLogger(JdbcConnexion.class);
	public  Connection getConnection(boolean autoCommit)  {

		Connection con = null;

		try {
			Class.forName(driver).newInstance();
			con = DriverManager.getConnection(url, user,password);
			
			if(con!=null) con.setAutoCommit(autoCommit);

			return con;

		} catch (Exception e) {
			//LOGGER.error("Erreur au niveau de la creation de la connection DB");
			throw new RuntimeException(e);

		}
		 

		
	}
	
	public  Connection getConnection() {
		return getConnection(true);
	}
	
	public static void clean(Statement st) {
		if(st!=null)
			try {
				st.close();
			} catch (SQLException e) {
				
				//LOGGER.warn(e);
			}
	}
	
	
	public static void clean(ResultSet rs, Statement st) {
		if(rs!=null) {
			try {
				rs.close();
			} catch (SQLException e) {
				
				//LOGGER.warn(e);
			}
		}
		
		if(st!=null) {
			try {
				st.close();
			} catch (SQLException e) {
				
				//LOGGER.warn(e);
			}
		}
	}
	
	public static void clean(ResultSet rs) {
		if(rs!=null) {
			try {
				rs.close();
			} catch (SQLException e) {
				
				//LOGGER.warn(e);
			}
		}
		
		
	}
	
	public static void clean(Connection con) {
		if(con!=null)
			try {
				con.close();
			} catch (SQLException e) {
				
				//LOGGER.warn(e);
			}
	}
	
	public static void clean(Connection con,ResultSet rs, Statement st) {
		clean(rs,st);
		clean(con);
	}
	
	public static void clean(Connection con, Statement st) {
		clean(st);
		clean(con);
	}
	
}