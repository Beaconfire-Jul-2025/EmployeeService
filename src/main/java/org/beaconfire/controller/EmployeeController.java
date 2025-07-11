package org.beaconfire.controller;

import lombok.RequiredArgsConstructor;


import org.beaconfire.dto.UpdateEmployeeRequest;
import org.beaconfire.service.EmployeeService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;


@RestController
@RequestMapping("/employee")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @PutMapping("/{id}")
    public ResponseEntity<?> updateEmployee(@PathVariable String id, @RequestBody UpdateEmployeeRequest request) {
        employeeService.updateEmployee(id,request);
        return ResponseEntity.ok(Collections.singletonMap("message", "Employee profile updated"));
    }

}
