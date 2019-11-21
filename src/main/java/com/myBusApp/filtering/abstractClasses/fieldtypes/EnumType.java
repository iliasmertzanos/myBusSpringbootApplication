package com.myBusApp.filtering.abstractClasses.fieldtypes;

import java.util.Arrays;

import com.myBusApp.domainvalue.RelationalOperator;


/**
 * An abstract class for subclasses that handle the behavior of 
 * Enum fields.
 *
 * Since there are a lot of ENUM fields in BusDO and DriverDO the subclass must implement the method isDataValid() 
 */
public abstract class EnumType extends ValueType {
	
	public EnumType(String value, RelationalOperator compateType) {
		super(value, compateType);
	}
	
	public abstract boolean isDataValid();
	
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
