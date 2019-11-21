package com.myBusApp.filtering.fields.Driver;

import java.util.function.Predicate;

import com.myBusApp.domainobject.DriverDO;
import com.myBusApp.domainvalue.RelationalOperator;
import com.myBusApp.filtering.abstractClasses.fieldtypes.AlphanumericType;

public class UserName  extends AlphanumericType{

	public UserName(String value, RelationalOperator compateType) {
		super(value, compateType);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Predicate<DriverDO> getFilterCriteria() {
		Predicate<DriverDO> criteria=x -> true;
		switch(compateType) {
		case EQUALS:
			criteria= driver->value.equals(driver.getUsername());
			break;
		case LIKE:
			criteria= driver->driver.getUsername().contains(value);
			break;
		case NOTEMPTY:
			criteria= driver->!"".equals(driver.getUsername());
			break;
		case NOTNULL:
			criteria= driver->driver.getUsername()!=null;
			break;
		default:
			break;    			
	}
		return criteria;
	}
	
	
	
}
