package org.beaconfire.service;

import org.beaconfire.dto.CreateEmployeeRequest;
import org.beaconfire.exception.EmployeeAlreadyExistsException;
import org.beaconfire.model.Employee;
import org.beaconfire.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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
    void registerEmployee_Success() {
        CreateEmployeeRequest request = new CreateEmployeeRequest();
        request.setUserId("U123");
        request.setFirstName("Alice");
        request.setLastName("Smith");
        request.setEmail("alice@example.com");

        when(employeeRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(employeeRepository.save(any(Employee.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Employee savedEmployee = employeeService.registerEmployee(request);

        assertNotNull(savedEmployee);
        assertEquals("Alice", savedEmployee.getFirstName());
        assertEquals("Smith", savedEmployee.getLastName());
        assertEquals("alice@example.com", savedEmployee.getEmail());

        verify(employeeRepository, times(1)).existsByEmail(request.getEmail());
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @Test
    void registerEmployee_AlreadyExists() {
        CreateEmployeeRequest request = new CreateEmployeeRequest();
        request.setEmail("alice@example.com");

        when(employeeRepository.existsByEmail(request.getEmail())).thenReturn(true);

        assertThrows(EmployeeAlreadyExistsException.class, () -> employeeService.registerEmployee(request));

        verify(employeeRepository, times(1)).existsByEmail(request.getEmail());
        verify(employeeRepository, never()).save(any(Employee.class));
    }
}
