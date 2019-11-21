package com.myBusApp.filtering.fields.Bus;

import java.util.function.Predicate;

import com.myBusApp.domainobject.DriverDO;
import com.myBusApp.domainvalue.RelationalOperator;
import com.myBusApp.filtering.abstractClasses.fieldtypes.NumericalType;

public class Rating extends NumericalType{

	public Rating(String value, RelationalOperator compateType) {
		super(value, compateType);
	}

	@Override
	public Predicate<DriverDO> getFilterCriteria() {
		Predicate<DriverDO> criteria=x -> true;
		int rating=Integer.parseInt(value);
		switch(compateType) {
			case EQUALS:
				criteria= driver->rating==driver.getBusDetails().getRating();
				break;
			case GREATERTHAN:
				criteria= driver->driver.getBusDetails().getRating()>rating;
				break;
			case LESSTHAN:
				criteria= driver->driver.getBusDetails().getRating()<rating;
				break;
			case NOTEMPTY:
				criteria= driver->driver.getBusDetails().getRating()!=0;
				break;
			default:
				break;
		}
		return criteria;
	}
	
	

}
