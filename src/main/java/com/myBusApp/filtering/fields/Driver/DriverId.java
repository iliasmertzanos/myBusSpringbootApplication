package com.myBusApp.filtering.fields.Driver;

import java.util.function.Predicate;

import com.myBusApp.domainobject.DriverDO;
import com.myBusApp.domainvalue.RelationalOperator;
import com.myBusApp.filtering.abstractClasses.fieldtypes.NumericalType;

public class DriverId extends NumericalType{

	public DriverId(String value, RelationalOperator compateType) {
		super(value, compateType);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Predicate<DriverDO> getFilterCriteria() {
		Predicate<DriverDO> criteria=x -> true;
		int driverId=Integer.parseInt(value);
		switch(super.compateType) {
			case EQUALS:
				criteria= driver->driverId==driver.getId();
				break;
			case GREATERTHAN:
				criteria= driver->driver.getId()>driverId;
				break;
			case LESSTHAN:
				criteria= driver->driver.getId()<driverId;
				break;
			case NOTEMPTY:
				criteria= driver->driver.getId()!=0;
				break;
			default:
				break;
		}
		return criteria;
	}
	
	
	
}
