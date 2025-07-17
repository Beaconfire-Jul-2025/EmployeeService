package org.beaconfire.employee.repository;

import org.beaconfire.employee.model.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface EmployeeRepository extends MongoRepository<Employee, String> {
    Optional<Employee> findByEmail(String email);
    boolean existsByEmail(String email);
    List<Employee> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(String firstName, String lastName);
    Page<Employee> findByHouseId(String houseId, Pageable pageable);
    Page<Employee> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(String firstName, String lastName, Pageable pageable);
    Optional<Employee> findByUserId(String userId);

}
