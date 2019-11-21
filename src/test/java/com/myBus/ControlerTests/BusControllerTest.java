package com.myBus.ControlerTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.json.JSONException;
import org.json.JSONObject;
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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.myBusApp.MyBusApplication;
import com.myBusApp.datatransferobject.BusDTO;
import com.myBusApp.domainvalue.EngineType;

/**
 * @author imertzanidis
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = MyBusApplication.class,webEnvironment=WebEnvironment.RANDOM_PORT)
public class BusControllerTest {
	
	private static Log myLogger = LogFactory.getLog(BusControllerTest.class);
	
    private static RestTemplate restTemplate;
	
	private static HttpHeaders headers;
	
	private JSONObject busJsonObject;
	
	@LocalServerPort
    private int port;
	
	@BeforeClass
	public static void runBeforeAllTestMethods() {
	 
	    restTemplate = new RestTemplate();
	    headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);
	    
	}
	
	//Checks if a new Bus is created over the controller
	@Test
	public void testCreateBus() throws JsonProcessingException, Exception {
		busJsonObject = new JSONObject();
		busJsonObject.put("engineType", "ELECTRIC");
		busJsonObject.put("licensePlate", "AC-MM5077");
		
		HttpEntity<String> request = 
			      new HttpEntity<String>(busJsonObject.toString(), headers);
			     
		BusDTO  myNewBus = 
			      restTemplate.postForObject("http://localhost:"+port+"/v1/bus", request, BusDTO.class);
		
		myLogger.info("=================="+myNewBus);

		assertNotNull(myNewBus);
	    assertNotNull(myNewBus.getId());
		
	}
	
	//Checks that a bus can not be persisted when an other bus has the same license plate
	@Test
	public void testCreateBusAlreadyExists() throws JSONException {
		busJsonObject = new JSONObject();
		busJsonObject.put("engineType", "ELECTRIC");
		busJsonObject.put("licensePlate", "PO-RT8523");
		
		HttpEntity<String> request = 
			      new HttpEntity<String>(busJsonObject.toString(), headers);
		try {
				restTemplate.postForObject("http://localhost:"+port+"/v1/bus", request, String.class);
		}catch(HttpClientErrorException  e){
			assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
		}			
	}
	
	//Checks that a bus can not be persisted when not license plate was given
	@Test
	public void testCreateBusWithNoLicesePlate() throws JSONException {
		busJsonObject = new JSONObject();
		busJsonObject.put("engineType", "ELECTRIC");
		
		HttpEntity<String> request = 
			      new HttpEntity<String>(busJsonObject.toString(), headers);
		try {
				restTemplate.postForObject("http://localhost:"+port+"/v1/bus", request, String.class);
		}catch(HttpClientErrorException  e){
			assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
		}			
	}
	
	//Check that an existing bus is being retrieved successfully
	@Test
	public void testGetExistingBus() {
		String licensePlate="PO-RT8523";
		myLogger.info("================== GET BY LICENSE PLATE");
		ResponseEntity<BusDTO> myBus=restTemplate.getForEntity("http://localhost:"+port+"/v1/bus/licencePlate/"+licensePlate, BusDTO.class);
		myLogger.info("================== GET BY LICENSE PLATE: "+myBus);
		assertNotNull(myBus);
		assertTrue(licensePlate.equals(myBus.getBody().getLicensePlate()));
	}
	
	//Check if Bad request is coming back 
	// during a non existing bus is being retrieved successfully
	@Test
	public void testGetNonExistingBus() {
		String licensePlate="8888888";
		myLogger.info("================== GET BY WRONG LICENSE PLATE");
		try {
			restTemplate.getForEntity("http://localhost:"+port+"/v1/bus/licencePlate/"+licensePlate, String.class);
		}catch(HttpClientErrorException  e){
			assertEquals(HttpStatus.NOT_FOUND , e.getStatusCode());
		}		
	}
	
	//Check if not found response is coming back 
	// during BusController is trying to delete a non existing bus
	@Test
	public void testDeleteNonExistingBus() {
		String licensePlate="8888888";
		myLogger.info("================== DELETE BY WRONG LICENSE PLATE");
		try {
			restTemplate.delete("http://localhost:"+port+"/v1/bus/"+licensePlate, "");
		}catch(HttpClientErrorException  e){
			assertEquals(HttpStatus.NOT_FOUND , e.getStatusCode());
		}		
	}
	
	//Check an existing bus is being successfully deleted
	@Test
	public void testDeleteExistingBus() {
		String licensePlate="YX-NM6987";
		myLogger.info("================== DELETE BY WRONG LICENSE PLATE");
		try {
			restTemplate.delete("http://localhost:"+port+"/v1/bus/"+licensePlate, "");
		}catch(HttpClientErrorException  e){
			fail();
		}		
	}
	
	//Check an existing bus is being successfully updated
	@Test
	public void testUpdateMultipleFieldsOfExistingBus() throws JSONException {
		String licensePlate="HJ-WE7894";	
		
		busJsonObject = new JSONObject();
		busJsonObject.put("engineType", "ELECTRIC");
		busJsonObject.put("rating", 5);
		busJsonObject.put("manufacturer", "Toyota");
		
		myLogger.info("================== UPDATE CAR"+busJsonObject);
		
		HttpEntity<String> request = 
			      new HttpEntity<String>(busJsonObject.toString(), headers);
			     
		ResponseEntity<BusDTO>  myNewBusResponse = 
			      restTemplate.exchange("http://localhost:"+port+"/v1/bus/"+licensePlate,HttpMethod.PUT, request, BusDTO.class);
		
		myLogger.info("=================="+myNewBusResponse);

		assertNotNull(myNewBusResponse);
		BusDTO myNewBus=myNewBusResponse.getBody();
	    assertNotNull(myNewBus.getManufacturer());
	    assertTrue(myNewBus.getEngineType().compareTo(EngineType.ELECTRIC)==0);
	    assertTrue(myNewBus.getRating().compareTo(5)==0);
	    assertTrue("Toyota".equals(myNewBus.getManufacturer()));
	}
	
	//Test bus was parsed that doesn't exists
	@Test
	public void testUpdateNonExistingBus() throws JSONException {
		String licensePlate="88888";	
		
		busJsonObject = new JSONObject();
		busJsonObject.put("manufacturer", "ELECTRIC");
		
		myLogger.info("================== UPDATE NON EXISTING CAR "+busJsonObject);
		
		HttpEntity<String> request = 
			      new HttpEntity<String>(busJsonObject.toString(), headers);
		assertThrows(HttpClientErrorException.class, () -> {
			 restTemplate.exchange("http://localhost:"+port+"/v1/bus/"+licensePlate,HttpMethod.PUT, request, BusDTO.class);
        });
	}
	
	//Test fields was given with wrong conditions or invalid form for example ENGINETYPE=blabla
	@Test
	public void testUpdateBusWithInvalidData() throws JSONException {
		String licensePlate="AS-OZ24654";
		
		busJsonObject = new JSONObject();
		busJsonObject.put("engineType", "blabla");
		
		myLogger.info("================== UPDATE CAR WITH INVALID DATA "+busJsonObject);
		HttpEntity<String> request = 
			      new HttpEntity<String>(busJsonObject.toString(), headers);
		assertThrows(HttpClientErrorException.class, () -> {
			 restTemplate.exchange("http://localhost:"+port+"/v1/bus/"+licensePlate,HttpMethod.PUT, request, BusDTO.class);
        });
	}
	
	//Test fields was given with wrong conditions or invalid form for example rating=100
	//although maximum value is defined to be 5
	@Test
	public void testUpdateBusWithDataOutOfConditions() throws JSONException {
		String licensePlate="AS-OZ24654";
		
		busJsonObject = new JSONObject();
		busJsonObject.put("rating", "100");
		
		myLogger.info("================== UPDATE CAR WITH INVALID DATA "+busJsonObject);
		HttpEntity<String> request = 
			      new HttpEntity<String>(busJsonObject.toString(), headers);
		assertThrows(HttpClientErrorException.class, () -> {
			 restTemplate.exchange("http://localhost:"+port+"/v1/bus/"+licensePlate,HttpMethod.PUT, request, BusDTO.class);
        });
	}
	
	
}

