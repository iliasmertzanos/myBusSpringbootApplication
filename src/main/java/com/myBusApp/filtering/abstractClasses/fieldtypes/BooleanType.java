package com.myBusApp.filtering.abstractClasses.fieldtypes;

import java.util.Arrays;

import com.myBusApp.domainvalue.RelationalOperator;


/**
 * An abstract class for subclasses that handle the behavior of 
 * boolean fields.
 *
 * Provides a standard method to validate that a given string is a boolean value. 
 */
public abstract class BooleanType extends ValueType {

	public BooleanType(String value, RelationalOperator compateType) {
		super(value, compateType);
	}
	
	public boolean isDataValid() {
		return !"".equals(value) &&
				(
				"true".equals(value)||
				"TRUE".equals(value)||
				"false".equals(value)||
				"FALSE".equals(value)
				);
	}
	
	/**
	 * Checks if the current field can be compared using the given comparator
	 *
	 */
	@Override
	public boolean isRelationalOperatorSupported() {
		return Arrays.asList(RelationalOperator.EQUALS)
				.stream()
				.anyMatch(type->type.equals(this.compateType));
	}
	
}
