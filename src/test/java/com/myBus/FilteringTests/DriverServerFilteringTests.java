package com.myBus.FilteringTests;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import static org.hamcrest.CoreMatchers.*;

import com.myBusApp.MyBusApplication;
import com.myBusApp.domainobject.DriverDO;
import com.myBusApp.domainvalue.BusAttributeField;
import com.myBusApp.domainvalue.DriverAttributeField;
import com.myBusApp.domainvalue.EngineType;
import com.myBusApp.domainvalue.LogicalOperator;
import com.myBusApp.domainvalue.OnlineStatus;
import com.myBusApp.domainvalue.RelationalOperator;
import com.myBusApp.filtering.BusCriteriaGroup;
import com.myBusApp.filtering.BusSingleCriteria;
import com.myBusApp.filtering.DriverCriteriaGroup;
import com.myBusApp.filtering.DriverSingleCriteria;
import com.myBusApp.service.driver.DriverService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = MyBusApplication.class,webEnvironment=WebEnvironment.RANDOM_PORT)
public class DriverServerFilteringTests {
	
	@Autowired
	private DriverService myDriverService;
	
	
	@Test
	public void testFilteringDrivers1() {
		
		
		//Create criteria for drivers that are online and their name contains the char a
		List<DriverSingleCriteria> driverCriteriaList=Arrays.asList(
				DriverSingleCriteria.newBuilder().setField(DriverAttributeField.ONLINE_STATUS)
				.setRelationalOperato(RelationalOperator.EQUALS)
				.setValue("ONLINE").createDriverSingleCriteria(),
				DriverSingleCriteria.newBuilder().setField(DriverAttributeField.USER_NAME)
				.setRelationalOperato(RelationalOperator.LIKE)
				.setValue("a").createDriverSingleCriteria()
				);
		
		//Creating criteria for drivers that have a bus with hybrid engine and their rating are bigger than 2 
		List<BusSingleCriteria> busCriteriaList=Arrays.asList(
				BusSingleCriteria.newBuilder().setField(BusAttributeField.CAR_ENGINE)
				.setRelationalOperato(RelationalOperator.EQUALS)
				.setValue("HYBRID").createBusSingleCriteria(),
				BusSingleCriteria.newBuilder().setField(BusAttributeField.RATING)
				.setRelationalOperato(RelationalOperator.GREATERTHAN)
				.setValue("2").createBusSingleCriteria()
				);
		
		List<DriverDO> result=myDriverService.filterDrivers(
				
				new BusCriteriaGroup(busCriteriaList, LogicalOperator.AND),
				
				new DriverCriteriaGroup(driverCriteriaList, LogicalOperator.AND)
				
				);
		
		assertTrue(result.stream().filter(driver->driver.getOnlineStatus()==OnlineStatus.OFFLINE).collect(Collectors.toList()).size()==0);
		assertTrue(result.stream().filter(driver->!driver.getUsername().contains("a")).collect(Collectors.toList()).size()==0);
		assertTrue(result.stream().filter(driver->driver.getBusDetails().getEngineType()!=EngineType.HYBRID).collect(Collectors.toList()).size()==0);
		assertTrue(result.stream().filter(driver->driver.getBusDetails().getRating()<2).collect(Collectors.toList()).size()==0);
		
	}
	
	//Filter only according to their bus
	@Test
	public void testFilteringDrivers2() {
		
		//Creating criteria for drivers that have a bus with hybrid engine and their rating are bigger than 2 
		List<BusSingleCriteria> busCriteriaList=Arrays.asList(
				BusSingleCriteria.newBuilder().setField(BusAttributeField.CAR_ENGINE)
				.setRelationalOperato(RelationalOperator.EQUALS)
				.setValue("HYBRID").createBusSingleCriteria(),
				BusSingleCriteria.newBuilder().setField(BusAttributeField.CONVERTIBLE)
				.setRelationalOperato(RelationalOperator.EQUALS)
				.setValue("true").createBusSingleCriteria()
				);
		
		List<DriverDO> result=myDriverService.filterDrivers(
				
				new BusCriteriaGroup(busCriteriaList, LogicalOperator.AND),
				
				null
				
				);
		
		//see in data.sql under the title Create Drivers and bus for filtering for testing in the 4 last lines
		assertTrue(result.size()==2);
		//Lets be sure about that
		assertTrue(result.stream().filter(driver->driver.getBusDetails().getEngineType()!=EngineType.HYBRID).collect(Collectors.toList()).size()==0);
		assertTrue(result.stream().filter(driver->driver.getBusDetails().getConvertible()==false).collect(Collectors.toList()).size()==0);
		
	}
	
	//Get a list with a driver having specific names
	@Test
	public void testFilteringDrivers3() {
		
		//Create criteria for drivers that are online and their name contains the char a
		List<DriverSingleCriteria> driverCriteriaList=Arrays.asList(
				DriverSingleCriteria.newBuilder().setField(DriverAttributeField.USER_NAME)
				.setRelationalOperato(RelationalOperator.EQUALS)
				.setValue("Joachim").createDriverSingleCriteria(),
				DriverSingleCriteria.newBuilder().setField(DriverAttributeField.USER_NAME)
				.setRelationalOperato(RelationalOperator.EQUALS)
				.setValue("Ben").createDriverSingleCriteria(),
				DriverSingleCriteria.newBuilder().setField(DriverAttributeField.USER_NAME)
				.setRelationalOperato(RelationalOperator.EQUALS)
				.setValue("Richard").createDriverSingleCriteria()
				);
		
		List<DriverDO> result=myDriverService.filterDrivers(
				
				null,
				
				new DriverCriteriaGroup(driverCriteriaList, LogicalOperator.OR)
				
				);
		
		assertTrue(result.size()==3);
		//Lets be sure about that
		Assert.assertThat(result.stream().map(driver->driver.getUsername()).collect(Collectors.toList()),hasItems("Joachim","Ben","Richard"));		
	}
	
}
