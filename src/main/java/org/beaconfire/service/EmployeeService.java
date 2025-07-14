package org.beaconfire.service;

import org.beaconfire.dto.*;
import org.beaconfire.model.Employee;

import java.util.List;


public interface EmployeeService {
    String createEmployee(CreateEmployeeRequest request);
    String validateEmployeeInfo(ValidateEmployeeInfoRequest request);
    Employee getEmployeeById(String id);
    Employee registerEmployee(CreateEmployeeRequest request);
    Employee updateEmployee(String id, UpdateEmployeeRequest request);
    void uploadDocument(String employeeId, UploadDocumentRequest request);
    GetDocumentsResponse getDocumentsByEmployeeId(String employeeId);
    void updateDocument(String employeeId, UpdateDocumentRequest request);
    GetEmployeeResponse getEmployeeProfileById(String id);

    List<GetEmployeeByHouseResponse> getEmployeesByHouseId(String houseId);

    List<GetEmployeeResponse> searchEmployeesByName(String name);

    List<GetEmployeeResponse> getAllEmployees();
}