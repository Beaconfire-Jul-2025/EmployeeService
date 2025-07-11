package org.beaconfire.service;

import lombok.RequiredArgsConstructor;
import org.beaconfire.exception.EmployeeNotFoundException;
import org.beaconfire.model.Employee;
import org.beaconfire.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    @Override
    public Employee getEmployeeById(String id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));
    }

}

