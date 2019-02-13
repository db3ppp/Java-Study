import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Vector;

public class DAO {
	DTO dto = new DTO();
//	MemberList mList;
	RankList rList;
	
	final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver"; 
	final String DB_URL = "jdbc:mysql://localhost:3306/test?serverTimezone=Asia/Seoul"; 
	final String USERNAME = "hyewon"; 
	final String PASSWORD = "gpdnjs206";
	
	public DAO(){}
	public DAO(RankList rList) {
		this.rList = rList;
	}
	
	public Vector<Vector>getRankList(){
		Vector<Vector>data = new Vector<Vector>();
		return data;
	}
	public void insertRank(DTO dto)throws Exception{
		System.out.println("insertRank method");
	}
	/*
	public Vector<Vector>getRankList(){
		Vector<Vector>data = new Vector<Vector>();
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL,USERNAME,PASSWORD);
			String sql = "select * from test.rank order by score desc";
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
		System.out.println("insertRank method");
		Connection conn = null;		
		Statement stmt = null;
		
		String id = dto.getId();
		int score = dto.getScore();
		String sql = "INSERT INTO test.rank (id, score) VALUES";
		
//		final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver"; 
//		final String DB_URL = "jdbc:mysql://localhost:3306/test?serverTimezone=Asia/Seoul"; 
//		final String USERNAME = "hyewon"; 
//		final String PASSWORD = "gpdnjs206";

		System.out.println("insertRank");
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
	*/
	  

}
