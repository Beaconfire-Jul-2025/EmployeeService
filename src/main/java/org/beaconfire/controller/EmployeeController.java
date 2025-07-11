package org.beaconfire.controller;

import lombok.RequiredArgsConstructor;
import org.beaconfire.dto.*;
import org.beaconfire.model.Employee;
import org.beaconfire.service.EmployeeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/employee")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getEmployee(@PathVariable String id) {
        Employee employee = employeeService.getEmployeeById(id);

        GetEmployeeResponse response = new GetEmployeeResponse(employee, "Employee profile retrieved");

        return ResponseEntity.ok(response);
    }

}

