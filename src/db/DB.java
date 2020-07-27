package db;

import java.sql.*;
import java.sql.DriverManager;

public class DB {
	public static Connection conn() throws Exception {
		Connection conn = null;
		Class.forName("com.mysql.jdbc.Driver"); // mysql 설치 여부
		conn = DriverManager.getConnection(DBConfig.DB_URL, DBConfig.DB_USER, DBConfig.DB_PW); // 연결 (주소, 사용자 이름, 비번)
//            System.out.println("연결 성공");
		return conn;
	}
}
