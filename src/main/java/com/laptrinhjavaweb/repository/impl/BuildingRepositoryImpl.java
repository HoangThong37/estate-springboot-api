package com.laptrinhjavaweb.repository.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.laptrinhjavaweb.entity.BuildingEntity;
import com.laptrinhjavaweb.repository.BuildingRepository;
import com.laptrinhjavaweb.util.ConnectDB;
import com.laptrinhjavaweb.util.checkInputSearch;

@Repository
public class BuildingRepositoryImpl implements BuildingRepository {
	
	private Connection conn = null;
	private Statement stmt;
	private ResultSet rs;
	// private checkInputSearch checkInputSearch = new checkInputSearch();

	@Override
	public List<BuildingEntity> findBuilding(Map<String, String> request, List<String> types) throws SQLException {
	    List<BuildingEntity> buildingEntities = new ArrayList<>();
		try {
			 conn = ConnectDB.getConnection(); // connect db
			 conn.setAutoCommit(false);
			 if (conn != null) {
				 stmt = conn.createStatement();
				 StringBuilder querry = new StringBuilder("SELECT * FROM building as b "); 
				 StringBuilder sqlJoin = new StringBuilder();
				 StringBuilder sqlWhere = new StringBuilder(" where 1=1 ");
				 StringBuilder sqlNotJoin = new StringBuilder();
				 
				 sqlWithJoin(request, sqlJoin, sqlWhere, types, sqlNotJoin); // join table
				 sqlNoJoin(request, sqlNotJoin);   // no join table
				 
				 querry.append(sqlJoin).append(sqlWhere).append(sqlNotJoin).append(" GROUP BY b.id");
				 
				 rs = stmt.executeQuery(querry.toString());
				 // rs = stmt.executeQuery(querrySearch(buildingSearchRequest)); // querry search
				 while (rs.next()) { // Xử lý kết quả trả về
					BuildingEntity buildingEntity = new BuildingEntity();
					buildingEntity.setId(rs.getLong("id"));
					buildingEntity.setName(rs.getString("name"));
					buildingEntity.setStreet(rs.getString("street"));
					buildingEntity.setWard(rs.getString("ward"));
					buildingEntity.setDistrictId(rs.getLong("districtid"));
					buildingEntity.setStructure(rs.getString("structure"));
					buildingEntity.setNumberOfBasement(rs.getInt("numberofbasement"));
					buildingEntity.setFloorArea(rs.getInt("floorarea")); // dtich sàn
					buildingEntity.setRentPrice(rs.getInt("rentprice"));
					buildingEntity.setRentPriceDescription(rs.getString("rentpricedescription"));
					buildingEntity.setServiceFee(rs.getString("servicefee"));
					buildingEntity.setRentAreaId(rs.getInt("value"));
				    
					buildingEntities.add(buildingEntity);
				}
			}
		  conn.commit();
		} catch (Exception e) {
				conn.rollback();
				System.out.println("Error jdbc building");
				e.printStackTrace();
			
		} finally {
	 		  conn.close();
              rs.close();
            stmt.close();
		}
		return buildingEntities;	
	}

	
	private void sqlWithJoin(Map<String, String> request, StringBuilder sqlJoin, StringBuilder sqlWhere, List<String> types, StringBuilder sqlNotJoin) {
		// join rentare
		// Search form and to of rentArea (diện tích thuê)
		String rentAreaFrom = request.get("rentAreaFrom");
		String rentAreaTo = request.get("rentAreaTo");
		if (!checkInputSearch.isNullStr(rentAreaFrom) || !checkInputSearch.isNullStr(rentAreaTo)) {
			sqlJoin.append(" inner join rentarea as ra on b.id = ra.buildingid ");

			if (!checkInputSearch.isNullStr(rentAreaFrom)) {
				sqlWhere.append(" and ra.value >= " + rentAreaFrom + "");
			}
			if (!checkInputSearch.isNullStr(rentAreaTo)) {
				sqlWhere.append(" and ra.value <= " + rentAreaTo + "");
			}
		}
		
		// districtId
		String districtId = request.get("districtid");
		if (!checkInputSearch.isNullStr(districtId)) {
			sqlJoin.append(" inner join district as d on b.districtid = d.id ");
			sqlWhere.append(" and d.code = '" + districtId + "'");
		}

	    // type building -> list
		if (types != null &&  !types.isEmpty()) {
			sqlJoin.append(" inner join buildingrenttype as br on b.id = br.buildingid \r\n" + 
								 " inner join renttype as rt on br.renttypeid = rt.id ");
			// xử lý types building
			List<String> buildingTypes = new ArrayList<>();
			for (String item : types) {
				buildingTypes.add("rt.code LIKE '%" + item + "%'");
			}
			sqlJoin.append(" AND( " + String.join(" OR ", buildingTypes) + " )");
		}
		
		// search staff 
		String staff = request.get("staffid");
		if (!checkInputSearch.isNullStr(staff)) {
			sqlJoin.append(" inner join assignmentbuilding ab on b.id = ab.buildingid inner join user as u  on ab.staffid = u.id ");
			sqlWhere.append(" and u.id = " + staff + " ");
		}
	}
	
	
	private void sqlNoJoin(Map<String, String> request, StringBuilder sqlNotJoin) {
		// search field name
		String name = request.get("name");
		if (!checkInputSearch.isNullStr(name)) {
			sqlNotJoin.append(" and b.name LIKE '%" + name + "%'");
		}
	
		// phường, đường, hướng, hạng
		String ward = request.get("ward");
		if (!checkInputSearch.isNullStr(ward)) {
			sqlNotJoin.append(" and b.ward LIKE '%" + ward + "%'");
		}
		
		String street = request.get("street");
		if (!checkInputSearch.isNullStr(street)) {
			sqlNotJoin.append(" and b.street LIKE '%" + street + "%'");
		}
		
		String direction = request.get("direction");
		if (!checkInputSearch.isNullStr(direction)) {
			sqlNotJoin.append(" and b.direction LIKE '%" + direction + "%'");
		}
		
		String level = request.get("level");
		if (!checkInputSearch.isNullStr(level)) {
			sqlNotJoin.append(" and b.level LIKE '%" + level + "%'");
		}
		
		// Search chính xác
		String floorarea = request.get("floorarea");
		if (!checkInputSearch.isNullStr(floorarea)) {
			sqlNotJoin.append(" and b.floorarea = " + floorarea + "");
		}
		
		// numberOfBasement
		String numberofbasement = request.get("numberofbasement");
		if (!checkInputSearch.isNullStr(numberofbasement)) {
			sqlNotJoin.append(" and b.numberofbasement = " + numberofbasement + "");
		}
		
		// Search form and to of rentprice (giá thuê from -> to)
		String rentPriceFrom = request.get("rentPriceFrom");
		String rentPriceTo = request.get("rentPriceTo");
		if (!checkInputSearch.isNullStr(rentPriceFrom)) {
			sqlNotJoin.append(" and b.rentprice <= " + rentPriceFrom + "");
		}
		if (!checkInputSearch.isNullStr(rentPriceTo)) {
			sqlNotJoin.append(" and b.rentprice >= " + rentPriceTo + "");
		}
	}

}
