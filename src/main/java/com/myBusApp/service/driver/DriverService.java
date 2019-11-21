package com.myBusApp.service.driver;

import com.myBusApp.domainobject.DriverDO;
import com.myBusApp.domainvalue.OnlineStatus;
import com.myBusApp.exception.BusAlreadyInUseException;
import com.myBusApp.exception.ConstraintsViolationException;
import com.myBusApp.exception.DriverProfileDeactivatedException;
import com.myBusApp.exception.EntityNotFoundException;
import com.myBusApp.exception.WrongUserStatus;
import com.myBusApp.filtering.BusCriteriaGroup;
import com.myBusApp.filtering.CriteriaGroup;
import com.myBusApp.filtering.DriverCriteriaGroup;

import java.util.List;

public interface DriverService
{

    DriverDO find(Long driverId) throws EntityNotFoundException;

    DriverDO create(DriverDO driverDO) throws ConstraintsViolationException;

    void delete(Long driverId) throws EntityNotFoundException;

    void updateLocation(long driverId, double longitude, double latitude) throws EntityNotFoundException;

    List<DriverDO> find(OnlineStatus onlineStatus);
    
    DriverDO selectBus(Long driverId, String licensePlate) throws DriverProfileDeactivatedException, EntityNotFoundException, WrongUserStatus, BusAlreadyInUseException ;
    
    DriverDO deselectBus(Long driverId)throws EntityNotFoundException;
    
    List<DriverDO> filterDrivers(BusCriteriaGroup myBusCriteria, DriverCriteriaGroup myDriverCriteria);
    
    public List<DriverDO> filterDriversWithSpecifications(CriteriaGroup myCriteria);

}
