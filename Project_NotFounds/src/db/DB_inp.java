package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

public class DB_inp {
	static Logger logger = Logger.getLogger(DB_inp.class);
	private Connection conn = null;

	public DB_inp() {
		// default constructor
	}

	public Connection dbinit() {
		// DB init
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/notfound?characterEncoding=utf-8", "root", "pass");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {

		}
		return conn;
	}
	public boolean Template_Update(Connection conn, DB_TemUpdate temp) {
		PreparedStatement pstmt = null;
		try {
			pstmt = temp.QueryTemplate(conn);
			pstmt.executeUpdate(); 
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			logger.info(new Timestamp(System.currentTimeMillis()) + " :: " + pstmt);
			try {

				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return true;
	}

	private List<Map<String, String>> Template_Query(Connection conn, DB_TemQuery temp) {
		List<Map<String, String>> list = null;
		Map<String, String> map = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			list = new ArrayList<Map<String, String>>();
			rs = temp.QueryTemplate(conn);
			ResultSetMetaData rsmd = rs.getMetaData();
			int numberOfColumns = rsmd.getColumnCount();
			// ���� ����
			while (rs.next()) {
				map = new HashMap<String, String>();
				for (int i = 1; i <= numberOfColumns; i++) {
					map.put(rsmd.getColumnName(i), rs.getString(i));
				}
				list.add(map);
			}
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list;
	}
	
	public List<Map<String, String>> getList(final String query) {
		DB_TemQuery temp = new DB_TemQuery() {
			@Override
			public ResultSet QueryTemplate(Connection con) throws SQLException {
				PreparedStatement pstmt = con.prepareStatement(query);
				ResultSet rs = pstmt.executeQuery();
				logger.info(new Timestamp(System.currentTimeMillis()) + " :: " + query);
				return rs;
			}
		};
		return Template_Query(dbinit(), temp);
	}

}
