package org.beaconfire.service;

import org.beaconfire.dto.CreateEmployeeRequest;
import org.beaconfire.dto.GetDocumentsResponse;
import org.beaconfire.dto.UpdateEmployeeRequest;
import org.beaconfire.dto.UploadDocumentRequest;
import org.beaconfire.model.Employee;


public interface EmployeeService {

    Employee getEmployeeById(String id);
    Employee registerEmployee(CreateEmployeeRequest request);
    Employee updateEmployee(String id, UpdateEmployeeRequest request);
    void uploadDocument(String employeeId, UploadDocumentRequest request);
    GetDocumentsResponse getDocumentsByEmployeeId(String employeeId);


}