package com.myBus.ControlerTests;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.myBusApp.MyBusApplication;
import com.myBusApp.datatransferobject.BusDTO;
import com.myBusApp.datatransferobject.DriverWithBusDTO;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = MyBusApplication.class,webEnvironment=WebEnvironment.RANDOM_PORT)
public class DriverControllerTests {
	
private static Log myLogger = LogFactory.getLog(BusControllerTest.class);
	
    private static RestTemplate restTemplate;
	
	private static HttpHeaders headers;
	
	@LocalServerPort
    private int port;
	
	@BeforeClass
	public static void runBeforeAllTestMethods() {
	 
	    restTemplate = new RestTemplate();
	    headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);
	    
	}
	
	//Simple test to check if a bus is successfully selected by a driver
	@Test
	public void testDriverSelectsBus() throws JSONException {
		long driverId=6;
		String licensePlate="HH-HH5077";	
		
		myLogger.info("================== Allocate bus to Driver ================== ");
		
		HttpEntity<String> request = new HttpEntity<String>(headers);
			     
		ResponseEntity<DriverWithBusDTO>  myDriverResponse = 
			      restTemplate.exchange("http://localhost:"+port+"/v1/drivers/select/"+driverId+"?licensePlate="+licensePlate,
			    		  HttpMethod.PUT, request, DriverWithBusDTO.class);
		DriverWithBusDTO myDriver=myDriverResponse.getBody();
		assertNotNull(myDriverResponse);
		
		//Now lets see if the @OneToOne Mapping is really working
		BusDTO myBus=myDriver.getBusDetails();
		assertNotNull(myBus);
		myLogger.info("================== My updated Driver: "+myDriver.toString()+"\n================== "
		+ "==================  and new bus: "+myBus+"================== ");
		
	    assertTrue(licensePlate.equals(myBus.getLicensePlate()));
	    
	    //Check the @OneToOne Mapping into the bus!
	    ResponseEntity<DriverWithBusDTO> myDriverFromBus=restTemplate.getForEntity("http://localhost:"+port+"/v1/bus/driverFromBus/"+licensePlate, DriverWithBusDTO.class);
		assertNotNull(myDriverFromBus);
		assertTrue(myDriverFromBus.getBody().getId()==driverId);
	}
	
	//Simple test to check if a driver is not allowed to select an already used bus
	@Test
	public void testDriverSelectsBusAlreadyInUse() throws JSONException {
		long driverId=6;
		String licensePlate="AC-AJ5077";	
		HttpEntity<String> request = new HttpEntity<String>(headers);
		
		myLogger.info("================== Allocate bus to Driver ALREADY IN USE ================== ");
		ResponseEntity<DriverWithBusDTO>  myDriverResponse = 
				restTemplate.exchange("http://localhost:"+port+"/v1/drivers/select/"+driverId+"?licensePlate="+licensePlate,
			    		  HttpMethod.PUT, request, DriverWithBusDTO.class);
		assertNotNull(myDriverResponse);
		
		//try to select the same bus from an other driver with ID 5
		try {
			restTemplate.exchange("http://localhost:"+port+"/v1/drivers/select/"+5+"?licensePlate="+licensePlate,
		    		  HttpMethod.PUT, request, DriverWithBusDTO.class);
		}catch (HttpClientErrorException e) {
			Assert.assertEquals(HttpStatus.CONFLICT , e.getStatusCode());
		}
	}
	
	//Simple test to check if a driver is not allowed to select a bus offline
	@Test
	public void testDriverSelectsBusOffline() throws JSONException {
		long driverId=3;
		String licensePlate="HJ-WE7894";	
		HttpEntity<String> request = new HttpEntity<String>(headers);
		
		myLogger.info("================== Allocate bus to Driver OFFLINE ================== ");
		try {
			restTemplate.exchange("http://localhost:"+port+"/v1/drivers/select/"+driverId+"?licensePlate="+licensePlate,
		    		  HttpMethod.PUT, request, DriverWithBusDTO.class);
		}catch (HttpClientErrorException e) {
			Assert.assertEquals(HttpStatus.FORBIDDEN , e.getStatusCode());
		}			      
	}
	
	//Test driver tries to select non existing bus
	@Test
	public void testNonExistingDriverSelectsBus() throws JSONException {
		long driverId=888;
		String licensePlate="HJ-WE7894";	
		HttpEntity<String> request = new HttpEntity<String>(headers);
		
		myLogger.info("================== Allocate bus to NON EXISTING Driver ================== ");
		try {
			restTemplate.exchange("http://localhost:"+port+"/v1/drivers/select/"+driverId+"?licensePlate="+licensePlate,
		    		  HttpMethod.PUT, request, DriverWithBusDTO.class);
		}catch (HttpClientErrorException e) {
			Assert.assertEquals(HttpStatus.NOT_FOUND , e.getStatusCode());
		}			      
	}
	
	
	//Test a non existing driver tries to select a bus
	@Test
	public void testDriverSelectsNonExistingBus() throws JSONException {
		long driverId=5;
		String licensePlate="888888888";	
		HttpEntity<String> request = new HttpEntity<String>(headers);
		
		myLogger.info("================== Allocate NON EXISTING bus to Driver  ================== ");
		try {
			restTemplate.exchange("http://localhost:"+port+"/v1/drivers/select/"+driverId+"?licensePlate="+licensePlate,
		    		  HttpMethod.PUT, request, DriverWithBusDTO.class);
		}catch (HttpClientErrorException e) {
			Assert.assertEquals(HttpStatus.NOT_FOUND , e.getStatusCode());
		}			      
	}
	
	//Test a driver with a deactivated profile (deleted=true) tries to select a bus
	@Test
	public void testDeactivatedDriverSelectsBus() throws JSONException {
		long driverId=4;
		String licensePlate="GT-OZ24654";	
		HttpEntity<String> request = new HttpEntity<String>(headers);
		
		myLogger.info("================== Allocate bus to DEACTIVATED Driver  ================== ");
		try {
			restTemplate.exchange("http://localhost:"+port+"/v1/drivers/select/"+driverId+"?licensePlate="+licensePlate,
		    		  HttpMethod.PUT, request, DriverWithBusDTO.class);
		}catch (HttpClientErrorException e) {
			Assert.assertEquals(HttpStatus.GONE , e.getStatusCode());
		}			      
	}
	
	//Test to check if a bus is successfully deselected by a driver
	@Test
	public void testDriverDeselectsBus() throws JSONException {
		long driverId=6;
		
		myLogger.info("================== DESELECT CAR ================== ");
		
		HttpEntity<String> request = new HttpEntity<String>(headers);
			     
		ResponseEntity<DriverWithBusDTO>  myDriverResponse = 
			      restTemplate.exchange("http://localhost:"+port+"/v1/drivers/deselect/"+driverId,HttpMethod.PUT, request, DriverWithBusDTO.class);
		DriverWithBusDTO myDriver=myDriverResponse.getBody();
		assertNotNull(myDriverResponse);
		
		BusDTO myBus=myDriver.getBusDetails();
		assertNull(myBus);
		myLogger.info("================== My updated Driver: "+myDriver.toString());											
	}
	
	//Test a non existing driver can not deselect a bus
	@Test
	public void testDriverDeselectsNonExistingBus() throws JSONException {
		long driverId=888;
		
		myLogger.info("================== DESELECT CAR");
		
		HttpEntity<String> request = new HttpEntity<String>(headers);
			      
		try {
			restTemplate.exchange("http://localhost:"+port+"/v1/drivers/deselect/"+driverId,HttpMethod.PUT, request, DriverWithBusDTO.class);
		}catch (HttpClientErrorException e) {
			Assert.assertEquals(HttpStatus.NOT_FOUND , e.getStatusCode());
		}	
	}
	
	
}
