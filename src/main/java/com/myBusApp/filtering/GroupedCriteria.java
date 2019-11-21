package com.myBusApp.filtering;



/**
 * This class is a Wrapper to encapsulates the objects BusCriteriaGroup and DriverCriteriaGroup
 * that  are parsed over the PostMapping method in the DriverController.
 *
 */
public class GroupedCriteria {
	
	BusCriteriaGroup myBusCriteria;
	
	DriverCriteriaGroup myDriverCriteria;

	public GroupedCriteria(BusCriteriaGroup myBusCriteria, DriverCriteriaGroup myDriverCriteria) {
		this.myBusCriteria = myBusCriteria;
		this.myDriverCriteria = myDriverCriteria;
	}

	public BusCriteriaGroup getMyBusCriteria() {
		return myBusCriteria;
	}

	public void setMyBusCriteria(BusCriteriaGroup myBusCriteria) {
		this.myBusCriteria = myBusCriteria;
	}

	public DriverCriteriaGroup getMyDriverCriteria() {
		return myDriverCriteria;
	}

	public void setMyDriverCriteria(DriverCriteriaGroup myDriverCriteria) {
		this.myDriverCriteria = myDriverCriteria;
	}
	
	
	
}
