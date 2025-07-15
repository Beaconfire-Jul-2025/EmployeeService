package org.beaconfire.employee.exception;


public class EmployeeNotFoundException extends RuntimeException {
    public EmployeeNotFoundException(String id) {
        super("Employee with ID " + id + " not found");
    }
}
