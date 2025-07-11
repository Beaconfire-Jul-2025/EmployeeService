package org.beaconfire.service;

import org.beaconfire.dto.UpdateEmployeeRequest;
import org.beaconfire.exception.EmployeeNotFoundException;
import org.beaconfire.model.Employee;
import org.beaconfire.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.time.LocalDate;

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
}

