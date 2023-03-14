package com.laptrinhjavaweb.repository.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.type.TrueFalseType;
import org.springframework.stereotype.Repository;

import com.laptrinhjavaweb.dto.request.BuildingSearchRequest;
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
	public List<BuildingEntity> findBuilding(BuildingSearchRequest request) throws SQLException {
	    List<BuildingEntity> buildingEntities = new ArrayList<>();
		try {
			 conn = ConnectDB.getConnection(); // connect db
			 conn.setAutoCommit(false);
			 if (conn != null) {
				 stmt = conn.createStatement();
				 StringBuilder querry = new StringBuilder("SELECT * FROM building as b "); 
				 StringBuilder sqlJoin = new StringBuilder();
				 StringBuilder sqlNotJoin = new StringBuilder();
				 
				 sqlWithJoin(request, sqlJoin, sqlNotJoin); // join table
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

	private void sqlWithJoin(BuildingSearchRequest request, StringBuilder sqlJoin, StringBuilder sqlNotJoin) {
		// join rentare
	   if (!checkInputSearch.isNullInt(request.getRentAreaFrom()) || !checkInputSearch.isNullInt(request.getRentAreaTo())) {
	         	sqlJoin.append(" inner join rentarea as ra on b.id = ra.buildingid ");
	      }
	   // type building
		if (request.getRentTypes() != null &&  request.getRentTypes().size() > 0) {
			sqlJoin.append(" inner join buildingrenttype as br on b.id = br.buildingid \r\n" + 
								 " inner join renttype as rt on br.renttypeid = rt.id ");
		}
		// nhân viên quản lí
		if (!checkInputSearch.isNullInt(request.getStaffId())) {
			sqlJoin.append(" inner join assignmentbuilding ab on b.id = ab.buildingid inner join user as u  on ab.staffid = u.id ");
		}
	}
	
	
	private void sqlNoJoin(BuildingSearchRequest request, StringBuilder sqlNotJoin) {
		if (!checkInputSearch.isNullStr(request.getName())) {
			sqlNotJoin.append(" and b.name LIKE '%" + request.getName() + "%'");
		}
		// phường, đường, hướng, hạng
		if (!checkInputSearch.isNullStr(request.getWard())) {
			sqlNotJoin.append(" and b.ward LIKE '%" + request.getWard() + "%'");
		}
		if (!checkInputSearch.isNullStr(request.getStreet())) {
			sqlNotJoin.append(" and b.street LIKE '%" + request.getStreet() + "%'");
		}
		if (!checkInputSearch.isNullStr(request.getDirection())) {
			sqlNotJoin.append(" and b.direction LIKE '%" + request.getDirection() + "%'");
		}
		if (!checkInputSearch.isNullStr(request.getLevel())) {
			sqlNotJoin.append(" and b.level LIKE '%" + request.getLevel() + "%'");
		}
		// Search chính xác
		// floorarea
		if (!checkInputSearch.isNullStr(request.getFloorArea())) {
			sqlNotJoin.append(" and b.floorarea = " + request.getFloorArea() + "");
		}
		// numberOfBasement
		if (!checkInputSearch.isNullInt(request.getNumberOfBasement())) {
			sqlNotJoin.append(" and b.numberofbasement = " + request.getNumberOfBasement() + "");
		}
		// Search From - To
		// Search form and to of rentArea (diện tích thuê)
		if (!checkInputSearch.isNullInt(request.getRentAreaFrom())) {
			sqlNotJoin.append(" and ra.value <= " + request.getRentAreaFrom() + "");
		}
		if (!checkInputSearch.isNullInt(request.getRentAreaTo())) {
			sqlNotJoin.append(" and ra.value >= " + request.getRentAreaTo() + "");
		}
		// Search form and to of rentprice (giá thuê from -> to)
		if (!checkInputSearch.isNullInt(request.getRentPriceFrom())) {
			sqlNotJoin.append(" and b.rentprice <= " + request.getRentPriceFrom() + "");
		}
		if (!checkInputSearch.isNullInt(request.getRentPriceTo())) {
			sqlNotJoin.append(" and b.rentprice >= " + request.getRentPriceTo() + "");
		}
		// search staff 
		if (!checkInputSearch.isNullInt(request.getStaffId())) {
			sqlNotJoin.append(" and u.id = " + request.getStaffId() + " ");
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
