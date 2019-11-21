package com.myBusApp.filtering;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.commons.lang3.EnumUtils;

import com.myBusApp.domainobject.DriverDO;
import com.myBusApp.domainvalue.LogicalOperator;

/**
 * This class encapsulates a list of multiple criteria to filter a list of drivers according to their attributes .<br>
 * The parameter <code>LogicalOperator driverLogicalOperator</code> defines if the filtering should be done via conjunction (and) or disjunction (or).<br>
 *
 */
public class DriverCriteriaGroup {
	
	private List<DriverSingleCriteria> driverCriteriaList= new ArrayList<DriverSingleCriteria>();
	
	private LogicalOperator driverLogicalOperator;

	public DriverCriteriaGroup(List<DriverSingleCriteria> driverCriteriaList,
			LogicalOperator driverLogicalOperator) {
		this.driverCriteriaList = driverCriteriaList;
		this.driverLogicalOperator=driverLogicalOperator;
	}

	public List<DriverSingleCriteria> getDriverCriteriaList() {
		return driverCriteriaList;
	}

	public void setDriverCriteriaList(List<DriverSingleCriteria> driverCriteriaList) {
		this.driverCriteriaList = driverCriteriaList;
	}

	public LogicalOperator getDriverLogicalOperator() {
		return driverLogicalOperator;
	}

	public void setDriverLogicalOperator(LogicalOperator driverLogicalOperator) {
		this.driverLogicalOperator = driverLogicalOperator;
	}
	
	public Predicate<DriverDO> generateDriverPredicatesList() {
		List<Predicate<DriverDO>> myPredicateList =driverCriteriaList
				.stream()
				.map(driverSingleCriteria->driverSingleCriteria.getDriverFilterCriteria())
				.collect(Collectors.toList());
		if(this.driverLogicalOperator==LogicalOperator.AND) {
			return myPredicateList.stream().reduce(x->true, Predicate::and);
		}else {
			return myPredicateList.stream().reduce(x->false, Predicate::or);
		}
	}

	/**
	 * Method for web service layer to check validity of parsed criteria type
	 *
	 */
	public boolean isCriteriaTypeValid(String myType) {
		return myType!=null && EnumUtils.isValidEnum(LogicalOperator.class, myType);
	}
	
	
}
