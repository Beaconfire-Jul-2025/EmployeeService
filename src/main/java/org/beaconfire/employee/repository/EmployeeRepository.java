package org.beaconfire.employee.repository;

import org.beaconfire.employee.model.Employee;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends MongoRepository<Employee, String> {
    // Additional query methods can be defined here if needed
}

