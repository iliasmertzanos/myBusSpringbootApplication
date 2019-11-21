package com.myBusApp.filtering.fields.Bus;

import java.util.function.Predicate;

import com.myBusApp.domainobject.DriverDO;
import com.myBusApp.domainvalue.RelationalOperator;
import com.myBusApp.filtering.abstractClasses.fieldtypes.AlphanumericType;

public class BusManufacturer extends AlphanumericType{

	public BusManufacturer(String value, RelationalOperator compateType) {
		super(value, compateType);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Predicate<DriverDO> getFilterCriteria() {
		Predicate<DriverDO> criteria=x -> true;
		switch(compateType) {
			case EQUALS:
				criteria= driver->value.equals(driver.getBusDetails().getManufacturer());
				break;
			case LIKE:
				criteria= driver->driver.getBusDetails().getManufacturer().contains(value);
				break;
			case NOTEMPTY:
				criteria= driver->!"".equals(driver.getBusDetails().getManufacturer());
				break;
			case NOTNULL:
				criteria= driver->driver.getBusDetails().getManufacturer()!=null;
				break;
			default:
				break;    			
		}
		return criteria;
	}
	
	

}
