package com.myBus.FilteringTests;

import java.util.Arrays;
import java.util.List;

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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.myBusApp.MyBusApplication;
import com.myBusApp.datatransferobject.DriverWithBusDTO;
import com.myBusApp.domainvalue.DriverAttributeField;
import com.myBusApp.domainvalue.LogicalOperator;
import com.myBusApp.domainvalue.RelationalOperator;
import com.myBusApp.filtering.BusCriteriaGroup;
import com.myBusApp.filtering.DriverCriteriaGroup;
import com.myBusApp.filtering.DriverSingleCriteria;
import com.myBusApp.filtering.GroupedCriteria;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = MyBusApplication.class,webEnvironment=WebEnvironment.RANDOM_PORT)
public class DriverControlerFilteringExceptionTests {
	
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
	
	//Test that exception is thrown when value is not compatible with the field 
	@Test
	public void testDriverControllerExceptionOnInvalidValue() throws JsonProcessingException, JSONException {
		basicUrl="http://localhost:"+port+"/v1/drivers";
		
		List<DriverSingleCriteria> driverCriteriaList=Arrays.asList(
				DriverSingleCriteria.newBuilder().setField(DriverAttributeField.ID)
				.setRelationalOperato(RelationalOperator.GREATERTHAN)
				.setValue("Joachim").createDriverSingleCriteria()//here we give am aphanumeric value to be compared with the id
				);
		
		//wrap lists in the wrapper class GroupedCriteria
		GroupedCriteria myGroupedCriteria = new GroupedCriteria(
				new BusCriteriaGroup(null, LogicalOperator.AND), 
				new DriverCriteriaGroup(driverCriteriaList, LogicalOperator.AND));
		
		//Convert myGroupedCriteria to json and parse it into the request
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(myGroupedCriteria);
		
		HttpEntity<String> request = 
			      new HttpEntity<String>(json, headers);
		
		//An exception is expected with status 400
		try {
			restTemplate.postForObject(basicUrl+"/filter", request, DriverWithBusDTO[].class);
		}catch (HttpClientErrorException e) {
			Assert.assertEquals(HttpStatus.BAD_REQUEST , e.getStatusCode());
		}
		
	}
	
	
	
	//Test that exception is thrown when a relational operator is given that is not compatible with the field
	@Test
	public void testDriverControllerExceptionOnIncompatibleOperator() throws JsonProcessingException, JSONException {
		basicUrl="http://localhost:"+port+"/v1/drivers";
		
		//We give here the operator greater than (>) which is incompatible with boolean variables
		List<DriverSingleCriteria> driverCriteriaList=Arrays.asList(
				DriverSingleCriteria.newBuilder().setField(DriverAttributeField.DELETED)
				.setRelationalOperato(RelationalOperator.GREATERTHAN)//here we give an incompatible operator
				.setValue("TRUE").createDriverSingleCriteria()
				);
		
		//wrap lists in the wrapper class GroupedCriteria
		GroupedCriteria myGroupedCriteria = new GroupedCriteria(
				new BusCriteriaGroup(null, LogicalOperator.AND), 
				new DriverCriteriaGroup(driverCriteriaList, LogicalOperator.AND));
		
		//Convert myGroupedCriteria to json and parse it into the request
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(myGroupedCriteria);
		
		HttpEntity<String> request = 
			      new HttpEntity<String>(json, headers);
		
		//An exception is expected with status 400
		try {
			restTemplate.postForObject(basicUrl+"/filter", request, DriverWithBusDTO[].class);
		}catch (HttpClientErrorException e) {
			Assert.assertEquals(HttpStatus.NOT_ACCEPTABLE , e.getStatusCode());
		}
		
	}
	//like (OnlineStatus>2)
}