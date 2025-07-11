package org.beaconfire.service;

import lombok.RequiredArgsConstructor;
import org.beaconfire.dto.CreateEmployeeRequest;
import org.beaconfire.dto.GetDocumentsResponse;
import org.beaconfire.dto.UpdateEmployeeRequest;
import org.beaconfire.dto.UploadDocumentRequest;
import org.beaconfire.exception.EmployeeAlreadyExistsException;
import org.beaconfire.exception.EmployeeNotFoundException;
import org.beaconfire.model.Employee;
import org.beaconfire.model.PersonalDocument;
import org.beaconfire.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Override
    public Employee updateEmployee(String id, UpdateEmployeeRequest request) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));

        employee.setFirstName(request.getFirstName());
        employee.setLastName(request.getLastName());
        employee.setMiddleName(request.getMiddleName());
        employee.setPreferredName(request.getPreferredName());
        employee.setCellPhone(request.getCellPhone());
        employee.setAlternatePhone(request.getAlternatePhone());
        employee.setGender(request.getGender());
        employee.setSsn(request.getSsn());
        employee.setDob(request.getDob());
        employee.setStartDate(request.getStartDate());
        employee.setEndDate(request.getEndDate());
        employee.setWorkAuthType(request.getWorkAuthType());
        employee.setWorkAuthStartDate(request.getWorkAuthStartDate());
        employee.setWorkAuthEndDate(request.getWorkAuthEndDate());
        employee.setDriverLicense(request.getDriverLicense());
        employee.setDriverLicenseExpiration(request.getDriverLicenseExpiration());
        employee.setLastModificationDate(LocalDateTime.now());

        return employeeRepository.save(employee);
    }
    @Override
    public Employee registerEmployee(CreateEmployeeRequest request) {
        if (employeeRepository.existsByEmail(request.getEmail())) {
            throw new EmployeeAlreadyExistsException(request.getEmail());
        }
        Employee employee = Employee.builder()
                .userId(request.getUserId())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .middleName(request.getMiddleName())
                .email(request.getEmail())
                .activeFlag(true)
                .createDate(LocalDateTime.now())
                .lastModificationDate(LocalDateTime.now())
                .build();
        employee.setPersonalDocuments(new ArrayList<>());

        return employeeRepository.save(employee);
    }
    @Override
    public Employee updateEmployee(String id, UpdateEmployeeRequest request) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));

        employee.setFirstName(request.getFirstName());
        employee.setLastName(request.getLastName());
        employee.setMiddleName(request.getMiddleName());
        employee.setPreferredName(request.getPreferredName());
        employee.setCellPhone(request.getCellPhone());
        employee.setAlternatePhone(request.getAlternatePhone());
        employee.setGender(request.getGender());
        employee.setSsn(request.getSsn());
        employee.setDob(request.getDob());
        employee.setStartDate(request.getStartDate());
        employee.setEndDate(request.getEndDate());
        employee.setWorkAuthType(request.getWorkAuthType());
        employee.setWorkAuthStartDate(request.getWorkAuthStartDate());
        employee.setWorkAuthEndDate(request.getWorkAuthEndDate());
        employee.setDriverLicense(request.getDriverLicense());
        employee.setDriverLicenseExpiration(request.getDriverLicenseExpiration());
        employee.setLastModificationDate(LocalDateTime.now());

        return employeeRepository.save(employee);
    }
    @Override
    public void uploadDocument(String employeeId, UploadDocumentRequest request) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id: " + employeeId));

        PersonalDocument document = new PersonalDocument();
        document.setTitle(request.getTitle());
        document.setPath(request.getPath());
        document.setComment(request.getComment());
        document.setCreateDate(LocalDateTime.now());

        employee.getPersonalDocuments().add(document);
        employeeRepository.save(employee);
    }
    @Override
    public GetDocumentsResponse getDocumentsByEmployeeId(String employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id: " + employeeId));

        List<PersonalDocument> documents = employee.getPersonalDocuments();

        GetDocumentsResponse response = new GetDocumentsResponse();
        response.setDocuments(documents);
        response.setMessage("Documents retrieved");

        return response;
    }



}