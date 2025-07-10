package org.beaconfire.service;

import org.beaconfire.dto.CreateEmployeeRequest;
import org.beaconfire.dto.UpdateEmployeeRequest;
import org.beaconfire.dto.UploadDocumentRequest;
import org.beaconfire.model.Employee;
import org.beaconfire.model.PersonalDocument;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {

    Employee registerEmployee(CreateEmployeeRequest request);

    Employee getEmployeeById(String id);

    void addDocumentToEmployee(String id, UploadDocumentRequest request);

    Employee updateEmployee(String id, UpdateEmployeeRequest request);

    List<PersonalDocument> getEmployeeDocuments(String id); //sudo path for test the function ,wait for AWS key

//    String uploadDocument(MultipartFile file, String employeeId, String docTitle);
   void deleteEmployee(String id);
    List<Employee> getAllEmployees();
    Optional<Employee> getEmployeeByEmail(String email);
}
