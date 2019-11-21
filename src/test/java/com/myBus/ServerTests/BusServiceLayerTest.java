package com.myBus.ServerTests;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.myBusApp.MyBusApplication;
import com.myBusApp.domainobject.BusDO;
import com.myBusApp.domainvalue.EngineType;
import com.myBusApp.exception.ConstraintsViolationException;
import com.myBusApp.exception.EntityNotFoundException;
import com.myBusApp.service.Bus.BusInterfaceService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = MyBusApplication.class)
public class BusServiceLayerTest {
	
	@Autowired
	BusInterfaceService myBusService;
	
	private static Log myLogger = LogFactory.getLog(BusServiceLayerTest.class);
	
	//Testing that a bus is successfully found 
	@Test
	public void testBusServiceFind() throws NumberFormatException, EntityNotFoundException {
		BusDO myBus=null;
		myBus=myBusService.find(Long.parseLong("12"));
		
		assertNotNull(myBus);
	}
	
	//Testing that Exception is thrown during a non existing bus is being searched
	@Test
	public void testBusServiceFindException() {	    	
	    	assertThrows(EntityNotFoundException.class, () -> {
	    		myBusService.find(Long.parseLong("888"));
	        });
	}
	
	//Testing that a bus is successfully saved
	@Test
	public void testBusServiceSave() throws ConstraintsViolationException, EntityNotFoundException {
		BusDO myBus=new BusDO("AC-AJ-5077",5,EngineType.ELECTRIC);
		myBus=myBusService.create(myBus); 
		
		myBus=myBusService.findBylicenseplate("AC-AJ-5077");
		
		assertNotNull(myBus.getId());
	}
	
	//Check that a bus with same license plate can't be persisted 
	//Bus with license plate AS-OZ24654 is inserted direct to H2 data base
	//over a INSERT INTO query, (see in data.sql)
	@Test
	public void saveBusAlreadyExists() {
		BusDO myBus=new BusDO("AS-OZ24654",5,EngineType.ELECTRIC);	
		
		assertThrows(ConstraintsViolationException.class, () -> {
			myBusService.create(myBus);
        });
	}
	
	//Testing that a bus is successfully updated 
	//bus is being retrieved once more just to be sure that we are getting the newest entity from db
	@Test
	public void  testUpdateBusRating() throws EntityNotFoundException {
		//Find and update the bus with license plate AS-OZ24654
		myBusService.updateRating("AS-OZ24654", 7);
		//retrieve entity once more just to be sure that is updated
		BusDO myBus=myBusService.findBylicenseplate("AS-OZ24654");
		myLogger.info(myBus);
		assertTrue(myBus.getRating().compareTo(7)==0);
	}
	
	//Testing that a EntityNotFoundException is thrown 
	//during a non existing bus is being updated
	@Test
	public void  updateNotExistingBus() {

		assertThrows(EntityNotFoundException.class, () -> {
			myBusService.updateRating("AS-555555", 7);
        });
	}
	
	//Test that a bus is successfully deleted
	@Test
	public void testDeleteBus() throws EntityNotFoundException {
		myBusService.delete("AS-OZ24654");
	
		assertThrows(EntityNotFoundException.class, () -> {
			myBusService.findBylicenseplate("AS-OZ24654");
        });
	}
	
	//Testing that a EntityNotFoundException is thrown 
	//during a non existing bus is being deleted
	@Test
	public void testDeleteBusNotExists() {
		
		assertThrows(EntityNotFoundException.class, () -> {
			myBusService.delete("AS-555555");
        });
	}
}
