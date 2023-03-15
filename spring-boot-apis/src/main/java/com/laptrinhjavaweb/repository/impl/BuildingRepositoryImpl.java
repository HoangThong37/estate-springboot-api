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
				 StringBuilder sqlNotJoin = new StringBuilder();
				 
				 sqlWithJoin(request, sqlJoin, types, sqlNotJoin); // join table
				 sqlNoJoin(request, sqlNotJoin);   // no join table
				 
				 querry.append(sqlJoin).append(" where 1=1 ").append(sqlNotJoin);
				 
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

	// Build query với JOIN thì xử lý luôn WHERE cho quý khách, đỡ phải check nhiều lần
	private void sqlWithJoin(Map<String, String> request, StringBuilder sqlJoin, List<String> types, StringBuilder sqlNotJoin) {
		// join rentare
		// Search form and to of rentArea (diện tích thuê)
		String rentAreaFrom = request.get("rentAreaFrom");
		String rentAreaTo = request.get("rentAreaTo");
		if (!checkInputSearch.isNullStr(rentAreaFrom) || !checkInputSearch.isNullStr(rentAreaTo)) {
			sqlJoin.append(" inner join rentarea as ra on b.id = ra.buildingid ");
				if (!checkInputSearch.isNullStr(rentAreaFrom)) {
					sqlJoin.append(" and ra.value <= " + rentAreaFrom + "");
				}
				if (!checkInputSearch.isNullStr(rentAreaTo)) {
					sqlJoin.append(" and ra.value >= " + rentAreaTo + "");
				}
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
			sqlJoin.append(" AND( " + String.join(" - ", buildingTypes) + " )");
		}
		
		// search staff 
		// nhân viên quản lí
		String staff = request.get("staffid");
		if (!checkInputSearch.isNullStr(staff)) {
			sqlJoin.append(" inner join assignmentbuilding ab on b.id = ab.buildingid inner join user as u  on ab.staffid = u.id ");
				if (!checkInputSearch.isNullStr(staff)) {
					sqlJoin.append(" and u.id = " + staff + " ");
				}
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
		String rentPriceFrom = request.get("rentAreaFrom");
		String rentPriceTo = request.get("rentAreaTo");
		if (!checkInputSearch.isNullStr(rentPriceFrom)) {
			sqlNotJoin.append(" and b.rentprice <= " + rentPriceFrom + "");
		}
		if (!checkInputSearch.isNullStr(rentPriceTo)) {
			sqlNotJoin.append(" and b.rentprice >= " + rentPriceTo + "");
		}
	}

	
	
	
	
//	private String querrySearch(BuildingSearchRequest buildingSearchRequest) {
//		StringBuilder stringBuilder = new StringBuilder("SELECT * FROM building as b ");
//		// inner join rentArea (diện tích thuê)
//		if (!checkInputSearch.isNullInt(buildingSearchRequest.getRentAreaFrom()) || !checkInputSearch.isNullInt(buildingSearchRequest.getRentAreaTo())) {
//			stringBuilder.append(" inner join rentarea as ra on b.id = ra.buildingid ");
//		}
//		// loại tòa nhà
//		if (buildingSearchRequest.getRentTypes() != null &&  buildingSearchRequest.getRentTypes().size() > 0) {
//			stringBuilder.append(" inner join buildingrenttype as br on b.id = br.buildingid \r\n" + 
//								 " inner join renttype as rt on br.renttypeid = rt.id ");
//		}
//		// nhân viên quản lí
//		if (!checkInputSearch.isNullInt(buildingSearchRequest.getStaffId())) {
//			stringBuilder.append(" inner join assignmentbuilding ab on b.id = ab.buildingid inner join user as u  on ab.staffid = u.id ");
//		}
//		stringBuilder.append(" WHERE 1=1 ");
//		// search gần đúng
//		// name building
//		if (!checkInputSearch.isNullStr(buildingSearchRequest.getName())) {
//			stringBuilder.append(" and b.name LIKE '%" + buildingSearchRequest.getName() + "%'");
//		}
//		// phường, đường, hướng, hạng
//		if (!checkInputSearch.isNullStr(buildingSearchRequest.getWard())) {
//			stringBuilder.append(" and b.ward LIKE '%" + buildingSearchRequest.getWard() + "%'");
//		}
//		if (!checkInputSearch.isNullStr(buildingSearchRequest.getStreet())) {
//			stringBuilder.append(" and b.street LIKE '%" + buildingSearchRequest.getStreet() + "%'");
//		}
//		if (!checkInputSearch.isNullStr(buildingSearchRequest.getDirection())) {
//			stringBuilder.append(" and b.direction LIKE '%" + buildingSearchRequest.getDirection() + "%'");
//		}
//		if (!checkInputSearch.isNullStr(buildingSearchRequest.getLevel())) {
//			stringBuilder.append(" and b.level LIKE '%" + buildingSearchRequest.getLevel() + "%'");
//		}
//		// Search chính xác
//		// floorarea
//		if (!checkInputSearch.isNullStr(buildingSearchRequest.getFloorArea())) {
//			stringBuilder.append(" and b.floorarea = " + buildingSearchRequest.getFloorArea() + "");
//		}
//		// numberOfBasement
//		if (!checkInputSearch.isNullInt(buildingSearchRequest.getNumberOfBasement())) {
//			stringBuilder.append(" and b.numberofbasement = " + buildingSearchRequest.getNumberOfBasement() + "");
//		}
//		// Search From - To
//		// Search form and to of rentArea (diện tích thuê)
//		if (!checkInputSearch.isNullInt(buildingSearchRequest.getRentAreaFrom())) {
//			stringBuilder.append(" and ra.value <= " + buildingSearchRequest.getRentAreaFrom() + "");
//		}
//		if (!checkInputSearch.isNullInt(buildingSearchRequest.getRentAreaTo())) {
//			stringBuilder.append(" and ra.value >= " + buildingSearchRequest.getRentAreaTo() + "");
//		}
//		// Search form and to of rentprice (giá thuê from -> to)
//		if (!checkInputSearch.isNullInt(buildingSearchRequest.getRentPriceFrom())) {
//			stringBuilder.append(" and b.rentprice <= " + buildingSearchRequest.getRentPriceFrom() + "");
//		}
//		if (!checkInputSearch.isNullInt(buildingSearchRequest.getRentPriceTo())) {
//			stringBuilder.append(" and b.rentprice >= " + buildingSearchRequest.getRentPriceTo() + "");
//		}
//		// search staff 
//		if (!checkInputSearch.isNullInt(buildingSearchRequest.getStaffId())) {
//			stringBuilder.append(" and u.id = " + buildingSearchRequest.getStaffId() + " ");
//		}
//		return stringBuilder.toString();
//	}
	
}
