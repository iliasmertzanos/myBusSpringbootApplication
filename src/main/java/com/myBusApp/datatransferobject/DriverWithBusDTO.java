package com.myBusApp.datatransferobject;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.myBusApp.domainvalue.GeoCoordinate;
import com.myBusApp.domainvalue.OnlineStatus;

import javax.validation.constraints.NotNull;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DriverWithBusDTO
{
	
    private Long id;

    @NotNull(message = "Username can not be null!")
    private String username;

    @NotNull(message = "Password can not be null!")
    private String password;

    private GeoCoordinate coordinate;
    
    private OnlineStatus onlineStatus;

    private BusDTO busDetails;

    private DriverWithBusDTO()
    {
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public GeoCoordinate getCoordinate() {
		return coordinate;
	}

	public void setCoordinate(GeoCoordinate coordinate) {
		this.coordinate = coordinate;
	}

	public BusDTO getBusDetails() {
		return busDetails;
	}

	public void setBusDetails(BusDTO busDetails) {
		this.busDetails = busDetails;
	}

	public OnlineStatus getOnlineStatus() {
		return onlineStatus;
	}

	public void setOnlineStatus(OnlineStatus onlineStatus) {
		this.onlineStatus = onlineStatus;
	}

	@Override
	public String toString() {
		return "DriverWithBusDTO [id=" + id + ", username=" + username + ", password=" + password + ", coordinate="
				+ coordinate + ", busDetails=" + busDetails + "]";
	}
	
	
}
