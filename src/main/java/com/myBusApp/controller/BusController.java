package com.myBusApp.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.myBusApp.datatransferobject.BusDTO;
import com.myBusApp.datatransferobject.BusToUpdateDTO;
import com.myBusApp.datatransferobject.DriverWithBusDTO;
import com.myBusApp.domainobject.BusDO;
import com.myBusApp.domainobject.DriverDO;
import com.myBusApp.exception.ConstraintsViolationException;
import com.myBusApp.exception.EntityNotFoundException;
import com.myBusApp.service.Bus.BusInterfaceService;

import javax.validation.Valid;
@RestController
@RequestMapping("v1/bus")
public class BusController {

    private BusInterfaceService busService;
    
    private ModelMapper modelMapper = new ModelMapper();

    @Autowired
    public BusController(final BusInterfaceService busService)
    {
        this.busService = busService;
    }
    
    @GetMapping("/{busId}")
    public BusDTO getBus(@PathVariable long busId) throws EntityNotFoundException {
    	return this.modelMapper.map(busService.find(busId),BusDTO.class);
    }

    @GetMapping("licencePlate/{licensePlate}")
    public BusDTO getBus(@PathVariable String licensePlate) throws EntityNotFoundException {
    	BusDTO myBus=this.modelMapper.map(busService.findBylicenseplate(licensePlate),BusDTO.class);
    	return myBus;
    }
    
    @GetMapping("driverFromBus/{licensePlate}")
    public DriverWithBusDTO getDriverFromBus(@PathVariable String licensePlate) throws EntityNotFoundException {
    	DriverDO myDriverDetails=busService.getDriverFromBus(licensePlate);
    	DriverWithBusDTO driverDetails=this.modelMapper.map(myDriverDetails,DriverWithBusDTO.class);
    	return driverDetails;
    }
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BusDTO createBus(@Valid @RequestBody BusDTO busDTO) throws ConstraintsViolationException {
    	
    	BusDO bus =this.modelMapper.map(busDTO,BusDO.class);
        bus = busService.create(bus);
        return this.modelMapper.map(bus,BusDTO.class);
    }


    @DeleteMapping("/{licensePlate}")
    public void deleteBus(@PathVariable String licensePlate) throws EntityNotFoundException {
        busService.delete(licensePlate);
    }
    
    @PutMapping("/{licensePlate}")
    public BusDTO updateFields(@PathVariable String licensePlate,@Valid @RequestBody BusToUpdateDTO busWithNewValues) throws EntityNotFoundException {
    	
    	return this.modelMapper.map(busService.updateFields(licensePlate, busWithNewValues),BusDTO.class);
    }
    
    
    
    
}
