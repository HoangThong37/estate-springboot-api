package com.laptrinhjavaweb.repository.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.laptrinhjavaweb.entity.BuildingEntity;
import com.laptrinhjavaweb.repository.BuildingRepository2;
import com.laptrinhjavaweb.util.ConnectDB;
import com.laptrinhjavaweb.util.MapUtils;
import com.laptrinhjavaweb.util.checkInputSearch;

@Repository
public class BuildingRepositoryImpl2 implements BuildingRepository2 {
	
	private Connection conn = null;
	private Statement stmt;
	private ResultSet rs;

	@Override
	public List<BuildingEntity> findBuilding2(Map<String, Object> request, List<String> types) throws SQLException {
//		// cách 2: sử dụng generic viết hàm chung
		
		List<BuildingEntity> buildingEntities = new ArrayList<>();
		try {
			conn = ConnectDB.getConnection(); // connect db
			conn.setAutoCommit(false);
			if (conn != null) {
				stmt = conn.createStatement();
				StringBuilder querry = new StringBuilder("SELECT * FROM building as b ");
				StringBuilder sqlJoin = new StringBuilder();
				StringBuilder sqlWhere = new StringBuilder(" where 1=1 ");
				// StringBuilder sqlNotJoin = new StringBuilder();

				sqlWithJoin2(request, sqlJoin, sqlWhere, types); // join table
				sqlNoJoin2(request, sqlWhere);

				querry.append(sqlJoin).append(sqlWhere).append(" GROUP BY b.id");

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
//				    buildingEntity.setRentAreaId(rs.getInt("value"));

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

	private void sqlNoJoin2(Map<String, Object> request, StringBuilder sqlWhere) {
		String name = MapUtils.getObject(request, "name", String.class);
		if (!checkInputSearch.isNullStr(name)) {
			sqlWhere.append(" and b.name LIKE '%" + name + "%'");
		}

		String ward = MapUtils.getObject(request, "ward", String.class); // phường
		if (!checkInputSearch.isNullStr(ward)) {
			sqlWhere.append(" and b.ward LIKE '%" + ward + "%'");
		}

		String street = MapUtils.getObject(request, "street", String.class); // đường
		if (!checkInputSearch.isNullStr(street)) {
			sqlWhere.append(" and b.street LIKE '%" + street + "%'");
		}

		String direction = MapUtils.getObject(request, "direction", String.class); // hướng
		if (!checkInputSearch.isNullStr(direction)) {
			sqlWhere.append(" and b.direction LIKE '%" + direction + "%'");
		}

		String level = MapUtils.getObject(request, "level", String.class); // hạng
		if (!checkInputSearch.isNullStr(level)) {
			sqlWhere.append(" and b.level LIKE '%" + level + "%'");
		}

		Integer floorarea = MapUtils.getObject(request, "floorarea", Integer.class); // Search chính xác
		if (!checkInputSearch.isNullInt(floorarea)) {
			sqlWhere.append(" and b.floorarea = " + floorarea + "");
		}

		Integer numberofbasement = MapUtils.getObject(request, "numberofbasement", Integer.class); //
		if (!checkInputSearch.isNullInt(numberofbasement)) {
			sqlWhere.append(" and b.numberofbasement = " + numberofbasement + "");
		}

		Integer rentPriceFrom = MapUtils.getObject(request, "rentpricefrom", Integer.class); //
		if (!checkInputSearch.isNullInt(rentPriceFrom)) {
			sqlWhere.append(" and b.rentprice >= " + rentPriceFrom + "");
		}

		Integer rentPriceTo = MapUtils.getObject(request, "rentpriceto", Integer.class); //
		if (!checkInputSearch.isNullInt(rentPriceTo)) {
			sqlWhere.append(" and b.rentprice <= " + rentPriceTo + "");
		}
	}

	// update c2
	private void sqlWithJoin2(Map<String, Object> request, StringBuilder sqlJoin, StringBuilder sqlWhere,
			List<String> types) {
		// join rentare (diện tích thuê)
		String rentAreaFrom = MapUtils.getObject(request, "rentareafrom", String.class);
		String rentAreaTo = MapUtils.getObject(request, "rentareato", String.class);

		if (!checkInputSearch.isNullStr(rentAreaFrom) || !checkInputSearch.isNullStr(rentAreaTo)) {
			sqlWhere.append(" AND EXISTS (SELECT * FROM rentarea ra WHERE ra.buildingid = b.id ");
			if (!checkInputSearch.isNullStr(rentAreaFrom)) {
				sqlWhere.append(" and ra.value >= " + rentAreaFrom + "");
			}
			if (!checkInputSearch.isNullStr(rentAreaTo)) {
				sqlWhere.append(" and ra.value <= " + rentAreaTo + "");
			}
			sqlWhere.append(")");
		}

		// districtId
		Long districtId = MapUtils.getObject(request, "districtid", Long.class);
		if (districtId != null) {
			sqlJoin.append(" inner join district as d on b.districtid = d.id ");
			sqlWhere.append(" and d.id = '" + districtId + "'");
		}

		// type building -> list
		if (types != null && !types.isEmpty()) {
			sqlJoin.append(" inner join buildingrenttype as br on b.id = br.buildingid \r\n"
					+ " inner join renttype as rt on br.renttypeid = rt.id ");
			// xử lý types building
			// java 7

			// List<String> buildingTypes = new ArrayList<>();
			// for (String item : types) {
			// buildingTypes.add("rt.code LIKE '%" + item + "%'");
			// }
			// sqlJoin.append(" AND( " + String.join(" OR ", buildingTypes) + " )");

			// java8
			sqlJoin.append(" and (");
			String sql = types.stream().map(index -> "rt.code = '" + index + "'").collect(Collectors.joining(" or "));
			sqlJoin.append(sql);
			sqlJoin.append(")");
		}

		// search staff
		Integer staff = MapUtils.getObject(request, "staffid", Integer.class);
		if (!checkInputSearch.isNullInt(staff)) {
			sqlJoin.append(
					" inner join assignmentbuilding ab on b.id = ab.buildingid inner join user as u  on ab.staffid = u.id ");
			sqlWhere.append(" and u.id = '" + staff + "'");
		}
	}	
}
