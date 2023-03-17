package com.laptrinhjavaweb.dto;

import java.util.List;

public class RentAreaDTO {
	
	private Integer id;
	private List<Integer> value;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public List<Integer> getValue() {
		return value;
	}
	public void setValue(List<Integer> value) {
		this.value = value;
	}

}
