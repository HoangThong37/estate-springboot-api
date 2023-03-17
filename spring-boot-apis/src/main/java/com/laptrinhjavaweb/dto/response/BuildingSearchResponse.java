package com.laptrinhjavaweb.dto.response;

import java.util.Date;
import java.util.List;

public class BuildingSearchResponse {
    private long id;
    private String name;
    private String address;
    private String floorArea;       // diện tích sàn
    private String emptyArea;      // diện tích trống
    private String rentCost;      // giá thuê
    private String serviceFee;   // phí dịch vụ
    private String brokerageFee;// phí môi giới
    private Integer numberOfBasement;
    private Date createdDate;
    private Date modifiedDate;
    private String createdBy;
    private String modifiedBy;
	private List<String> rentArea; // đẩy ra dạng value1,value2
  //  private String rentArea;


	public long getId() {
		return id;
	}
	public List<String> getRentArea() {
		return rentArea;
	}
	public void setRentArea(List<String> rentArea) {
		this.rentArea = rentArea;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}

	public String getFloorArea() {
		return floorArea;
	}
	public void setFloorArea(String floorArea) {
		this.floorArea = floorArea;
	}
	public String getEmptyArea() {
		return emptyArea;
	}
	public void setEmptyArea(String emptyArea) {
		this.emptyArea = emptyArea;
	}
	public String getRentCost() {
		return rentCost;
	}
	public void setRentCost(String rentCost) {
		this.rentCost = rentCost;
	}
	public String getServiceFee() {
		return serviceFee;
	}
	public void setServiceFee(String serviceFee) {
		this.serviceFee = serviceFee;
	}
	public String getBrokerageFee() {
		return brokerageFee;
	}
	public void setBrokerageFee(String brokerageFee) {
		this.brokerageFee = brokerageFee;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public Date getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	public Integer getNumberOfBasement() {
		return numberOfBasement;
	}
	public void setNumberOfBasement(Integer numberOfBasement) {
		this.numberOfBasement = numberOfBasement;
	}
	
	
}
