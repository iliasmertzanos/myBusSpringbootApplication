package com.myBusApp.filtering.abstractClasses.fieldtypes;

import java.util.Arrays;

import com.myBusApp.domainvalue.RelationalOperator;


/**
 * An abstract class for subclasses that handle the behavior of 
 * Long, Integer etc. fields.
 *
 */
public abstract class NumericalType extends ValueType  {
	
	public NumericalType(String value, RelationalOperator compateType) {
		super(value, compateType);
		// TODO Auto-generated constructor stub
	}
	
	public boolean isDataValid() {
		return value.matches("-?(0|[1-9]\\d*)");
	}
	
	
	/**
	 * Checks if the current field can be compared using the given comparator
	 *
	 */
	@Override
	public boolean isRelationalOperatorSupported() {
		return !Arrays.asList(RelationalOperator.NOTNULL,RelationalOperator.LIKE)
				.stream()
				.anyMatch(type->type.equals(this.compateType));
	}
	
}
