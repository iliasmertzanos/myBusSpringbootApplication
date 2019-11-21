package com.myBusApp.filtering.abstractClasses.fieldtypes;

import java.util.Arrays;

import com.myBusApp.domainvalue.RelationalOperator;



/**
 * An abstract class for subclasses that handle the behavior of 
 * string fields
 *
 */
public abstract class AlphanumericType  extends ValueType {	
	
	public AlphanumericType(String value, RelationalOperator compateType) {
		super(value, compateType);
	}
	
	public boolean isDataValid() {
		return true;
	}
	
	/**
	 * Checks if the current field can be compared using the given comparator
	 *
	 */
	@Override
	public boolean isRelationalOperatorSupported() {
		return !Arrays.asList(RelationalOperator.GREATERTHAN,RelationalOperator.LESSTHAN)
				.stream()
				.anyMatch(type->type.equals(this.compateType));
	}
	
}
