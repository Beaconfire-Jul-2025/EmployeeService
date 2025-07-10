package org.beaconfire.controller;

import lombok.RequiredArgsConstructor;
import org.beaconfire.dto.*;
import org.beaconfire.model.Employee;
import org.beaconfire.model.PersonalDocument;
import org.beaconfire.repository.EmployeeRepository;
import org.beaconfire.service.EmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;


@RestController
@RequestMapping("/employee")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;
    private final EmployeeRepository employeeRepository;

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
        employeeService.updateEmployee(id, request);
        return ResponseEntity.ok(Collections.singletonMap("message", "Employee profile updated"));
    }

    @GetMapping("/{id}/documents")
    public ResponseEntity<?> getDocuments(@PathVariable String id) {
        List<PersonalDocument> docs = employeeService.getEmployeeDocuments(id);

        GetDocumentsResponse response = new GetDocumentsResponse(docs, "Documents retrieved");
        return ResponseEntity.ok(response);
    }
    //sudo path for test the function ,wait for AWS key
    @PostMapping("/{id}/documents")
    public ResponseEntity<?> addDocument(@PathVariable String id, @RequestBody UploadDocumentRequest request) {
        employeeService.addDocumentToEmployee(id, request);
        return new ResponseEntity<>(Collections.singletonMap("message", "Document uploaded"), HttpStatus.CREATED);
    }



//    @PostMapping("/{id}/documents")
//    public ResponseEntity<?> uploadDocument(
//            @PathVariable String id,
//            @RequestParam("file") MultipartFile file,
//            @RequestParam("title") String title) {
//
//        String url = employeeService.uploadDocument(file, id, title);
//
//        return new ResponseEntity<>(new GenericResponse("Document uploaded to: " + url), HttpStatus.CREATED);
//    }
}

