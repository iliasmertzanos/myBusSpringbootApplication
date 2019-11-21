package com.myBusApp.filtering.fields.Bus;

import java.util.function.Predicate;

import org.apache.commons.lang3.EnumUtils;

import com.myBusApp.domainobject.DriverDO;
import com.myBusApp.domainvalue.EngineType;
import com.myBusApp.domainvalue.RelationalOperator;
import com.myBusApp.filtering.abstractClasses.fieldtypes.EnumType;

public class EngineTypeField extends EnumType{

	public EngineTypeField(String value, RelationalOperator compateType) {
		super(value, compateType);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean isDataValid() {
		return EnumUtils.isValidEnum(EngineType.class, value);
	}

	@Override
	public Predicate<DriverDO> getFilterCriteria() {
		Predicate<DriverDO> criteria= driver->
		driver.getBusDetails().
			getEngineType().
			compareTo(EnumUtils.getEnum(EngineType.class, value))==0;
		return criteria;
	}

}
