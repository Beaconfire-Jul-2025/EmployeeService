package org.beaconfire.service;

import org.beaconfire.dto.*;
import org.beaconfire.model.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface EmployeeService {
    String createEmployee(CreateEmployeeRequest request);
    String validateEmployeeInfo(ValidateEmployeeInfoRequest request);
    Employee registerEmployee(CreateEmployeeRequest request);
    Employee updateEmployee(String id, UpdateEmployeeRequest request);
    void uploadDocument(String employeeId, UploadDocumentRequest request);
    GetDocumentsResponse getDocumentsByEmployeeId(String employeeId);
    void updateDocument(String employeeId, UpdateDocumentRequest request);
    GetEmployeeResponse getEmployeeProfileById(String id);

    List<GetEmployeeByHouseResponse> getEmployeesByHouseId(String houseId);

    List<GetEmployeeResponse> getAllEmployees();

    List<GetEmployeeResponse> searchEmployeesByName(String name);

    Page<GetEmployeeResponse> getAllEmployeesPaged(Pageable pageable);

    Page<GetEmployeeResponse> searchEmployeesByNamePaged(String name, Pageable pageable);
    Page<GetEmployeeByHouseResponse> getEmployeesByHouseIdPaged(String houseId, Pageable pageable);

}