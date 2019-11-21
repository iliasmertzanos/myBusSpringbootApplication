package com.myBusApp.service.Bus;

import java.util.List;

import com.myBusApp.datatransferobject.BusToUpdateDTO;
import com.myBusApp.domainobject.BusDO;
import com.myBusApp.domainobject.DriverDO;
import com.myBusApp.exception.ConstraintsViolationException;
import com.myBusApp.exception.EntityNotFoundException;

public interface BusInterfaceService {

    BusDO find(long id) throws EntityNotFoundException;
    
    public BusDO findBylicenseplate(String licensePlate) throws EntityNotFoundException;

    BusDO create(BusDO bus) throws ConstraintsViolationException;

    List<BusDO> findAll();

    void delete(String licensePlate) throws EntityNotFoundException;

    BusDO updateRating(String licensePlate, Integer rating) throws EntityNotFoundException;
    
    public BusDO updateFields(String licensePLate, BusToUpdateDTO busWithNewValues) throws EntityNotFoundException;
    
    public DriverDO getDriverFromBus(String licensePLate) throws EntityNotFoundException ;
	
}
