import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Vector;

public class DAO {
	DTO dto = new DTO();
	MemberList mList;
	
	final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver"; 
	final String DB_URL = "jdbc:mysql://localhost:3306/test?serverTimezone=Asia/Seoul"; 
	final String USERNAME = "hyewon"; 
	final String PASSWORD = "gpdnjs206";
	
	public DAO(){}
	public DAO(MemberList mList) {
		this.mList = mList;
	}
	
	public Vector<Vector>getMemberList() {
		Vector<Vector>data = new Vector<Vector>();
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL,USERNAME,PASSWORD);
			String sql = "select * from guest order by name asc";
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			//stmt=(Statement)conn.createStatement();
			//stmt.executeUpdate(sql);
			
			while(rs.next()) {
				String name = rs.getString("name");
				String id = rs.getString("id");
				String pw = rs.getString("password");
				String major = rs.getString("major");
				String gender = rs.getString("gender");
				
				Vector<String> row = new Vector<String>();
				row.add(name);
				row.add(id);
				row.add(pw);
				row.add(major);
				row.add(gender);
				
				data.add(row);
			}
			
		} catch(Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		
		return data;
	}
	public Vector<Vector>getRankList(){
		Vector<Vector>data = new Vector<Vector>();
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL,USERNAME,PASSWORD);
			String sql = "select * from test.rank order by score desc limit 5";
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			//stmt=(Statement)conn.createStatement();
			//stmt.executeUpdate(sql);
			
			while(rs.next()) {
				String id = rs.getString("id");
				int score = rs.getInt("score");
				
				Vector<String> row = new Vector<String>();
				row.add(id);
				row.add(Integer.toString(score));
				
				data.add(row);
			}
			
		} catch(Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		
		return data;
		
	}
	public void insertRank(DTO dto)throws Exception{
		Connection conn = null;		
		Statement stmt = null;
		
		String id = dto.getId();
		int score = dto.getScore();
		String sql = "INSERT INTO test.rank(id, score) VALUES";
		
		try {
			sql+="('"+id+"', '"+score+"')";
			
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL,USERNAME,PASSWORD);
			stmt=(Statement)conn.createStatement();
			stmt.executeUpdate(sql);
			
		} catch(Exception e) {
			e.printStackTrace();
			System.exit(0);
		
		} finally {
			try {
				if(stmt != null)
					stmt.close();
				if(conn != null)
					conn.close();
			}catch(Exception e) {
				System.out.println(e.getMessage());
			}
		}
	}
	  
	public void insertAccount(DTO dto)throws Exception{
		Connection conn = null;		
		Statement stmt = null;
		
		String name = dto.getName();
		String id = dto.getId();
		String pw = dto.getPw();
		String major = dto.getMajor();
		String gender = dto.getGender();
		String sql = "INSERT INTO guest(name, id, password, major, gender) VALUES";
		
//		final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver"; 
//		final String DB_URL = "jdbc:mysql://localhost:3306/test?serverTimezone=Asia/Seoul"; 
//		final String USERNAME = "hyewon"; 
//		final String PASSWORD = "gpdnjs206";

		
		try {
			sql+="('"+name+"', '"+id+"', '"+pw+"', '"+major+"', '"+gender+"')";
			
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL,USERNAME,PASSWORD);
			stmt=(Statement)conn.createStatement();
			stmt.executeUpdate(sql);
			
		} catch(Exception e) {
			e.printStackTrace();
			System.exit(0);
		
		} finally {
			try {
				if(stmt != null)
					stmt.close();
				if(conn != null)
					conn.close();
			}catch(Exception e) {
				System.out.println(e.getMessage());
			}
		}
	}

}
