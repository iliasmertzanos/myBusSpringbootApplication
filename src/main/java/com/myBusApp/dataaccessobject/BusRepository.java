package com.myBusApp.dataaccessobject;

import com.myBusApp.domainobject.BusDO;
import com.myBusApp.exception.EntityNotFoundException;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

/**
 * Database Access Object for driver table.
 * <p/>
 */
public interface BusRepository extends CrudRepository<BusDO, Long>
{

	Optional<BusDO> findBylicenseplate(String licenseplate) throws EntityNotFoundException ;
}
