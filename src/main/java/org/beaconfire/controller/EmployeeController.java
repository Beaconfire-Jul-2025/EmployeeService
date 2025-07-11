package org.beaconfire.controller;

import lombok.RequiredArgsConstructor;
import org.beaconfire.dto.*;
import org.beaconfire.model.Employee;
import org.beaconfire.model.PersonalDocument;
import org.beaconfire.service.EmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/employee")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;
    @PostMapping
    public ResponseEntity<?> createEmployee(@RequestBody CreateEmployeeRequest request) {
        Employee savedEmployee = employeeService.registerEmployee(request);

        CreateEmployeeResponse response = new CreateEmployeeResponse(
                savedEmployee.getId(),
                "Employee profile created"
        );

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getEmployee(@PathVariable String id) {
        Employee employee = employeeService.getEmployeeById(id);

        GetEmployeeResponse response = new GetEmployeeResponse(employee, "Employee profile retrieved");

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateEmployee(@PathVariable String id, @RequestBody UpdateEmployeeRequest request) {
        employeeService.updateEmployee(id,request);
        return ResponseEntity.ok(Collections.singletonMap("message", "Employee profile updated"));
    }

    @PostMapping("/{id}/documents")
    public ResponseEntity<Map<String, String>> uploadDocument(
            @PathVariable("id") String employeeId,
            @RequestBody UploadDocumentRequest request) {
        employeeService.uploadDocument(employeeId, request);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Document uploaded");
        return ResponseEntity.status(201).body(response);
    }



}

