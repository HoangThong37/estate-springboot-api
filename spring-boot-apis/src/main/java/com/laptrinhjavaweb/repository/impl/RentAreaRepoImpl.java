package com.laptrinhjavaweb.repository.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.laptrinhjavaweb.entity.RentAreaEntity;
import com.laptrinhjavaweb.repository.RentAreaRepository;
import com.laptrinhjavaweb.util.ConnectDB;

@Repository
public class RentAreaRepoImpl implements RentAreaRepository{
	
	private Connection conn = null;
	private Statement stmt;
	private ResultSet rs;

	@Override
	public List<RentAreaEntity> findRentArea(Integer id) throws SQLException {
		
		List<RentAreaEntity> rentAreaEntity = new ArrayList<>(); // id, value
		try {
			conn = ConnectDB.getConnection(); // connect db
			 if (conn != null) {
				 conn.setAutoCommit(false);
				 stmt = conn.createStatement();
				 String sql = "select ra.id, ra.value, ra.buildingid from rentarea as ra where ra.value >=  " + id;
				 rs = stmt.executeQuery(sql); // querry search
				 while (rs.next()) { // Xử lý kết quả trả về
					 RentAreaEntity rentArea = new RentAreaEntity();
					 rentArea.setId(rs.getInt("id"));
					 rentArea.setValue(rs.getInt("value"));
					 rentArea.setBuildingid(rs.getLong("buildingid"));
					 rentAreaEntity.add(rentArea);
				 }
		    }
			 conn.commit();
		} catch (Exception e) {
			    conn.rollback();
				System.out.println("Error jdbc rentArea ");
				e.printStackTrace();
			
		} finally {
	 		 conn.close();
             rs.close();
             stmt.close();
		}
		return rentAreaEntity;	
	}

}
