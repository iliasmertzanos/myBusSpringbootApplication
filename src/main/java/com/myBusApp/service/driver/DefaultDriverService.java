package com.myBusApp.service.driver;

import com.myBusApp.dataaccessobject.BusRepository;
import com.myBusApp.dataaccessobject.DriverRepository;
import com.myBusApp.domainobject.BusDO;
import com.myBusApp.domainobject.DriverDO;
import com.myBusApp.domainvalue.GeoCoordinate;
import com.myBusApp.domainvalue.OnlineStatus;
import com.myBusApp.exception.BusAlreadyInUseException;
import com.myBusApp.exception.ConstraintsViolationException;
import com.myBusApp.exception.DriverProfileDeactivatedException;
import com.myBusApp.exception.EntityNotFoundException;
import com.myBusApp.exception.WrongUserStatus;
import com.myBusApp.filtering.BusCriteriaGroup;
import com.myBusApp.filtering.CriteriaGroup;
import com.myBusApp.filtering.DriverCriteriaGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service to encapsulate the link between DAO and controller and to have business logic for some driver specific things.
 * <p/>
 */
@Service
public class DefaultDriverService implements DriverService
{

    private static final Logger LOG = LoggerFactory.getLogger(DefaultDriverService.class);

    private final DriverRepository driverRepository;
    
    //Just to be sure that no Circular dependency exists
    private final BusRepository myBusRepo;


    public DefaultDriverService(final DriverRepository driverRepository,
    		final BusRepository myBusRepo)
    {
        this.myBusRepo = myBusRepo;
		this.driverRepository = driverRepository;
    }


    /**
     * Selects a driver by id.
     *
     * @param driverId
     * @return found driver
     * @throws EntityNotFoundException if no driver with the given id was found.
     */
    @Override
    public DriverDO find(Long driverId) throws EntityNotFoundException
    {
        return findDriverChecked(driverId);
    }


    /**
     * Creates a new driver.
     *
     * @param driverDO
     * @return DriverDO
     * @throws ConstraintsViolationException if a driver already exists with the given username, ... .
     */
    @Override
    @Transactional
    public DriverDO create(DriverDO driverDO) throws ConstraintsViolationException
    {
        DriverDO driver;
        try
        {
            driver = driverRepository.save(driverDO);
        }
        catch (DataIntegrityViolationException e)
        {
            LOG.warn("ConstraintsViolationException while creating a driver: {}", driverDO, e);
            throw new ConstraintsViolationException(e.getMessage());
        }
        return driver;
    }


    /**
     * Deletes an existing driver by id.
     *
     * @param driverId
     * @throws EntityNotFoundException if no driver with the given id was found.
     */
    @Override
    @Transactional
    public void delete(Long driverId) throws EntityNotFoundException
    {
        DriverDO driverDO = findDriverChecked(driverId);
        driverDO.setDeleted(true);
    }


    /**
     * Update the location for a driver.
     *
     * @param driverId
     * @param longitude
     * @param latitude
     * @throws EntityNotFoundException
     */
    @Override
    @Transactional
    public void updateLocation(long driverId, double longitude, double latitude) throws EntityNotFoundException
    {
        DriverDO driverDO = findDriverChecked(driverId);
        driverDO.setCoordinate(new GeoCoordinate(latitude, longitude));
    }


    /**
     * Find all drivers by online state.
     *
     * @param onlineStatus
     * @return List<DriverDO>
     */
    @Override
    public List<DriverDO> find(OnlineStatus onlineStatus)
    {
        return driverRepository.findByOnlineStatus(onlineStatus);
    }


    private DriverDO findDriverChecked(Long driverId) throws EntityNotFoundException
    {
        return driverRepository.findById(driverId)
            .orElseThrow(() -> new EntityNotFoundException("Could not find entity with id: " + driverId));
    }
	
    
    /**
     * Allocate bus to driver
     * First check if driver is online
     * And then make sure bus exists
     * In case the bus is to an other driver allocated the method throws a {@code BusAlreadyInUseException } 
     * @throws EntityNotFoundException if given bus or driver do not exist
     * @throws WrongUserStatus if driver is offline
     * @throws BusAlreadyInUseException if bus already selected
     * @throws DriverProfileDeactivatedException 
     * @return DriverDO
     */
	@Override
    @Transactional
    public DriverDO selectBus(Long driverId, String licensePlate) throws EntityNotFoundException, WrongUserStatus, BusAlreadyInUseException, DriverProfileDeactivatedException {
        DriverDO myDriver = findDriverChecked(driverId);

        if(myDriver.getOnlineStatus().equals(OnlineStatus.OFFLINE)){
        	LOG.warn("Driver with Id:"+ driverId +" is not logged in (ONLINE) ");
            throw new WrongUserStatus("Driver with Id: "+ driverId +" is not logged in (ONLINE) ");
        }
        
        if(myDriver.getDeleted()==true){
        	LOG.warn("The profile of driver with Id:"+ driverId +" is deactivated.");
            throw new DriverProfileDeactivatedException("The profile of driver with Id: "+ driverId +" is deactivated.");
        }

        Optional<BusDO> bus = this.myBusRepo.findBylicenseplate(licensePlate);
        if(!bus.isPresent()){
        	LOG.warn("Bus with license plate: " + licensePlate+" does not exists.");
            throw new EntityNotFoundException("Bus with license plate: " + licensePlate+" does not exist.");
        }
        BusDO myBus=bus.get();
        if(myBus.getDriverDetails()!=null ){
        	LOG.warn("Bus with license plate: " + licensePlate + " already in use by driver with Id: "+bus.get().getDriverDetails().getId());
            throw new BusAlreadyInUseException("Bus with license plate: " + licensePlate + " already in use by driver with Id: "+bus.get().getDriverDetails().getId() );
        }
        
        myDriver.setBusDetails(myBus);
        myBus.setDriverDetails(myDriver);
        

        return myDriver;
    }
	
	@Override
    @Transactional
    public DriverDO deselectBus(Long driverId) throws EntityNotFoundException {
        DriverDO driver = findDriverChecked(driverId);
        BusDO myBus=driver.getBusDetails();
        
        if(myBus!=null) {
        	myBus.setDriverDetails(null);
        }
        driver.setBusDetails(null);
        
        return driver;
    }

	
	/**
	 * The method filters the drivers from the data base according to the criteria that are given over the API.<br><br>
	 * If the first argument <code>BusCriteriaGroup myBusCriteria</code>  is empty or null,<br>
	 * then the method assumes that no filtering should be done to the driver list depending on the bus.<br>
	 * In this case it just filters directly the driver list depending on the driver attributes parsed into <code>DriverCriteriaGroup myDriverCriteria</code>.<br><br>
	 * 
	 * If the second argument <code>DriverCriteriaGroup myDriverCriteria</code>  is empty or null,<br>
	 * then it filters the list only according to the bus attributes
	 * Otherwise it first filter the driver list based on the bus attributes and then on the drivers attributes.<br>
	 * @return List<DriverToFilteringDTO>
	 */
	@Override
	public List<DriverDO> filterDrivers(BusCriteriaGroup myBusCriteria,
			DriverCriteriaGroup myDriverCriteria) {
		
		List<DriverDO> driversList=new ArrayList<DriverDO>();
		
		driverRepository.findAll().forEach(driversList::add);		
		
		//Check if bus criteria was parsed
		//if this is the case then filter drivers with bus
		if(myBusCriteria!=null 
				&& myBusCriteria.getBusCriteriaList() !=null
				&& !myBusCriteria.getBusCriteriaList().isEmpty()) {
			driversList=driversList.stream()
					.filter(driver->driver.getBusDetails()!=null)
					.collect(Collectors.toList())
					.stream()
					.filter(myBusCriteria.generateCombinedBusPredicate())
					.collect(Collectors.toList());
		}
		
		//Check if driver criteria was parsed
		if(myDriverCriteria!=null 
				&& myDriverCriteria.getDriverCriteriaList() !=null
				&& !myDriverCriteria.getDriverCriteriaList().isEmpty()) {
			driversList=driversList.stream()
					.filter(myDriverCriteria.generateDriverPredicatesList())
					.collect(Collectors.toList());
		}	
		
		return driversList;
	}	
	
	@Override
	public List<DriverDO> filterDriversWithSpecifications(CriteriaGroup myCriteria){
		return this.driverRepository.findAll(myCriteria.build());
	}
	
}
