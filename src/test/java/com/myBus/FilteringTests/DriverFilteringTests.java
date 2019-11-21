package com.myBus.FilteringTests;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.myBusApp.MyBusApplication;
import com.myBusApp.domainobject.BusDO;
import com.myBusApp.domainobject.DriverDO;
import com.myBusApp.domainvalue.BusAttributeField;
import com.myBusApp.domainvalue.DriverAttributeField;
import com.myBusApp.domainvalue.EngineType;
import com.myBusApp.domainvalue.RelationalOperator;
import com.myBusApp.filtering.BusSingleCriteria;
import com.myBusApp.filtering.DriverSingleCriteria;

import java.util.function.Predicate;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = MyBusApplication.class,webEnvironment=WebEnvironment.RANDOM_PORT)
public class DriverFilteringTests {
	
	private static List<DriverDO> driverList = new ArrayList<DriverDO>();
	
	@BeforeClass
	public static void initlizeDriverList() {
		driverList.add(new DriverDO( "driver12","pass12"));
		driverList.add(new DriverDO( "driver13","pass13"));
		driverList.add(new DriverDO( "driver14","pass14"));
		driverList.add(new DriverDO( "driver15","pass15"));
		driverList.add(new DriverDO( "driver16","pass16"));
	}
	
	//Testing the functionality of the basic filter classes given a list of drivers	
	@Test
	public void testFilteringMethodForDrivers() {	
		
		//Filtering given a name contains a char "driver1"
		List<Predicate<DriverDO>> driverCriteriaList=Arrays.asList(
				DriverSingleCriteria.newBuilder().setField(DriverAttributeField.USER_NAME)
				.setRelationalOperato(RelationalOperator.LIKE)
				.setValue("driver1").createDriverSingleCriteria().getDriverFilterCriteria()
				);	
		
	    List<DriverDO> result = driverList.stream()
	      .filter(driverCriteriaList.stream().reduce(x->true, Predicate::and))
	      .collect(Collectors.toList());   
	    
	    assertTrue(result.size()==5);    
	    
	    //Filtering given the attribute deleted is false
		driverCriteriaList=Arrays.asList(
				DriverSingleCriteria.newBuilder().setField(DriverAttributeField.DELETED)
				.setRelationalOperato(RelationalOperator.EQUALS)
				.setValue("false").createDriverSingleCriteria().getDriverFilterCriteria()
				);	
		
		//set 2 drivers to be deleted
		driverList.get(0).setDeleted(true);
		driverList.get(2).setDeleted(true);
		
		result = driverList.stream()
			      .filter(driverCriteriaList.stream().reduce(x->true, Predicate::and))
			      .collect(Collectors.toList()); 
		
		//we are expecting to find only three since by default deleted is set false
		Assert.assertTrue(result.size()==3);   		
	    
	}
	
	//Testing the functionality of the basic filter classes given a list of drivers	
	@Test
	public void testFilteringMethodForDriversWithBus() {	
		
		//Lets select some bus
		driverList.get(0).setBusDetails(new BusDO("AS-SD0912", 4, EngineType.ELECTRIC));
		driverList.get(1).setBusDetails(new BusDO("AS-RFBNHT", 4, EngineType.PETROL));
		driverList.get(2).setBusDetails(new BusDO("AS-123456", 4, EngineType.HYBRID));
		driverList.get(3).setBusDetails(new BusDO("BC-SD0912", 4, EngineType.HYBRID));
		driverList.get(4).setBusDetails(new BusDO("TZ-SD0934", 4, EngineType.GAS));
		
		//Get drivers that have selected bus with hybrid egine
		List<Predicate<DriverDO>> driverCriteriaList=Arrays.asList(
				BusSingleCriteria.newBuilder().setField(BusAttributeField.CAR_ENGINE)
				.setRelationalOperato(RelationalOperator.EQUALS)
				.setValue("HYBRID").createBusSingleCriteria().getBusFilterCriteria()
				);	
		
	    List<DriverDO> result = driverList.stream()
	      .filter(driverCriteriaList.stream().reduce(x->true, Predicate::and))
	      .collect(Collectors.toList());   
	    
	    assertTrue(result.size()==2);    
	    
	}
	
		
	
	
	
}
