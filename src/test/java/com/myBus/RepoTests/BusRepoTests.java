package com.myBus.RepoTests;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.myBusApp.MyBusApplication;
import com.myBusApp.dataaccessobject.BusRepository;
import com.myBusApp.domainobject.BusDO;
import com.myBusApp.domainvalue.EngineType;
import com.myBusApp.exception.EntityNotFoundException;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = MyBusApplication.class)
public class BusRepoTests
{
	@Autowired
	BusRepository myBusRepo;
	
	private static final Logger myLogger = LoggerFactory.getLogger(BusRepoTests.class);
	
	//Test: a bus is successfully persisted
    @Test
    public void testBusRepositoryCreate()
    {    	
    	BusDO myBus=new BusDO("AC-AJ5066", 5, EngineType.ELECTRIC);
    	myLogger.info(" ========================= Bus before saved in h2 data base :"+myBus);
    	myBus=myBusRepo.save(myBus);
    	myLogger.info(" ========================= Bus saved in h2 data base :"+myBus);
    	assertNotNull(myBus.getId());
    }
    
    //Test: retreiving an entity from data base
    @Test
    public void testBusRepositoryRead() throws NumberFormatException, EntityNotFoundException
    {    	
    	String busId="12";
    	BusDO myBus=null;
		myBus = myBusRepo.findById(Long.parseLong(busId))
				.orElseThrow(() -> new EntityNotFoundException("Could not find entity with id: " + busId));
		
		myLogger.info(" ========================= Bus with id: "+busId+" found "+myBus);
    	assertNotNull(myBus);
    	assertNotNull(myBus.getId());
    }
    
    //Test: a bus is successfully updated
    @Test
    @Transactional
    public void testBusRepositoryUpdate() throws NumberFormatException, EntityNotFoundException
    { 
    	String busId="12";
    	BusDO myBus = myBusRepo.findById(Long.parseLong(busId))
					.orElseThrow(() -> new EntityNotFoundException("Could not find entity with id: " + busId));
    	
    	//Rate the bus with super review!!!!
    	myBus.setRating(5);
    	
    	BusDO busUpdated=null;
    	//and then retreive entity once more from data base 
    	//to determine if column was updated
		busUpdated = myBusRepo.findById(Long.parseLong(busId))
				.orElseThrow(() -> new EntityNotFoundException("Could not find entity with id: " + busId));
    	
    	assertTrue(busUpdated.getRating().compareTo(5)==0);    	   	
    }
    
    

}
