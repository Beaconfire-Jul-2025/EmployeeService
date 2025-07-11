package org.beaconfire.service;


import org.beaconfire.dto.UpdateEmployeeRequest;
import org.beaconfire.model.Employee;

public interface EmployeeService {

    Employee updateEmployee(String id, UpdateEmployeeRequest request);


}