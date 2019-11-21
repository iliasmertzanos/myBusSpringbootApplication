package com.myBusApp.domainobject;

import java.time.ZonedDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import com.myBusApp.domainvalue.EngineType;

@Entity
@Table(
    name = "bus",
    uniqueConstraints = @UniqueConstraint(name = "uc_license_plate", columnNames = {"licenseplate"})
)
public class BusDO
{

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private ZonedDateTime dateCreated = ZonedDateTime.now();

    @Column(nullable = false)
    @NotNull(message = "License plate can not be null!")
    private String licenseplate;
    
    @Column(nullable = false)
    private Boolean convertible=false;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Engine type can not be null!")
    private EngineType engineType;
    
    @Column(nullable = false)
    @NotNull(message = "Number of seats can not be null!")
    private Integer seatcount;
    
    @Column(columnDefinition = "integer default 0")
    private Integer rating;
    
    @Column(columnDefinition = "varchar(255) default ''")
    private String manufacturer;
    
    @OneToOne(mappedBy="busDetails")
	private DriverDO driverDetails;
    
    private BusDO()
    {
    }    

	public BusDO(String licenseplate,
			Integer seatcount, EngineType engineType) {
		this.licenseplate = licenseplate;
		this.engineType = engineType;
		this.seatcount = seatcount;
		this.rating=0;
		this.convertible=false;
		this.manufacturer="";
	}



	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ZonedDateTime getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(ZonedDateTime dateCreated) {
		this.dateCreated = dateCreated;
	}

	public String getLicenseplate() {
		return licenseplate;
	}

	public void setLicenseplate(String licenseplate) {
		this.licenseplate = licenseplate;
	}
	
	public EngineType getEngineType() {
		return engineType;
	}

	public void setEngineType(EngineType engineType) {
		this.engineType = engineType;
	}

	public Integer getSeatcount() {
		return seatcount;
	}

	public void setSeatcount(Integer seatcount) {
		this.seatcount = seatcount;
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

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public DriverDO getDriverDetails() {
		return driverDetails;
	}

	public void setDriverDetails(DriverDO driverDetails) {
		this.driverDetails = driverDetails;
	}

	@Override
	public String toString() {
		return "BusDO [id=" + id + ", dateCreated=" + dateCreated + ", licenseplate=" + licenseplate
				+ ", convertible=" + convertible + ", engineType=" + engineType + ", seatcount=" + seatcount
				+ ", rating=" + rating + "]";
	}
	
	
}
