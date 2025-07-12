package org.beaconfire.service;

import org.beaconfire.dto.*;
import org.beaconfire.model.Employee;


public interface EmployeeService {
    String validateEmployeeInfo(ValidateEmployeeInfoRequest request);
    Employee getEmployeeById(String id);
    Employee registerEmployee(CreateEmployeeRequest request);
    Employee updateEmployee(String id, UpdateEmployeeRequest request);
    void uploadDocument(String employeeId, UploadDocumentRequest request);
    GetDocumentsResponse getDocumentsByEmployeeId(String employeeId);
    void updateDocument(String employeeId, UpdateDocumentRequest request);



}