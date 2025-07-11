package org.beaconfire.service;

import org.beaconfire.dto.UpdateEmployeeRequest;
import org.beaconfire.dto.CreateEmployeeRequest;
import org.beaconfire.dto.GetDocumentsResponse;
import org.beaconfire.dto.UploadDocumentRequest;
import org.beaconfire.exception.EmployeeAlreadyExistsException;
import org.beaconfire.exception.EmployeeNotFoundException;
import org.beaconfire.model.Employee;
import org.beaconfire.model.PersonalDocument;
import org.beaconfire.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.time.LocalDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmployeeServiceImplTest {

    private EmployeeRepository employeeRepository;
    private EmployeeServiceImpl employeeService;

    @BeforeEach
    void setUp() {
        employeeRepository = mock(EmployeeRepository.class);
        employeeService = new EmployeeServiceImpl(employeeRepository);
    }

    @Test
    void testUpdateEmployee_success() {
        String id = "123";
        Employee existingEmployee = new Employee();
        existingEmployee.setId(id);
        existingEmployee.setFirstName("OldFirstName");

        UpdateEmployeeRequest request = new UpdateEmployeeRequest();
        request.setFirstName("NewFirstName");
        request.setLastName("NewLastName");
        request.setMiddleName("M");
        request.setPreferredName("Nick");
        request.setCellPhone("111-222-3333");
        request.setAlternatePhone("444-555-6666");
        request.setGender("Male");
        request.setSsn("123-45-6789");
        request.setDob(LocalDate.of(1990, 1, 1));
        request.setStartDate(LocalDate.of(2020, 1, 1));
        request.setEndDate(LocalDate.of(2025, 1, 1));
        request.setWorkAuthType("H1B");
        request.setWorkAuthStartDate(LocalDate.of(2020, 1, 1));
        request.setWorkAuthEndDate(LocalDate.of(2025, 1, 1));
        request.setDriverLicense("DL1234567");
        request.setDriverLicenseExpiration(LocalDate.of(2030, 1, 1));

        when(employeeRepository.findById(id)).thenReturn(Optional.of(existingEmployee));
        when(employeeRepository.save(any(Employee.class))).thenAnswer(i -> i.getArgument(0));

        Employee updatedEmployee = employeeService.updateEmployee(id, request);

        assertEquals("NewFirstName", updatedEmployee.getFirstName());
        assertEquals("NewLastName", updatedEmployee.getLastName());
        assertEquals("M", updatedEmployee.getMiddleName());
        assertEquals("Nick", updatedEmployee.getPreferredName());
        assertEquals("111-222-3333", updatedEmployee.getCellPhone());
        assertEquals("444-555-6666", updatedEmployee.getAlternatePhone());
        assertEquals("Male", updatedEmployee.getGender());
        assertEquals("123-45-6789", updatedEmployee.getSsn());
        assertEquals(LocalDate.of(1990, 1, 1), updatedEmployee.getDob());
        assertEquals(LocalDate.of(2020, 1, 1), updatedEmployee.getStartDate());
        assertEquals(LocalDate.of(2025, 1, 1), updatedEmployee.getEndDate());
        assertEquals("H1B", updatedEmployee.getWorkAuthType());
        assertEquals(LocalDate.of(2020, 1, 1), updatedEmployee.getWorkAuthStartDate());
        assertEquals(LocalDate.of(2025, 1, 1), updatedEmployee.getWorkAuthEndDate());
        assertEquals("DL1234567", updatedEmployee.getDriverLicense());
        assertEquals(LocalDate.of(2030, 1, 1), updatedEmployee.getDriverLicenseExpiration());
        assertNotNull(updatedEmployee.getLastModificationDate());

        verify(employeeRepository).findById(id);
        verify(employeeRepository).save(existingEmployee);
    }

    @Test
    void testUpdateEmployee_notFound() {
        String id = "999";
        UpdateEmployeeRequest request = new UpdateEmployeeRequest();

        when(employeeRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EmployeeNotFoundException.class, () -> {
            employeeService.updateEmployee(id, request);
        });

        verify(employeeRepository).findById(id);
        verify(employeeRepository, never()).save(any());
    }
    @Test
    void registerEmployee_Success() {
        CreateEmployeeRequest request = new CreateEmployeeRequest();
        request.setUserId("AUTH123");
        request.setFirstName("Alice");
        request.setLastName("Smith");
        request.setMiddleName("M");
        request.setEmail("alice@example.com");

        when(employeeRepository.existsByEmail(request.getEmail())).thenReturn(false);

        Employee savedEmployee = new Employee();
        savedEmployee.setId("generatedId");
        savedEmployee.setUserId(request.getUserId());
        savedEmployee.setFirstName(request.getFirstName());
        savedEmployee.setLastName(request.getLastName());
        savedEmployee.setMiddleName(request.getMiddleName());
        savedEmployee.setEmail(request.getEmail());

        when(employeeRepository.save(any(Employee.class))).thenReturn(savedEmployee);

        Employee result = employeeService.registerEmployee(request);

        assertNotNull(result);
        assertEquals("generatedId", result.getId());
        assertEquals("Alice", result.getFirstName());
        assertEquals("Smith", result.getLastName());
        assertEquals("M", result.getMiddleName());
        assertEquals("alice@example.com", result.getEmail());

        verify(employeeRepository, times(1)).existsByEmail(request.getEmail());
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @Test
    void registerEmployee_EmailExists() {
        CreateEmployeeRequest request = new CreateEmployeeRequest();
        request.setEmail("existing@example.com");

        when(employeeRepository.existsByEmail(request.getEmail())).thenReturn(true);

        assertThrows(EmployeeAlreadyExistsException.class, () -> employeeService.registerEmployee(request));

        verify(employeeRepository, times(1)).existsByEmail(request.getEmail());
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @Test
    void testUpdateEmployee_success() {
    }

    @Test
    void testUpdateEmployee_notFound() {
    }

    @Test
    void uploadDocument_Success() {
        String employeeId = "EMP123";
        UploadDocumentRequest request = new UploadDocumentRequest();
        request.setTitle("Driver License");
        request.setPath("https://s3-url");
        request.setComment("Uploaded new license");

        Employee employee = new Employee();
        employee.setId(employeeId);
        employee.setPersonalDocuments(new ArrayList<>());

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));

        employeeService.uploadDocument(employeeId, request);

        // 验证 employee 对象中的文档是否添加成功
        List<PersonalDocument> documents = employee.getPersonalDocuments();
        assertNotNull(documents);
        assertEquals(1, documents.size());
        PersonalDocument savedDoc = documents.get(0);
        assertEquals("Driver License", savedDoc.getTitle());
        assertEquals("https://s3-url", savedDoc.getPath());
        assertEquals("Uploaded new license", savedDoc.getComment());
        assertNotNull(savedDoc.getCreateDate());

        verify(employeeRepository, times(1)).findById(employeeId);
        verify(employeeRepository, times(1)).save(employee);
    }

    @Test
    void uploadDocument_EmployeeNotFound() {
        String employeeId = "EMP404";
        UploadDocumentRequest request = new UploadDocumentRequest();
        request.setTitle("Passport");
        request.setPath("https://s3-url");
        request.setComment("Uploaded passport");

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.empty());

        assertThrows(EmployeeNotFoundException.class, () -> employeeService.uploadDocument(employeeId, request));

        verify(employeeRepository, times(1)).findById(employeeId);
        verify(employeeRepository, never()).save(any(Employee.class));
    }
    @Test
    void getDocumentsByEmployeeId_Success() {
        String employeeId = "EMP123";

        PersonalDocument doc1 = new PersonalDocument();
        doc1.setTitle("Passport");
        doc1.setPath("https://s3-url/passport");
        doc1.setComment("Uploaded passport");
        doc1.setCreateDate(LocalDateTime.now());

        PersonalDocument doc2 = new PersonalDocument();
        doc2.setTitle("Driver License");
        doc2.setPath("https://s3-url/license");
        doc2.setComment("Uploaded license");
        doc2.setCreateDate(LocalDateTime.now());

        List<PersonalDocument> documentList = new ArrayList<>();
        documentList.add(doc1);
        documentList.add(doc2);

        Employee employee = new Employee();
        employee.setId(employeeId);
        employee.setPersonalDocuments(documentList);

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));

        GetDocumentsResponse response = employeeService.getDocumentsByEmployeeId(employeeId);

        assertNotNull(response);
        assertEquals(2, response.getDocuments().size());
        assertEquals("Passport", response.getDocuments().get(0).getTitle());
        assertEquals("Driver License", response.getDocuments().get(1).getTitle());
        assertEquals("Documents retrieved", response.getMessage());

        verify(employeeRepository, times(1)).findById(employeeId);
    }

    // employee 不存在
    @Test
    void getDocumentsByEmployeeId_EmployeeNotFound() {
        String employeeId = "EMP404";

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.empty());

        assertThrows(EmployeeNotFoundException.class, () -> employeeService.getDocumentsByEmployeeId(employeeId));

        verify(employeeRepository, times(1)).findById(employeeId);
    }

}

