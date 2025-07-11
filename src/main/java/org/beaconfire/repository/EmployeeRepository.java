package org.beaconfire.repository;

import org.beaconfire.model.Employee;
import org.springframework.data.mongodb.repository.MongoRepository;

@Repository
public interface EmployeeRepository extends MongoRepository<Employee, String> {
    boolean existsByEmail(String email);

}

