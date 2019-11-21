package com.myBusApp.controller;

import com.myBusApp.controller.mapper.DriverMapper;
import com.myBusApp.datatransferobject.DriverDTO;
import com.myBusApp.datatransferobject.DriverWithBusDTO;
import com.myBusApp.domainobject.DriverDO;
import com.myBusApp.domainvalue.BusAttributeField;
import com.myBusApp.domainvalue.DriverAttributeField;
import com.myBusApp.domainvalue.OnlineStatus;
import com.myBusApp.domainvalue.RelationalOperator;
import com.myBusApp.exception.BusAlreadyInUseException;
import com.myBusApp.exception.ConstraintsViolationException;
import com.myBusApp.exception.DriverProfileDeactivatedException;
import com.myBusApp.exception.EntityNotFoundException;
import com.myBusApp.exception.InvalidFieldException;
import com.myBusApp.exception.InvalidRelationalOperatorException;
import com.myBusApp.exception.InvalidValueException;
import com.myBusApp.exception.NotSupportedFromFieldException;
import com.myBusApp.exception.WrongUserStatus;
import com.myBusApp.filtering.BusCriteriaGroup;
import com.myBusApp.filtering.BusSingleCriteria;
import com.myBusApp.filtering.CriteriaGroup;
import com.myBusApp.filtering.DriverCriteriaGroup;
import com.myBusApp.filtering.DriverSingleCriteria;
import com.myBusApp.filtering.GroupedCriteria;
import com.myBusApp.service.driver.DriverService;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * All operations with a driver will be routed by this controller.
 * <p/>
 */
@RestController
@RequestMapping("v1/drivers")
public class DriverController
{

    private final DriverService driverService;
    
    private ModelMapper modelMapper = new ModelMapper();

    @Autowired
    public DriverController(final DriverService driverService)
    {
        this.driverService = driverService;
    }


    @GetMapping("/{driverId}")
    public DriverDTO getDriver(@PathVariable long driverId) throws EntityNotFoundException
    {
        return DriverMapper.makeDriverDTO(driverService.find(driverId));
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DriverDTO createDriver(@Valid @RequestBody DriverDTO driverDTO) throws ConstraintsViolationException
    {
        DriverDO driverDO = DriverMapper.makeDriverDO(driverDTO);
        return DriverMapper.makeDriverDTO(driverService.create(driverDO));
    }


    @DeleteMapping("/{driverId}")
    public void deleteDriver(@PathVariable long driverId) throws EntityNotFoundException
    {
        driverService.delete(driverId);
    }


    @PutMapping("/{driverId}")
    public void updateLocation(
        @PathVariable long driverId, @RequestParam double longitude, @RequestParam double latitude)
        throws EntityNotFoundException
    {
        driverService.updateLocation(driverId, longitude, latitude);
    }


    @GetMapping
    public List<DriverDTO> findDrivers(@RequestParam OnlineStatus onlineStatus)
    {
        return DriverMapper.makeDriverDTOList(driverService.find(onlineStatus));
    }
    
    @PutMapping("select/{driverId}")
    public DriverWithBusDTO selectBus(@PathVariable long driverId,@RequestParam String licensePlate) 
    		throws EntityNotFoundException, WrongUserStatus, BusAlreadyInUseException, DriverProfileDeactivatedException
    {
    	return new ModelMapper().map(driverService.selectBus(driverId,licensePlate)
    			,DriverWithBusDTO.class);
    }
    
    @PutMapping("deselect/{driverId}")
    public DriverWithBusDTO deselectBus(@PathVariable long driverId) 
    		throws EntityNotFoundException, WrongUserStatus, BusAlreadyInUseException
    {
    	return new ModelMapper().map(driverService.deselectBus(driverId),DriverWithBusDTO.class);
    }
    
    /**
     * @param myGroupCriteria accepts bus or drivers attributes that are defined in the Enums: 
     * BusAttributeField and DriverAttributeFiels in the package com.myBusApp.domainvalue
     * @return
     * @throws InvalidValueException
     * @throws InvalidRelationalOperatorException
     * @throws InvalidFieldException
     * @throws NotSupportedFromFieldException
     */
    @PostMapping("/filter")
    public List<DriverWithBusDTO> filterDrivers(@RequestBody  GroupedCriteria myGroupCriteria) 
    				throws InvalidValueException, InvalidRelationalOperatorException, InvalidFieldException, NotSupportedFromFieldException
    {
    	
    	BusCriteriaGroup myBusCriteria=myGroupCriteria.getMyBusCriteria();
    	
    	DriverCriteriaGroup myDriverCriteria=myGroupCriteria.getMyDriverCriteria();
    	
    	// Validate input data
    	// Although no invalid enum parameter would pass, because before even getting here 
    	// there would be a deserialization exception, methods for case some wrong values come at this point are provided
    	if(myBusCriteria!=null && myBusCriteria.getBusCriteriaList()!=null ) {
	    	
	    	for(BusSingleCriteria busCriteria:myBusCriteria.getBusCriteriaList()) {
	    		RelationalOperator myRelationalOperator=busCriteria.getRelationalOperator();
	    		BusAttributeField myField=busCriteria.getField();
	    		if(!busCriteria.isValueValid()) {
	    			throw new InvalidValueException("Bus Field "+ myField+" has an invalid value: "+busCriteria.getValue());
	    		}else if(!busCriteria.isRelationalOperatorValid()) {
	    			throw new InvalidRelationalOperatorException(" Field "+ myField+" has an invalid relational operator "+myRelationalOperator);
	    		}else if(!busCriteria.isBusAttributeValid()) {
	    			throw new InvalidFieldException("Bus Field "+ myField+" does not exists.");
	    		}else if(!busCriteria.isRelationalOperatorSupported()) {
	    			throw new NotSupportedFromFieldException("Bus Field "+ myField+" does not support this type of relational operator: "+myRelationalOperator);
	    		}
	    	}
    	}
    	
    	if(myDriverCriteria!=null && myDriverCriteria.getDriverCriteriaList()!=null) {
	    	for(DriverSingleCriteria driverCriteria:myDriverCriteria.getDriverCriteriaList()) {
	    		DriverAttributeField myField=driverCriteria.getField();
	    		RelationalOperator myRelationalOperator=driverCriteria.getRelationalOperator();
	    		if(!driverCriteria.isValueValid()) {
	    			throw new InvalidValueException("Driver Field "+ myField+" has an invalid value: "+driverCriteria.getValue());
	    		}else if(!driverCriteria.isRelationalOperatorValid()) {
	    			throw new InvalidRelationalOperatorException("Driver Field "+ myField+" has an invalid relational operator "+myRelationalOperator);
	    		}else if(!driverCriteria.isBusAttributeValid()) {
	    			throw new InvalidFieldException("Driver Field "+ myField+" does not exists.");
	    		}else if(!driverCriteria.isRelationalOperatorSupported()) {
	    			throw new NotSupportedFromFieldException("Driver Field "+ myField+ " does not support this type of relational operator: "+myRelationalOperator);
	    		}
	    	}
    	}	
    	
    	List<DriverDO> myFilteredDriverList =driverService.filterDrivers(myBusCriteria, myDriverCriteria);
    	
        return myFilteredDriverList.stream()
        		.map( driver->modelMapper.map(driver,DriverWithBusDTO.class) )
        		.collect(Collectors.toList());
        		
    }
    
    /** The method differentiates from the previous one, because it doesn't retrieve all the records of drivers
     * and then filters them.
     * Instead it parses directly the predicates in the Specifications. 
     * Afterwards JPA creates an equivalent sql statement and retrieves only those records 
     * that meet the criteria that was given over the API
     * @param myGroupCriteria accepts bus or/and driver attributes that are defined in the Enum:
     * AttributeField in the package com.myBusApp.domainvalue. 
     * @return
     */
    @PostMapping("/coolFilter")
    public List<DriverWithBusDTO> filterDriversUsingCriteria(@RequestBody  CriteriaGroup myGroupCriteria){
    	return this.driverService.filterDriversWithSpecifications(myGroupCriteria).stream()
        		.map( driver->modelMapper.map(driver,DriverWithBusDTO.class) )
        		.collect(Collectors.toList());
    }
    
}
