package com.myBusApp.service.Bus;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.myBusApp.dataaccessobject.BusRepository;
import com.myBusApp.datatransferobject.BusToUpdateDTO;
import com.myBusApp.domainobject.BusDO;
import com.myBusApp.domainobject.DriverDO;
import com.myBusApp.domainvalue.EngineType;
import com.myBusApp.exception.ConstraintsViolationException;
import com.myBusApp.exception.EntityNotFoundException;


/**
 * Service to encapsulate the link between BusRepository and controller 
 * and to have business logic for some bus specific things.
 * <p/>
 */

@Service
public class BusServiceImpl implements BusInterfaceService {
	
	private static Logger myLogger = LoggerFactory.getLogger(BusServiceImpl.class);
	
    private final BusRepository myBusRepo;
    
    public BusServiceImpl(final BusRepository myBusRepo)
    {
    	this.myBusRepo = myBusRepo;
    }
	
	
	 /**
     * Creates a new driver.
     * Checks if bus already exist
     *
     * @param bus
     * @return BusDO
     * @throws ConstraintsViolationException if a bus with same license plate
     *  already exists
     * @return BusDO
     */
	@Override	
	public BusDO create(BusDO bus) throws ConstraintsViolationException {
		String busLicenseplate=bus.getLicenseplate();
		BusDO myBus;
		try {
			myBus = myBusRepo.save(bus);
		} catch (DataIntegrityViolationException e) {
			myLogger.warn("A bus with the same license plate: "+ busLicenseplate +" already exists.");
            throw new ConstraintsViolationException("A bus with the same license plate: "+ busLicenseplate +" already exists.");
		}
		return myBus;
	}
	
	/**
     * Finds a new bus.
     * Checks if bus exists
     *
     * @param id
     * @return BusDO
     * @throws EntityNotFoundException if bus does not exists
     *  already exists
     * @return BusDO
     */
	@Override
	public BusDO find(long id) throws EntityNotFoundException {
		
		Optional<BusDO> myBus=myBusRepo.findById(id);
		return myBus.orElseThrow(() -> {
			myLogger.warn("Bus with id: " + id+" does not exists.");
            return new EntityNotFoundException("Bus with id: " + id+" does not exists.");
        });
	}
	
	/**
     * Finds a new bus by license plate.
     * Checks if bus exists
     *
     * @param licensePlate
     * @throws EntityNotFoundException if bus does not exists
     *  already exists
     *  @return BusDO
     */
	@Override	
	public BusDO findBylicenseplate(String licensePlate) throws EntityNotFoundException{
		Optional<BusDO> existingBus = myBusRepo.findBylicenseplate(licensePlate);
        if(!existingBus.isPresent()){
        	myLogger.warn("Bus with license plate: " + licensePlate+" does not exists.");
            throw new EntityNotFoundException("Bus with license plate: " + licensePlate+" does not exists.");
        }
        
        return existingBus.get();       
	}
	
	/**
     * Finds all bus that are persisted in data base
     *
     * @return List<BusDO>
     */
	@Override
	public List<BusDO> findAll() {
		return (List<BusDO>)myBusRepo.findAll();
	}
	
	/**
     * Finds a bus by license plate and updates the rating.
     * Checks if bus exists
     *
     * @param licensePlate
     * @param rating
     * @throws EntityNotFoundException if bus does not exists
     *  already exists
     *  @return BusDO
     */
	@Override
	@Transactional
	public BusDO updateRating(String licensePlate, Integer rating) throws EntityNotFoundException {
		
		BusDO myBus= findBylicenseplate(licensePlate);
		myBus.setRating(rating);		
		return myBus;		
		
	}
	
	/**
     * Deletes bus from the data base
     *
     * @param licensePlate
     * @throws EntityNotFoundException if bus does not exists
     *  already exists
     */
	@Override
	@Transactional
	public void delete(String licensePlate) throws EntityNotFoundException {
		BusDO myBus=findBylicenseplate(licensePlate);
		myBusRepo.delete(myBus);		
	}
	
	
	/**
     * Updates the bus with the given license plate
     * The method looks inside the Object <code>BusToUpdateDTO busWithNewValues</code>,
     * takes all non null fields and set the same ones in the <code>BusDO</code>
     *
     * @param licensePlate
     * @param busWithNewValues contains all the field to be updated
     * @throws EntityNotFoundException if bus does not exists
     * @return BusDO
     */
	@Override
	@Transactional
	public BusDO updateFields(String licensePLate, BusToUpdateDTO busWithNewValues) throws EntityNotFoundException {
		BusDO busToUpdate=findBylicenseplate(licensePLate);
		Integer rating=busWithNewValues.getRating();
		if(rating!=null && rating!=0) {
			busToUpdate.setRating(rating);
		}
		Boolean convertible=busWithNewValues.getConvertible();
		if(convertible!=null ) {
			busToUpdate.setConvertible(convertible);
		}
		Integer seatCount=busWithNewValues.getSeatCount();
		if(seatCount!=null) {
			busToUpdate.setSeatcount(seatCount);
		}
		EngineType engine=busWithNewValues.getEngineType();
		if(engine!=null ) {
			busToUpdate.setEngineType(engine);
		}
		
		String manufacturer=busWithNewValues.getManufacturer();
		if(manufacturer!=null && !manufacturer.isEmpty()) {
			busToUpdate.setManufacturer(manufacturer);
		}
		
		return busToUpdate;
	}
	
	@Override
	public DriverDO getDriverFromBus(String licensePLate) throws EntityNotFoundException {
		BusDO bus=findBylicenseplate(licensePLate);
		return bus.getDriverDetails();
	}
	

}
