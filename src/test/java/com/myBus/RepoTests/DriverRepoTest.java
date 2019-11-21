package com.myBus.RepoTests;

import static org.junit.Assert.assertTrue;

import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.myBusApp.MyBusApplication;
import com.myBusApp.dataaccessobject.BusRepository;
import com.myBusApp.dataaccessobject.DriverRepository;
import com.myBusApp.domainobject.BusDO;
import com.myBusApp.domainobject.DriverDO;
import com.myBusApp.domainvalue.OnlineStatus;
import com.myBusApp.exception.EntityNotFoundException;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = MyBusApplication.class)
public class DriverRepoTest {
	
	@Autowired
	DriverRepository myDriverRepo;
	
	@Autowired
	BusRepository myBusRepo;
	
	//Test bus selection directly over Dao Layer
	@Test
	@Transactional
    public void testDriverSelectsBus() throws NumberFormatException, EntityNotFoundException
    {  
		String busId="12";
		BusDO myBus=null;
			myBus = myBusRepo.findById(Long.parseLong(busId))
					.orElseThrow(() -> new EntityNotFoundException("Could not find entity with id: " + busId));
		
		DriverDO myDriver=myDriverRepo.findByOnlineStatus(OnlineStatus.ONLINE).get(0);
		Long driverId=myDriver.getId();
		myDriver.setBusDetails(myBus);
		
		DriverDO newDriver=null;
			newDriver=myDriverRepo.findById(driverId)
					.orElseThrow(() -> new EntityNotFoundException("Could not find entity with id: " + driverId));
		
		System.out.println(" ========================= Driver with id: "+driverId+" found "+newDriver);
		
		assertTrue(newDriver.getBusDetails().getId().
				compareTo(Long.parseLong(busId))==0);    	   
		
    }
	
	
}
