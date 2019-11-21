package com.myBus.FilteringTests;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.json.JSONException;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import static org.hamcrest.CoreMatchers.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.myBus.ControlerTests.BusControllerTest;
import com.myBusApp.MyBusApplication;
import com.myBusApp.datatransferobject.DriverWithBusDTO;
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
import com.myBusApp.filtering.GroupedCriteria;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = MyBusApplication.class,webEnvironment=WebEnvironment.RANDOM_PORT)
public class DriverControlerFilteringTests {
	
	private static Log myLogger = LogFactory.getLog(BusControllerTest.class);
	
    private static RestTemplate restTemplate;
	
	private static HttpHeaders headers;
	
	private static String basicUrl;
	
	@LocalServerPort
    private int port;
	
	@BeforeClass
	public static void runBeforeAllTestMethods() {
	 
	    restTemplate = new RestTemplate();
	    headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);
	    
	}
	
	//Test that filtering using the criteria for Drivers and Buss is working properly
	@Test
	public void testDriverControllerBasic() throws JsonProcessingException, JSONException {
		basicUrl="http://localhost:"+port+"/v1/drivers";
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
		
		//wrap lists in the wrapper class GroupedCriteria
		GroupedCriteria myGroupedCriteria = new GroupedCriteria(
				new BusCriteriaGroup(busCriteriaList, LogicalOperator.AND), 
				new DriverCriteriaGroup(driverCriteriaList, LogicalOperator.AND));
		
		//Convert myGroupedCriteria to json and parse it into the request
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(myGroupedCriteria);
		
		HttpEntity<String> request = 
			      new HttpEntity<String>(json, headers);
		
		//call driver filtering
		DriverWithBusDTO[] response = 
			      restTemplate.postForObject(basicUrl+"/filter", request, DriverWithBusDTO[].class);
		myLogger.info("==================LOGGING RESULTS FROM testDriverControllerBasic=======================");
		//convert array to list
		List<DriverWithBusDTO> mylist=Arrays.asList(response);
		//iterate through the list and log the drivers
		mylist.forEach(driver->myLogger.info("=================="+driver));
		
		//check that we are getting a result
		assertNotNull(mylist);
		
		//make sure all bus have a hybrid engine
		mylist.forEach(driver->assertTrue(driver.getBusDetails().getEngineType()==EngineType.HYBRID));
		//make sure all bus have rating over 2 points
		mylist.forEach(driver->assertTrue(driver.getBusDetails().getRating()>2));
		//make sure all drivers have name that contains the letter a
		mylist.forEach(driver->assertTrue(driver.getUsername().contains("a")));
		//make sure all drivers have an online status
		mylist.forEach(driver->assertTrue(driver.getOnlineStatus()==OnlineStatus.ONLINE));
	}
	
	//Test that the filtering using the disjunction operator (OR) is working properly
	@Test
	public void testDriverControllerUsingOrOperator() throws JsonProcessingException {
		basicUrl="http://localhost:"+port+"/v1/drivers";
		
		//Creating criteria for drivers that have a bus either with a hybrid or a gas engine 
		List<BusSingleCriteria> busCriteriaList=Arrays.asList(
				BusSingleCriteria.newBuilder().setField(BusAttributeField.CAR_ENGINE)
				.setRelationalOperato(RelationalOperator.EQUALS)
				.setValue("HYBRID").createBusSingleCriteria(),
				BusSingleCriteria.newBuilder().setField(BusAttributeField.CAR_ENGINE)
				.setRelationalOperato(RelationalOperator.EQUALS)
				.setValue("GAS").createBusSingleCriteria()
				);
		
		//wrap lists in the wrapper class GroupedCriteria
		GroupedCriteria myGroupedCriteria = new GroupedCriteria(
				new BusCriteriaGroup(busCriteriaList, LogicalOperator.OR), //<<<<<<<<<<------------
				new DriverCriteriaGroup(null, LogicalOperator.AND));
		
		//Convert myGroupedCriteria to json and parse it into the request
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(myGroupedCriteria);
		
		HttpEntity<String> request = new HttpEntity<String>(json, headers);
		
		//call driver filtering
		DriverWithBusDTO[] response = restTemplate.postForObject(basicUrl+"/filter", request, DriverWithBusDTO[].class);
		
		//convert array to list
		List<DriverWithBusDTO> mylist=Arrays.asList(response);
		//iterate through the list and log the drivers
		myLogger.info("==================LOGGING RESULTS FROM testDriverControllerUsingOrOperator=======================");
		mylist.forEach(driver->myLogger.info("=================="+driver.getBusDetails()));
		
		//check that we are getting a result
		assertNotNull(mylist);
		
		//make sure all bus have a hybrid or gas engine
		Assert.assertThat(
				mylist.stream().map(driver->driver.getBusDetails().getEngineType()).collect(Collectors.toList())
				,hasItems(EngineType.HYBRID,EngineType.GAS));	
	}
	
	//Test that the filtering using the disjunction operator (OR) just for drivers is working properly
	@Test
	public void testDriverControllerFilterJustDrivers() throws JsonProcessingException {
		
		basicUrl="http://localhost:"+port+"/v1/drivers";
		
		//Create criteria for drivers that are online and their name contains the char a
		List<DriverSingleCriteria> driverCriteriaList=Arrays.asList(
				DriverSingleCriteria.newBuilder().setField(DriverAttributeField.USER_NAME)
				.setRelationalOperato(RelationalOperator.LIKE)
				.setValue("J").createDriverSingleCriteria(),
				DriverSingleCriteria.newBuilder().setField(DriverAttributeField.USER_NAME)
				.setRelationalOperato(RelationalOperator.EQUALS)
				.setValue("Richard").createDriverSingleCriteria()
				);
		
		//wrap lists in the wrapper class GroupedCriteria
		GroupedCriteria myGroupedCriteria = new GroupedCriteria(
				new BusCriteriaGroup(null, LogicalOperator.AND), 
				new DriverCriteriaGroup(driverCriteriaList, LogicalOperator.OR));//<<<<<<<<<<<<<<-----------------
		
		//Convert myGroupedCriteria to json and parse it into the request
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(myGroupedCriteria);
		
		HttpEntity<String> request = 
			      new HttpEntity<String>(json, headers);
		
		//call driver filtering
		DriverWithBusDTO[] response = 
			      restTemplate.postForObject(basicUrl+"/filter", request, DriverWithBusDTO[].class);
		myLogger.info("==================LOGGING RESULTS FROM testDriverControllerFilterJustDrivers=======================");
		//convert array to list
		List<DriverWithBusDTO> mylist=Arrays.asList(response);
		//iterate through the list and log the drivers
		mylist.forEach(driver->myLogger.info("=================="+driver));
		
		//check that we are getting a result
		assertNotNull(mylist);
		
		//make sure all driver have a name containing the letter J or equal to Richard
		mylist.forEach(
				driver->assertTrue(
						driver.getUsername().contains("J") 
						|| "Richard".contentEquals(driver.getUsername()
								)  
						));
	}
	
	
	
}
