package com.myBusApp.filtering.fields.Driver;

import java.util.function.Predicate;

import org.apache.commons.lang3.EnumUtils;

import com.myBusApp.domainobject.DriverDO;
import com.myBusApp.domainvalue.OnlineStatus;
import com.myBusApp.domainvalue.RelationalOperator;
import com.myBusApp.filtering.abstractClasses.fieldtypes.EnumType;
public class OnlineStatusField extends EnumType {

	public OnlineStatusField(String value, RelationalOperator compateType) {
		super(value, compateType);
	}

	@Override
	public Predicate<DriverDO> getFilterCriteria() {
		Predicate<DriverDO> criteria=driver->driver.getOnlineStatus().compareTo(EnumUtils.getEnum(OnlineStatus.class, value))==0;
		
		return criteria;
	}

	@Override
	public boolean isDataValid() {
		return EnumUtils.isValidEnum(OnlineStatus.class, value);
	}
	
	
	
}
