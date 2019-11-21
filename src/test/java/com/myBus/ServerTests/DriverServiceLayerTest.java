package com.myBus.ServerTests;

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
import com.myBusApp.domainobject.DriverDO;
import com.myBusApp.exception.BusAlreadyInUseException;
import com.myBusApp.exception.DriverProfileDeactivatedException;
import com.myBusApp.exception.EntityNotFoundException;
import com.myBusApp.exception.WrongUserStatus;
import com.myBusApp.service.driver.DriverService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = MyBusApplication.class)
public class DriverServiceLayerTest {
	
	@Autowired
	DriverService myDriverService;
	
	private static Log myLogger = LogFactory.getLog(DriverServiceLayerTest.class);
	//Test an existing bus is successfully allocated to a driver
	@Test
	public void testBusToDriverAllocation() throws DriverProfileDeactivatedException, EntityNotFoundException, WrongUserStatus, BusAlreadyInUseException {
		Long driverId=Long.parseLong("5");
		DriverDO myDriver=myDriverService.selectBus(driverId, "PO-OZ24654");
		
		myDriver=myDriverService.find(driverId);
		myLogger.info("testBusToDriverAllocation: \n"+myDriver);
		assertTrue(myDriver.getBusDetails()!=null);
		
	}
	
	//Check that an EntityNotFoundException is being thrown
	//during allocation of a non existing driver
	@Test
	public void testBusToDriverNotExists() {
		Long driverId=Long.parseLong("666");
		assertThrows(EntityNotFoundException.class, () -> {
			myDriverService.selectBus(driverId, "PO-OZ24654");
        });
		
	}
	
	//Check that an EntityNotFoundException is being thrown
	//during allocation of a non existing bus
	@Test
	public void testBusNotExistsToDriver() {
		Long driverId=Long.parseLong("5");
		assertThrows(EntityNotFoundException.class, () -> {
			myDriverService.selectBus(driverId, "5555555");
        });
		
	}
	
	
	//Check that an BusAlreadyInUseException is being thrown
	//during allocation of already used bus
	@Test
	public void testBusAlreadyAllocated() throws DriverProfileDeactivatedException, EntityNotFoundException, WrongUserStatus, BusAlreadyInUseException {
		Long driverId=Long.parseLong("5");
		Long anOtherDriverId=Long.parseLong("6");
		myDriverService.selectBus(driverId, "KL-KL9999");
		assertThrows(BusAlreadyInUseException.class, () -> {
			myDriverService.selectBus(anOtherDriverId, "KL-KL9999");
        });
		
	}
	//Check allocation of offline driver
	@Test
	public void testDriverOffline() {
		Long driverId=Long.parseLong("3");
		assertThrows(WrongUserStatus.class, () -> {
			myDriverService.selectBus(driverId, "PO-OZ24654");
        });		
	}
	
	//Check deallocation 
	@Test
	public void testDriverDeallocation() throws EntityNotFoundException {
		Long driverId=Long.parseLong("3");
		myDriverService.deselectBus(driverId);
		//just to be sure that we are getting current data, read driver once more
		DriverDO myNewDriver=this.myDriverService.find(driverId);
		assertTrue(myNewDriver.getBusDetails()==null);
		
	}
	
	
	//Check deallocation of non existing driver
	@Test
	public void testDriverNotExistsDeallocation() {
		Long driverId=Long.parseLong("888");
		assertThrows(EntityNotFoundException.class, () -> {
			myDriverService.deselectBus(driverId);
	        });		
	}
}
