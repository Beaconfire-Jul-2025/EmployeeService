package org.beaconfire.controller;

import lombok.RequiredArgsConstructor;
import org.beaconfire.dto.*;
import org.beaconfire.model.Employee;
import org.beaconfire.service.EmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;
    @PostMapping("/validate-info")
    public ResponseEntity<ValidateEmployeeInfoResponse>  validateEmployeeInfo(@RequestBody @Valid ValidateEmployeeInfoRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = (String) authentication.getPrincipal();
        String username = (String) authentication.getDetails();

        System.out.println("validateEmployeeInfo - userId: " + userId + ", username: " + username);

        String employeeId = employeeService.validateEmployeeInfo(request);

        ValidateEmployeeInfoResponse response = new ValidateEmployeeInfoResponse(
                employeeId,
                "Onboarding application submitted successfully. Please wait for HR review."
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<?> createEmployee(@RequestBody CreateEmployeeRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = (String) authentication.getPrincipal();
        String username = (String) authentication.getDetails();

        System.out.println("createEmployee - userId: " + userId + ", username: " + username);

        Employee savedEmployee = employeeService.registerEmployee(request);

        CreateEmployeeResponse response = new CreateEmployeeResponse(
                savedEmployee.getId(),
                "Employee profile created"
        );

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getEmployee(@PathVariable String id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = (String) authentication.getPrincipal();
        String username = (String) authentication.getDetails();

        System.out.println("getEmployee - userId: " + userId + ", username: " + username);

        Employee employee = employeeService.getEmployeeById(id);

        GetEmployeeResponse response = new GetEmployeeResponse(employee, "Employee profile retrieved");

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateEmployee(@PathVariable String id, @RequestBody UpdateEmployeeRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = (String) authentication.getPrincipal();
        String username = (String) authentication.getDetails();

        System.out.println("updateEmployee - userId: " + userId + ", username: " + username);

        employeeService.updateEmployee(id, request);
        return ResponseEntity.ok(Collections.singletonMap("message", "Employee profile updated"));
    }

    @PostMapping("/{id}/documents")
    public ResponseEntity<Map<String, String>> uploadDocument(
            @PathVariable("id") String employeeId,
            @RequestBody UploadDocumentRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = (String) authentication.getPrincipal();
        String username = (String) authentication.getDetails();

        System.out.println("uploadDocument - userId: " + userId + ", username: " + username);

        employeeService.uploadDocument(employeeId, request);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Document uploaded");
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("/{id}/documents")
    public ResponseEntity<GetDocumentsResponse> getDocuments(@PathVariable("id") String employeeId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = (String) authentication.getPrincipal();
        String username = (String) authentication.getDetails();

        System.out.println("getDocuments - userId: " + userId + ", username: " + username);

        GetDocumentsResponse response = employeeService.getDocumentsByEmployeeId(employeeId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/documents")
    public ResponseEntity<Map<String, String>> updateDocument(
            @PathVariable("id") String employeeId,
            @RequestBody UpdateDocumentRequest request) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = (String) authentication.getPrincipal();
        String username = (String) authentication.getDetails();

        System.out.println("updateDocument - userId: " + userId + ", username: " + username);

        employeeService.updateDocument(employeeId, request);
        return ResponseEntity.ok(Collections.singletonMap("message", "Document updated successfully"));
    }
    @GetMapping("/house/{houseId}")
    public ResponseEntity<List<GetEmployeeByHouseResponse>> getEmployeesByHouse(@PathVariable String houseId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = (String) authentication.getPrincipal();
        String username = (String) authentication.getDetails();

        System.out.println("getEmployeesByHouse - userId: " + userId + ", username: " + username + ", houseId: " + houseId);
        List<GetEmployeeByHouseResponse> employees = employeeService.getEmployeesByHouseId(houseId);
        return ResponseEntity.ok(employees);
    }

}

