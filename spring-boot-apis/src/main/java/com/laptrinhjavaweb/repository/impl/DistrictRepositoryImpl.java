package com.laptrinhjavaweb.repository.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.stereotype.Repository;

import com.laptrinhjavaweb.entity.DistrictEntity;
import com.laptrinhjavaweb.repository.DistrictRepository;
import com.laptrinhjavaweb.util.ConnectDB;

@Repository
public class DistrictRepositoryImpl implements DistrictRepository {

	private Connection conn = null;
	private Statement stmt;
	private ResultSet rs;

	@Override
	public DistrictEntity findDistrictById(Long id) throws SQLException {
		DistrictEntity districtEntity = new DistrictEntity();
		try {
			conn = ConnectDB.getConnection(); // connect db
			 if (conn != null) {
				 conn.setAutoCommit(false);
				 stmt = conn.createStatement();
				 rs = stmt.executeQuery("SELECT * FROM district where id = " + id); // querry search
				 while (rs.next()) { // Xử lý kết quả trả về
				    districtEntity.setId(rs.getInt("id"));
				    districtEntity.setCode(rs.getString("code"));
				    districtEntity.setName(rs.getString("name"));
				 }
		    }
			 conn.commit();
		} catch (Exception e) {
			    conn.rollback();
				System.out.println("Error jdbc district ");
				e.printStackTrace();
			
		} finally {
	 		 conn.close();
             rs.close();
             stmt.close();
		}
		return districtEntity;	
	}

}
