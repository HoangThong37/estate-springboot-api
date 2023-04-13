package com.laptrinhjavaweb.repository.impl;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.laptrinhjavaweb.builder.BuildingSearchBuilder;
import com.laptrinhjavaweb.entity.BuildingEntity;
import com.laptrinhjavaweb.repository.BuildingRepository2;
import com.laptrinhjavaweb.util.MapUtils;
import com.laptrinhjavaweb.util.checkInputSearch;

@Repository
public class BuildingRepositoryImpl2 implements BuildingRepository2 {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public List<BuildingEntity> findBuilding2(Map<String, Object> request, List<String> types) throws SQLException {
		// cách 2: sử dụng generic viết hàm chung
		StringBuilder querry = new StringBuilder("SELECT * FROM building as b ");
		StringBuilder sqlJoin = new StringBuilder();
		StringBuilder sqlWhere = new StringBuilder(" where 1=1 ");

		sqlWithJoin2(request, sqlJoin, sqlWhere, types); // join table
		sqlNoJoin2(request, sqlWhere);

		querry.append(sqlJoin).append(sqlWhere).append(" GROUP BY b.id");

		Query query = entityManager.createNativeQuery(querry.toString(), BuildingEntity.class);
		return query.getResultList();
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

		Integer rentPriceFrom = MapUtils.getObject(request, "rentPriceFrom", Integer.class); //
		if (!checkInputSearch.isNullInt(rentPriceFrom)) {
			sqlWhere.append(" and b.rentprice <= " + rentPriceFrom + "");
		}

		Integer rentPriceTo = MapUtils.getObject(request, "rentPriceTo", Integer.class); //
		if (!checkInputSearch.isNullInt(rentPriceTo)) {
			sqlWhere.append(" and b.rentprice >= " + rentPriceTo + "");
		}
	}

	// update c2
	private void sqlWithJoin2(Map<String, Object> request, StringBuilder sqlJoin, StringBuilder sqlWhere,
			List<String> types) {
		// join rentare (diện tích thuê)
		String rentAreaFrom = MapUtils.getObject(request, "rentAreaFrom", String.class);
		String rentAreaTo = MapUtils.getObject(request, "rentAreaTo", String.class);

		if (!checkInputSearch.isNullStr(rentAreaFrom) || !checkInputSearch.isNullStr(rentAreaTo)) {
			sqlJoin.append(" and EXISTS (SELECT * FROM rentarea ra WHERE ra.buildingid = b.id and ");
			if (!checkInputSearch.isNullStr(rentAreaFrom)) {
				sqlWhere.append(" and ra.value >= " + rentAreaFrom + "");
			}
			if (!checkInputSearch.isNullStr(rentAreaTo)) {
				sqlWhere.append(" and ra.value <= " + rentAreaTo + "");
			}
			sqlJoin.append(")");
		}

		// districtId
		Integer districtId = MapUtils.getObject(request, "districtId", Integer.class);
		if (!checkInputSearch.isNullInt(districtId)) {
			sqlJoin.append(" inner join district as d on b.districtid = d.id ");
			sqlWhere.append(" and d.code = '" + districtId + "'");
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
			sqlWhere.append(" and u.id = " + staff + " ");
		}
	}
	

	// cách 3: sử dụng builder - common search
	@Override
	public List<BuildingEntity> findBuilding3(BuildingSearchBuilder builder) {
		StringBuilder sql = new StringBuilder("SELECT * FROM building as b");
		buidSqlSpecial(builder, sql);
		sql.append(" where 1=1 ");
		buidSqlCommonUsingBuider(builder, sql);
		sql.append(" GROUP BY b.id");

		Query query = entityManager.createNativeQuery(sql.toString(), BuildingEntity.class);
 		return query.getResultList();

	}

	// sử dụng java reflection
	private StringBuilder buidSqlCommonUsingBuider(BuildingSearchBuilder builder, StringBuilder sql) {
		//StringBuilder sql = new StringBuilder("");
		try {
			Field fields[] = BuildingSearchBuilder.class.getDeclaredFields();
			for (Field field : fields) {
				field.setAccessible(true);
				String fieldName = field.getName();
				if (!fieldName.equals("types") && !fieldName.startsWith("rentarea") && !fieldName.equals("districtId")
						&& !fieldName.equals("staffid") && !fieldName.startsWith("costrent")) {

					Object objectValue = field.get(builder);
					if (objectValue != null) {
						if (field.getType().getName().equals("java.lang.String")) { // tìm kiếm theo like
							sql.append(" and b." + fieldName.toLowerCase() + " like '%" + objectValue + "%'");
						} else if (field.getType().getName().equals("java.lang.Integer")) { // tìm kiếm chính xác
							sql.append(" and b." + fieldName.toLowerCase() + " = " + objectValue + "");
						}
					}
				}
			}
		} catch (Exception e) {
		}
		return sql;
	}

	private String buidSqlSpecial(BuildingSearchBuilder builder, StringBuilder sql) {
        Integer rentAreaFrom = builder.getAreaRentFrom();
        Integer rentAreaTo = builder.getAreaRentTo();
        Integer rentPriceFrom = builder.getCostRentFrom();
        Integer rentPriceTo = builder.getCostRentTo();
        
		if (!checkInputSearch.isNullInt(rentPriceFrom)) {
			sql.append(" and b.rentprice <= " + rentPriceFrom + "");
		}
		if (!checkInputSearch.isNullInt(rentPriceTo)) {
			sql.append(" and b.rentprice >= " + rentPriceTo + "");
		}

		// rentarea
		if (!checkInputSearch.isNullInt(rentAreaFrom) || !checkInputSearch.isNullInt(rentAreaTo)) {
			sql.append(" and EXISTS (SELECT * FROM rentarea ra WHERE ra.buildingid = b.id and ");
			if (!checkInputSearch.isNullInt(rentAreaFrom)) {
				sql.append(" and ra.value >= " + rentAreaFrom + "");
			}
			if (!checkInputSearch.isNullInt(rentAreaTo)) {
				sql.append(" and ra.value <= " + rentAreaTo + "");
			}
			sql.append(")");
		}

		Integer staff = builder.getStaffId();
		if (staff != null) {
			sql.append("  inner join assignmentbuilding as ab on b.id = ab.buildingid ");
		}
		
		// districtId
		Integer districtId = builder.getDistrict();
		if (!checkInputSearch.isNullInt(districtId)) {
			sql.append(" inner join district as d on b.districtid = d.id ");
			sql.append(" and d.code = '" + districtId + "'");
		}
		
		String[] types = builder.getBuildingTypes();
		if (types != null && types.length > 0) {
			List<String> buildingTypes = new ArrayList<>();
			sql.append(" and (");
			 buildingTypes.stream().map(item -> " rt.code = '" + item + "'").collect(Collectors.joining(" or "));
     		sql.append(buildingTypes);
			sql.append(")");
		}
		return sql.toString();
	}

}
