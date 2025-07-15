package org.beaconfire.controller;

import lombok.RequiredArgsConstructor;
import org.beaconfire.dto.*;
import org.beaconfire.model.Employee;
import org.beaconfire.service.EmployeeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.HashMap;
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
    public ResponseEntity<GetEmployeeResponse> getEmployee(@PathVariable String id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = (String) authentication.getPrincipal();
        String username = (String) authentication.getDetails();

        System.out.println("getEmployee - userId: " + userId + ", username: " + username);

        GetEmployeeResponse response = employeeService.getEmployeeProfileById(id);
        return ResponseEntity.ok(response);
    }
    @GetMapping
    public ResponseEntity<PageListResponse<GetEmployeeResponse>> getEmployees(
            @RequestParam(value = "name", required = false) String fullName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = (String) authentication.getPrincipal();
        String username = (String) authentication.getDetails();
        System.out.println("getEmployees - userId: " + userId + ", username: " + username + ", fullName: " + fullName);

        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<GetEmployeeResponse> employeePage;
        if (fullName != null && !fullName.isEmpty()) {
            employeePage = employeeService.searchEmployeesByNamePaged(fullName, pageable);
        } else {
            employeePage = employeeService.getAllEmployeesPaged(pageable);
        }

        PageListResponse<GetEmployeeResponse> response = PageListResponse.<GetEmployeeResponse>builder()
                .list(employeePage.getContent())
                .current(employeePage.getNumber() + 1)
                .pageSize(employeePage.getSize())
                .total(employeePage.getTotalElements())
                .build();

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
    public ResponseEntity<PageListResponse<GetEmployeeByHouseResponse>> getEmployeesByHouse(
            @PathVariable String houseId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = (String) authentication.getPrincipal();
        String username = (String) authentication.getDetails();
        System.out.println("getEmployeesByHouse - userId: " + userId + ", username: " + username + ", houseId: " + houseId);

        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<GetEmployeeByHouseResponse> employeePage = employeeService.getEmployeesByHouseIdPaged(houseId, pageable);

        PageListResponse<GetEmployeeByHouseResponse> response = PageListResponse.<GetEmployeeByHouseResponse>builder()
                .list(employeePage.getContent())
                .current(employeePage.getNumber() + 1) // Page 是从 0 开始，所以要 +1
                .pageSize(employeePage.getSize())
                .total(employeePage.getTotalElements())
                .build();

        return ResponseEntity.ok(response);
    }


}

