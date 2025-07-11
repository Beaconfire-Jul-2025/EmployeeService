package org.beaconfire.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


import org.beaconfire.dto.UpdateEmployeeRequest;
import org.beaconfire.model.Employee;
import org.beaconfire.exception.EmployeeNotFoundException;
import org.beaconfire.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Override
    public Employee updateEmployee(String id, UpdateEmployeeRequest request) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));

        employee.setFirstName(request.getFirstName());
        employee.setLastName(request.getLastName());
        employee.setMiddleName(request.getMiddleName());
        employee.setPreferredName(request.getPreferredName());
        employee.setCellPhone(request.getCellPhone());
        employee.setAlternatePhone(request.getAlternatePhone());
        employee.setGender(request.getGender());
        employee.setSsn(request.getSsn());
        employee.setDob(request.getDob());
        employee.setStartDate(request.getStartDate());
        employee.setEndDate(request.getEndDate());
        employee.setWorkAuthType(request.getWorkAuthType());
        employee.setWorkAuthStartDate(request.getWorkAuthStartDate());
        employee.setWorkAuthEndDate(request.getWorkAuthEndDate());
        employee.setDriverLicense(request.getDriverLicense());
        employee.setDriverLicenseExpiration(request.getDriverLicenseExpiration());
        employee.setLastModificationDate(LocalDateTime.now());

        return employeeRepository.save(employee);
    }


}