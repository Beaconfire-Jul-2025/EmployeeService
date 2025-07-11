package org.beaconfire.service;

import org.beaconfire.dto.CreateEmployeeRequest;
import org.beaconfire.exception.EmployeeAlreadyExistsException;
import org.beaconfire.exception.EmployeeNotFoundException;
import org.beaconfire.model.Employee;
import org.beaconfire.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getEmployeeById_Success() {
        String id = "123456";
        Employee employee = new Employee();
        employee.setId(id);
        employee.setFirstName("Alice");
        employee.setLastName("Smith");

        when(employeeRepository.findById(id)).thenReturn(Optional.of(employee));

        Employee result = employeeService.getEmployeeById(id);

        assertNotNull(result);
        assertEquals("Alice", result.getFirstName());
        assertEquals("Smith", result.getLastName());

        verify(employeeRepository, times(1)).findById(id);
    }

    @Test
    void getEmployeeById_NotFound() {
        String id = "123456";

        when(employeeRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EmployeeNotFoundException.class, () -> employeeService.getEmployeeById(id));

        verify(employeeRepository, times(1)).findById(id);
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
}
