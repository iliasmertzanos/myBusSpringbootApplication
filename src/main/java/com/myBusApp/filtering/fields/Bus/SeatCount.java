package com.myBusApp.filtering.fields.Bus;

import java.util.function.Predicate;

import com.myBusApp.domainobject.DriverDO;
import com.myBusApp.domainvalue.RelationalOperator;
import com.myBusApp.filtering.abstractClasses.fieldtypes.NumericalType;

public class SeatCount extends NumericalType{

	public SeatCount(String value, RelationalOperator compateType) {
		super(value, compateType);
	}

	@Override
	public Predicate<DriverDO> getFilterCriteria() {
		Predicate<DriverDO> criteria=x -> true;
		int seats=Integer.parseInt(value);
		switch (compateType) {
			case EQUALS:
				criteria= driver->seats==driver.getBusDetails().getSeatcount();
				break;
			case GREATERTHAN:
				criteria= driver->driver.getBusDetails().getSeatcount()>seats;
				break;
			case LESSTHAN:
				criteria= driver->driver.getBusDetails().getSeatcount()<seats;
				break;
			case NOTEMPTY:
				criteria= driver->driver.getBusDetails().getSeatcount()!=0;
				break;
			default:
				break;
		}
		return criteria;
	}
	
	

}
