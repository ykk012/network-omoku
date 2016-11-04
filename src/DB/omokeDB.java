package DB;


import java.sql.*;



public class omokeDB {

	
	
	
	public omokeDB(){
		
		
	}
	public Connection connectDB() throws ClassNotFoundException, SQLException{
			Class.forName("com.mysql.jdbc.Driver");
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/test?user=root&password=");
			return conn;
	}
	
	public void insert(String str ,int player) throws ClassNotFoundException, SQLException {
		Connection c =connectDB();
		Statement stmt = c.createStatement();
		
		String query = " insert into omoku values( '"+str+"', "+player+")";

		try {
			stmt = c.createStatement();
			stmt.executeUpdate(query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch(Exception e2){
			
		}finally{
			
				try {
					
					if(stmt !=null) stmt.close();
					if(c !=null) c.close();
					
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
		}
		
		
	}
	
	public int selectPlayerByName(String str) throws SQLException, ClassNotFoundException{
		Connection c =connectDB();
		ResultSet rs = null;
		Statement stmt = c.createStatement();
		
		int player = 0;
		try {
			
			String query= " select player from omoku where name like '"+str+"' ";
			rs=stmt.executeQuery(query);
			
			if(rs.next()){
				player=rs.getInt("player");
			}
			

			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(rs !=null )rs.close();
		if(stmt !=null) stmt.close();
		if(c !=null) c.close();
		
		return player;
		
	}
	public String selectNameByPlayer(int player) throws SQLException, ClassNotFoundException{
		Connection c =connectDB();
		ResultSet rs = null;
		Statement stmt = c.createStatement();
		String name = null;
		try {
			
			String query= " select name from omoku where player="+player;
			rs=stmt.executeQuery(query);
			if(rs.next()){
				name=rs.getString("name");
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(rs !=null )rs.close();
		if(stmt !=null) stmt.close();
		if(c !=null) c.close();
		
		return name;
		
	}
	
	
	public int selectCheckPlayer() throws SQLException, ClassNotFoundException{
		Connection c =connectDB();
		ResultSet rs = null;
		Statement stmt = c.createStatement();
		int count = 0;
		try {
			
			String query= " select count(*) from omoku where player <= 2";
			rs=stmt.executeQuery(query);
			if(rs.next()){
				count=rs.getInt(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(rs !=null )rs.close();
		if(stmt !=null) stmt.close();
		if(c !=null) c.close();
		
		

		return count;
	}
	
//----------------------- Ù£îñªòü¬ìãª¹ªë--------------------//
	public String selectIsName(String str) throws SQLException, ClassNotFoundException{
		Connection c =connectDB();
		ResultSet rs = null;
		Statement stmt = c.createStatement();
		String name = null;
		try {
			
			String query= " select name from omoku where name like '"+str+"' ";//----------------------- Ù£îñªòü¬ìãª¹ªësql--------------------//
			
			rs=stmt.executeQuery(query);
			
			if(rs.next()){
				name=rs.getString("name");
			}
			
	
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(rs !=null )rs.close();
		if(stmt !=null) stmt.close();
		if(c !=null) c.close();
		
		return name;
		
	}
	public void deleteCleanRoom() throws SQLException, ClassNotFoundException{
		Connection c =connectDB();
		
		Statement stmt = c.createStatement();
		
		try {
			
			String query= " delete from omoku";
			stmt.executeUpdate(query);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(stmt !=null) stmt.close();
		if(c !=null) c.close();
		
	}
}
