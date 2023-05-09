package com.laptrinhjavaweb.repository.impl;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import com.laptrinhjavaweb.builder.BuildingSearchBuilder;
import com.laptrinhjavaweb.entity.BuildingEntity;
import com.laptrinhjavaweb.repository.BuildingRepository;
import com.laptrinhjavaweb.util.ValidateUtils;
import com.laptrinhjavaweb.util.checkInputSearch;

@Repository
public class BuildingRepositoryImpl implements BuildingRepository {

	// cách 3: sử dụng builder - common search
	 @PersistenceContext
	    private EntityManager entityManager;

	    @Override
	    public List<BuildingEntity> findBuilding(BuildingSearchBuilder builder) {
	        try {
	            StringBuilder sql = new StringBuilder("SELECT * from building as b ");
	            sql = buildingJoinQuerry(builder, sql);
	            sql.append(" where 1 = 1 ");
	            sql = buildingSqlPart1WithBuilder(builder, sql);
	            sql = buildingSqlPart2WithBuilder(builder, sql);
	            sql.append(" group by b.id");
	            Query query = entityManager.createNativeQuery(sql.toString(), BuildingEntity.class);
	            return query.getResultList();
	        }
	        catch (Exception e) {
	            e.printStackTrace();
	            return new ArrayList<>();
	        }
	    }

	    private StringBuilder buildingSqlPart2WithBuilder(BuildingSearchBuilder builder, StringBuilder sql) {
	        // rentare from
	        if (ValidateUtils.isValid(builder.getRentAreaFrom())) {
	            sql.append(" and EXISTS (select * from rentarea as ra where b.id = ra.building_id and ra.value >= " + builder.getRentAreaFrom() + ")");
	        }
	        // rentare to
	        if (ValidateUtils.isValid(builder.getRentAreaTo())) {
	            sql.append(" and EXISTS (select * from rentarea as ra where b.id = ra.building_id and ra.value <= " + builder.getRentAreaTo() + ")");
	        }
	        // rentprice from
	        if (ValidateUtils.isValid(builder.getRentPriceFrom())) {
	            sql.append(" and b.rentprice >= " + builder.getRentPriceFrom());
	        }
	        if (ValidateUtils.isValid(builder.getRentPriceTo())) {
	            sql.append(" and b.rentprice <= " + builder.getRentPriceTo());
	        }
	        // types
	        if (builder.getTypes() != null && builder.getTypes().size() > 0) {
	            sql.append(" and (");
	            String types = builder.getTypes().stream().map(item -> (" b.type like '%" + item + "%'")).collect(Collectors.joining(" or "));
	            sql.append(types);
	            sql.append(" )");
	        }
	        if (ValidateUtils.isValid(builder.getStaffID())) {
	            sql.append(" and u.id = " + builder.getStaffID());
	        }
	        return sql;
	    }



	    private StringBuilder buildingJoinQuerry(BuildingSearchBuilder builder, StringBuilder sql) {
	        if (ValidateUtils.isValid(builder.getRentAreaFrom()) || ValidateUtils.isValid(builder.getRentAreaFrom())) {
	            sql.append(" inner join rentarea as ra on ra.id = b.id");
	        }
	        if (ValidateUtils.isValid(builder.getStaffID())) {
	            sql.append( " inner join assignmentbuilding as ab on ab.building_id = b.id inner join user as u on ab.user_id = u.id ");
	        }
	        return sql;
	    }

	    // sử dụng java reflection
	    private StringBuilder buildingSqlPart1WithBuilder(BuildingSearchBuilder builder, StringBuilder sql) {
	        Field[] fields = BuildingSearchBuilder.class.getDeclaredFields();
	        try {
	            for (Field field : fields) {
	                field.setAccessible(true);
	                String fieldName = field.getName();
	                if (!fieldName.equals("types") && !fieldName.startsWith("rentArea")
	                        && !fieldName.equals("staffID")
	                        && !fieldName.startsWith("rentPrice")) {
	                    // getValue
	                    Object objectValue = field.get(builder);
	                    if (objectValue != null && objectValue != "") {
	                        if (objectValue instanceof String) {
	                            sql.append(" and b." + fieldName.toLowerCase() + " like '%" + objectValue + "%'");

	                        } else if (objectValue instanceof Integer) {
	                            sql.append(" and b." + fieldName.toLowerCase() + " = " + objectValue + "");
	                        }
	                    }
	                }
	            }
	        } catch (IllegalAccessException e) {
	            e.printStackTrace();
	        }

	        return sql;
	    }

}