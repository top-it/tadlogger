package ma.topit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import javax.servlet.http.Cookie;

import org.eclipse.jetty.server.Authentication;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.RequestLog;
import org.eclipse.jetty.server.Response;

import org.eclipse.jetty.http.HttpHeaders;


public class JettyRequestLog  implements  RequestLog {

	private Connection con = null;
	private PreparedStatement pstmtLog = null;
	private PreparedStatement pstmtCookies = null;
	
	public void log(Request request, Response response) {
		
		try {
			long now = System.currentTimeMillis();
			
			pstmtLog.setString(1, request.getServerName());
			pstmtLog.setString(2,request.getHeader(HttpHeaders.X_FORWARDED_FOR.toString()));
			pstmtLog.setString(3,request.getRemoteAddr());
			
			
			Authentication authentication=request.getAuthentication();
            if (authentication instanceof Authentication.User)
            	pstmtLog.setString(4,((Authentication.User)authentication).getUserIdentity().getUserPrincipal().getName());
            else
            	pstmtLog.setNull(4, java.sql.Types.VARCHAR);
            
            pstmtLog.setTimestamp(5,new Timestamp(request.getTimeStamp()));
            
            pstmtLog.setString(6,request.getMethod());
            pstmtLog.setString(7,request.getUri().toString());
            pstmtLog.setString(8,request.getProtocol());
            
            
            pstmtLog.setInt(9, response.getStatus());
            pstmtLog.setLong(10, response.getContentCount());
            
            pstmtLog.setString(11,request.getHeader(HttpHeaders.REFERER.toString()));
            pstmtLog.setString(12,request.getHeader(HttpHeaders.USER_AGENT.toString()));
                     
            
            
            
            
            long d = request.getDispatchTime();
            
            
            pstmtLog.setLong(13,now - (d==0 ? request.getTimeStamp():d));
            
            pstmtLog.setLong(14,now - request.getTimeStamp());
            
            pstmtLog.executeUpdate();
           int request_log_id = getGeneratedKey(pstmtLog);
           
           Cookie[] cookies = request.getCookies();
           if(cookies!=null && cookies.length>0) {
        	   pstmtCookies.setInt(1, request_log_id);
        	   for (int i = 0; i < cookies.length; i++)
               {
        		   pstmtCookies.setString(2, cookies[i].getName());
        		   pstmtCookies.setString(2, cookies[i].getValue());
                   
               }
        	   
           }
            
			
		} catch (SQLException e) {
			//warn
		}
		
		
	}
	
	
	private int getGeneratedKey(PreparedStatement pstm) throws SQLException {
		ResultSet rs = null;
		int res = 0;
		try {
			rs = pstm.getGeneratedKeys();
			
			res = rs.getInt(0);
		}finally {
			UtilDb.clean(rs);
		}
		
		
		return res;
		
	}

	@Override
	public void addLifeCycleListener(Listener arg0) {
		
		
	}

	@Override
	public boolean isFailed() {
		
		return false;
	}

	@Override
	public boolean isRunning() {
		
		return false;
	}

	@Override
	public boolean isStarted() {
		
		return false;
	}

	@Override
	public boolean isStarting() {
		
		return false;
	}

	@Override
	public boolean isStopped() {
		
		return false;
	}

	@Override
	public boolean isStopping() {
		
		return false;
	}

	@Override
	public void removeLifeCycleListener(Listener arg0) {
		
		
	}

	@Override
	public void start() throws Exception {
		UtilDb utilDb = new UtilDb(driver, url, user, password);
		con = utilDb.getConnection();
			 
		
		pstmtLog = con.prepareStatement("insert into request_log (server_name,proxied_address,remote_address,user_name,date_request,method,uri,protocol,status,length,referer,agent,latency ,dispatchtime) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,)");
		pstmtCookies = con.prepareStatement("insert into cookies_log(request_id,name,value) values (?,?,?)");
	}

	@Override
	public void stop() throws Exception {
		UtilDb.clean(pstmtLog);
		UtilDb.clean(pstmtCookies);
		UtilDb.clean(con);
	}

	
	private String driver;
	private String url;
	private String user;
	private String password;

	public void setDriver(String driver) {
		this.driver = driver;
	}


	public void setUrl(String url) {
		this.url = url;
	}


	public void setUser(String user) {
		this.user = user;
	}


	public void setPassword(String password) {
		this.password = password;
	}

	
	
	
	
	
	
	
}
