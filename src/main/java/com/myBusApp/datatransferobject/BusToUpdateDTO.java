package com.myBusApp.datatransferobject;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.myBusApp.domainvalue.EngineType;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class BusToUpdateDTO {

    @Min(0)
    private Integer seatCount;

    private Boolean convertible=null;

    @Min(0)
    @Max(5)
    private Integer rating;

    private EngineType engineType;
    
    private String manufacturer;
    
    private BusToUpdateDTO() {}

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

	@Override
	public String toString() {
		return "BusDTO [ seatCount=" + seatCount + ", convertible="
				+ convertible + ", rating=" + rating + ", engineType=" + engineType + ", manufacturer=" + manufacturer
				+"]";
	}
    
    
}
