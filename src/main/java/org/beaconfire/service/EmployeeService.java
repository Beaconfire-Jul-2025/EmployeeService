package org.beaconfire.service;

import org.beaconfire.dto.CreateEmployeeRequest;
import org.beaconfire.model.Employee;

public interface EmployeeService {

    Employee getEmployeeById(String id);
    Employee registerEmployee(CreateEmployeeRequest request);


}