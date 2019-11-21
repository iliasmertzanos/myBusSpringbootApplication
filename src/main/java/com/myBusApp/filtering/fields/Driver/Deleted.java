package com.myBusApp.filtering.fields.Driver;

import java.util.function.Predicate;

import com.myBusApp.domainobject.DriverDO;
import com.myBusApp.domainvalue.RelationalOperator;
import com.myBusApp.filtering.abstractClasses.fieldtypes.BooleanType;

public class Deleted extends BooleanType {

	public Deleted(String value, RelationalOperator compateType) {
		super(value, compateType);
	}

	@Override
	public Predicate<DriverDO> getFilterCriteria() {
		Predicate<DriverDO> criteria=driver->Boolean.valueOf(value)==driver.getDeleted();
		return criteria;
	}	
}
