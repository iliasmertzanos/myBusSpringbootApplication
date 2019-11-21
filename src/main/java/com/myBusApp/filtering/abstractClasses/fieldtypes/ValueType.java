package com.myBusApp.filtering.abstractClasses.fieldtypes;

import java.util.function.Predicate;

import com.myBusApp.domainobject.DriverDO;
import com.myBusApp.domainvalue.RelationalOperator;

public abstract class ValueType  {
	
	protected String value;
	
	protected RelationalOperator compateType;
	
	public ValueType(String value, RelationalOperator compateType) {
		this.value = value;
		this.compateType = compateType;
	}

	protected String getValue() {
		return value;
	}

	protected void setValue(String value) {
		this.value = value;
	}
	
	protected RelationalOperator getCompateTyp() {
		return compateType;
	}

	protected void setCompateType(RelationalOperator compateType) {
		this.compateType = compateType;
	}

	public abstract Predicate<DriverDO> getFilterCriteria() ;
	
	public abstract boolean isDataValid();
	
	public abstract boolean isRelationalOperatorSupported(); 
	
}
