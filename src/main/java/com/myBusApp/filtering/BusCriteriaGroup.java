package com.myBusApp.filtering;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.commons.lang3.EnumUtils;

import com.myBusApp.domainobject.DriverDO;
import com.myBusApp.domainvalue.LogicalOperator;

/**
 * This class encapsulates a list of multiple criteria to filter a list of drivers<br>
 * according to the attributes of the bus they have selected.<br>
 * The parameter <code>LogicalOperator driverLogicalOperator </code> defines if the filtering should be done via conjunction (and) or disjunction (or).<br>
 */
public class BusCriteriaGroup {
	
	private List<BusSingleCriteria> busCriteriaList=new ArrayList<BusSingleCriteria>();
	
	private LogicalOperator busLogicalOperator;

	public BusCriteriaGroup(List<BusSingleCriteria> busCriteriaList, 
			LogicalOperator busLogicalOperator) {
		this.busCriteriaList = busCriteriaList;
		this.busLogicalOperator = busLogicalOperator;
	}

	public List<BusSingleCriteria> getBusCriteriaList() {
		return busCriteriaList;
	}

	public void setBusCriteriaList(List<BusSingleCriteria> busCriteriaList) {
		this.busCriteriaList = busCriteriaList;
	}
	
	public LogicalOperator getBusLogicalOperator() {
		return busLogicalOperator;
	}

	public void setBusLogicalOperator(LogicalOperator busLogicalOperator) {
		this.busLogicalOperator = busLogicalOperator;
	}
	
	public Predicate<DriverDO> generateCombinedBusPredicate() {
		List<Predicate<DriverDO>> myPredictaeList =busCriteriaList
				.stream()
				.map(busSingleCriteria->busSingleCriteria.getBusFilterCriteria())
				.collect(Collectors.toList());
		if(this.busLogicalOperator==LogicalOperator.AND) {
			return myPredictaeList.stream().reduce(x->true, Predicate::and);
		}else {
			return myPredictaeList.stream().reduce(x->false, Predicate::or);
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
