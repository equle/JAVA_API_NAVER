package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import db.DB;
import dto.naverDTO;

public class naverDAOimpl implements naverDAO {

	@Override
	public void insert(naverDTO dto) {
		// DB연결
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = DB.conn();

			String sql = "INSERT INTO news_tb (title, url) VALUES (?, ?);";
			pstmt = conn.prepareStatement(sql);

			// dto를 insert 쿼리
			pstmt.setString(1, dto.getTitle());
			pstmt.setString(2, dto.getUrl());

			// 쿼리 실행
			int i = pstmt.executeUpdate();

			if (i == 0) {
				System.out.println("데이터 입력 실패");
			} else if (i == 1) {
				System.out.println("데이터 입력 성공");
			}

		} catch (ClassNotFoundException e) {
			System.out.println("드라이버 로딩 실패");
		} catch (SQLException e) {
			System.out.println("에러: " + e);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (conn != null && !conn.isClosed()) {
					conn.close();
				}
				if (pstmt != null && !pstmt.isClosed()) {
					pstmt.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

	@Override
	public void select() {
		// TODO Auto-generated method stub

	}

}
