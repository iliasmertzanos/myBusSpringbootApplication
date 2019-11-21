package com.myBusApp.filtering.fields.Bus;

import java.util.function.Predicate;

import com.myBusApp.domainobject.DriverDO;
import com.myBusApp.domainvalue.RelationalOperator;
import com.myBusApp.filtering.abstractClasses.fieldtypes.BooleanType;

public class Convertible extends BooleanType {

	public Convertible(String value, RelationalOperator compateType) {
		super(value, compateType);
	}

	@Override
	public Predicate<DriverDO> getFilterCriteria() {
		return driver->Boolean.valueOf(value)==driver.getBusDetails().getConvertible();
	}	
}
