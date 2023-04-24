package com.laptrinhjavaweb.repository.impl;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.laptrinhjavaweb.builder.BuildingSearchBuilder;
import com.laptrinhjavaweb.entity.BuildingEntity1;
import com.laptrinhjavaweb.repository.BuildingRepository3;
import com.laptrinhjavaweb.util.MapUtils;
import com.laptrinhjavaweb.util.checkInputSearch;


@Repository
public class BuildingRepositoryImpl3 implements BuildingRepository3 {
	
	// cách 3: sử dụng builder - common search
	@PersistenceContext
	private EntityManager entityManager;
	
	
		@Override
		public List<BuildingEntity1> findBuilding3(BuildingSearchBuilder builder) throws SQLException {
			StringBuilder querry = new StringBuilder("SELECT * FROM building as b ");
			StringBuilder sqlJoin = new StringBuilder();
			StringBuilder sqlWhere = new StringBuilder(" where 1=1 ");
			
			buidSqlCommonUsingBuider(builder, sqlWhere);
			buidSqlSpecial(builder, sqlJoin, sqlWhere);
			
			querry.append(sqlJoin).append(sqlWhere).append(" GROUP BY b.id");
			
			Query query = entityManager.createNativeQuery(querry.toString(), BuildingEntity1.class);
			return query.getResultList();
		}


		// sử dụng java reflection
		private StringBuilder buidSqlCommonUsingBuider(BuildingSearchBuilder builder, StringBuilder sql) {
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

		private String buidSqlSpecial(BuildingSearchBuilder builder, StringBuilder sqlJoin, StringBuilder sqlWhere) {
			Integer rentAreaFrom = builder.getRentAreaFrom();
			Integer rentAreaTo = builder.getRentAreaTo();
			Integer rentPriceFrom = builder.getRentPriceFrom();
			Integer rentPriceTo = builder.getRentPriceTo();

			if (!checkInputSearch.isNullInt(rentPriceFrom)) {
				sqlJoin.append(" and b.rentprice <= " + rentPriceFrom + "");
			}
			if (!checkInputSearch.isNullInt(rentPriceTo)) {
				sqlJoin.append(" and b.rentprice >= " + rentPriceTo + "");
			}

			// rentarea
			if (!checkInputSearch.isNullInt(rentAreaFrom) || !checkInputSearch.isNullInt(rentAreaTo)) {
				sqlJoin.append(" and EXISTS (SELECT * FROM rentarea ra WHERE ra.buildingid = b.id and ");
				if (!checkInputSearch.isNullInt(rentAreaFrom)) {
					sqlJoin.append(" and ra.value >= " + rentAreaFrom + "");
				}
				if (!checkInputSearch.isNullInt(rentAreaTo)) {
					sqlJoin.append(" and ra.value <= " + rentAreaTo + "");
				}
				sqlJoin.append(")");
			}

			// search staff
			Integer staff = builder.getStaffID();
			if (staff != null) {
				sqlJoin.append(
						" inner join assignmentbuilding ab on b.id = ab.buildingid inner join user as u  on ab.staffid = u.id ");
				sqlJoin.append(" and u.id = " + staff + "");
			}
			
			// districtId
			Integer districtId = builder.getDistrict();
			if (districtId != null) {
				sqlJoin.append(" inner join district as d on b.districtid = d.id ");
				sqlWhere.append(" and d.id = '" + districtId + "'");
			}

            // types
			if (builder.getTypes() != null && !builder.getTypes().isEmpty()) {
				sqlJoin.append(" inner join buildingrenttype as br on b.id = br.buildingid \r\n"
						+ " inner join renttype as rt on br.renttypeid = rt.id ");
				sqlJoin.append(" and (");
				String types = builder.getTypes().stream().map(item -> " rt.code LIKE '%" + item + "%'").collect(Collectors.joining(" or "));
				sqlJoin.append(types);
				sqlJoin.append(")");
			}	
			return sqlJoin.toString();
		}
	}