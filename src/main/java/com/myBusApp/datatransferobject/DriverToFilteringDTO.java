package com.myBusApp.datatransferobject;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.myBusApp.domainvalue.OnlineStatus;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DriverToFilteringDTO
{
	
    private Long id;

    private String username;
    
    private Boolean deleted;
    
    private OnlineStatus onlineStatus;

    private BusDTO busDetails;

    private DriverToFilteringDTO()
    {
    }   

	public DriverToFilteringDTO(Long id, String username, Boolean deleted, OnlineStatus onlineStatus) {
		super();
		this.id = id;
		this.username = username;
		this.deleted = deleted;
		this.onlineStatus = onlineStatus;
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

	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	public OnlineStatus getOnlineStatus() {
		return onlineStatus;
	}

	public void setOnlineStatus(OnlineStatus onlineStatus) {
		this.onlineStatus = onlineStatus;
	}

	public BusDTO getBusDetails() {
		return busDetails;
	}

	public void setBusDetails(BusDTO busDetails) {
		this.busDetails = busDetails;
	}

	@Override
	public String toString() {
		return "DriverToFilteringDTO [id=" + id + ", username=" + username + ", deleted=" + deleted + ", onlineStatus="
				+ onlineStatus + ", busDetails=" + busDetails + "]";
	}
	
}
