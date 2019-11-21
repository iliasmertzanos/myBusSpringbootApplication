package com.myBusApp.datatransferobject;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.myBusApp.domainvalue.EngineType;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class BusDTO {
	
	@JsonIgnore
    private long id;

    @NotNull(message = "License plate can not be null!")
    @Size(min=9, max=9)
    private String licensePlate;

    @Min(0)
    private int seatCount=0;

    private boolean convertible=false;

    @Min(0)
    @Max(5)
    private int rating=0;

    @NotNull(message = "Engine type must be defined!")
    private EngineType engineType;
    
    private String manufacturer="";
    
    @JsonIgnore
    private DriverDTO driverDetails;
    
    private BusDTO() {}

    public BusDTO(String licensePlate, int seatCount, boolean convertible, int rating, EngineType engineType, String manufacturer) {
        this.licensePlate = licensePlate;
        this.seatCount = seatCount;
        this.convertible = convertible;
        this.rating = rating;
        this.engineType = engineType;
        this.manufacturer = manufacturer;
    } 
    
    public BusDTO(String licensePlate,EngineType engineType) {
        this.licensePlate = licensePlate;
        this.engineType = engineType;
    } 

    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLicensePlate() {
		return licensePlate;
	}

	public void setLicensePlate(String licensePlate) {
		this.licensePlate = licensePlate;
	}

	public Integer getSeatCount() {
		return seatCount;
	}

	public void setSeatCount(Integer seatCount) {
		this.seatCount = seatCount;
	}

	public Boolean getConvertible() {
		return convertible;
	}

	public void setConvertible(Boolean convertible) {
		this.convertible = convertible;
	}

	public Integer getRating() {
		return rating;
	}

	public void setRating(Integer rating) {
		this.rating = rating;
	}

	public EngineType getEngineType() {
		return engineType;
	}

	public void setEngineType(EngineType engineType) {
		this.engineType = engineType;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public DriverDTO getDriverDetails() {
		return driverDetails;
	}

	public void setDriverDetails(DriverDTO driverDetails) {
		this.driverDetails = driverDetails;
	}

	@Override
	public String toString() {
		return "BusDTO [id=" + id + ", licensePlate=" + licensePlate + ", seatCount=" + seatCount + ", convertible="
				+ convertible + ", rating=" + rating + ", engineType=" + engineType + ", manufacturer=" + manufacturer
				+ ", driverDetails=" + driverDetails + "]";
	}
    
    
}
