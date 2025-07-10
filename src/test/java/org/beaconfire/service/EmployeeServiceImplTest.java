package org.beaconfire.service;

import org.beaconfire.dto.CreateEmployeeRequest;
import org.beaconfire.dto.UpdateEmployeeRequest;
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
import java.util.*;

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
    void testRegisterEmployee_Success() {
        CreateEmployeeRequest request = new CreateEmployeeRequest();
        request.setUserId("AUTH123");
        request.setFirstName("Alice");
        request.setLastName("Smith");
        request.setEmail("alice@example.com");

        when(employeeRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(employeeRepository.save(any(Employee.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Employee saved = employeeService.registerEmployee(request);

        assertEquals("Alice", saved.getFirstName());
        assertTrue(saved.getActiveFlag());
        assertNotNull(saved.getCreateDate());
        assertNotNull(saved.getPersonalDocuments());
    }

    @Test
    void testRegisterEmployee_EmailExists() {
        CreateEmployeeRequest request = new CreateEmployeeRequest();
        request.setEmail("duplicate@example.com");

        when(employeeRepository.existsByEmail(request.getEmail())).thenReturn(true);

        assertThrows(EmployeeAlreadyExistsException.class, () -> employeeService.registerEmployee(request));
    }

    @Test
    void testGetEmployeeById_Found() {
        Employee employee = Employee.builder().id("123").firstName("Test").build();
        when(employeeRepository.findById("123")).thenReturn(Optional.of(employee));

        Employee found = employeeService.getEmployeeById("123");

        assertEquals("Test", found.getFirstName());
    }

    @Test
    void testGetEmployeeById_NotFound() {
        when(employeeRepository.findById("456")).thenReturn(Optional.empty());

        assertThrows(EmployeeNotFoundException.class, () -> employeeService.getEmployeeById("456"));
    }

    @Test
    void testUpdateEmployee() {
        Employee existing = Employee.builder().id("123").firstName("Old").build();
        when(employeeRepository.findById("123")).thenReturn(Optional.of(existing));
        when(employeeRepository.save(any(Employee.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UpdateEmployeeRequest request = new UpdateEmployeeRequest();
        request.setFirstName("NewName");
        request.setLastName("NewLast");
        request.setDob(LocalDate.of(1990, 1, 1));

        Employee updated = employeeService.updateEmployee("123", request);

        assertEquals("NewName", updated.getFirstName());
        assertEquals("NewLast", updated.getLastName());
        assertEquals(LocalDate.of(1990, 1, 1), updated.getDob());
        assertNotNull(updated.getLastModificationDate());
    }

    @Test
    void testGetEmployeeDocuments() {
        List<PersonalDocument> docs = new ArrayList<>();
        docs.add(PersonalDocument.builder().title("Doc1").build());

        Employee employee = Employee.builder()
                .id("123")
                .personalDocuments(docs)
                .build();

        when(employeeRepository.findById("123")).thenReturn(Optional.of(employee));

        List<PersonalDocument> retrieved = employeeService.getEmployeeDocuments("123");

        assertEquals(1, retrieved.size());
        assertEquals("Doc1", retrieved.get(0).getTitle());
    }

    @Test
    void testAddDocumentToEmployee() {
        Employee employee = Employee.builder()
                .id("123")
                .personalDocuments(new ArrayList<>())
                .build();

        when(employeeRepository.findById("123")).thenReturn(Optional.of(employee));
        when(employeeRepository.save(any(Employee.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UploadDocumentRequest request = new UploadDocumentRequest();
        request.setTitle("Driver License");
        request.setPath("test-path");
        request.setComment("Uploaded new license");

        employeeService.addDocumentToEmployee("123", request);

        assertEquals(1, employee.getPersonalDocuments().size());
        assertEquals("Driver License", employee.getPersonalDocuments().get(0).getTitle());
    }
}

