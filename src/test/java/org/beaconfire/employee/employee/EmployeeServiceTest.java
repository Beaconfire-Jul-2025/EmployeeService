package org.beaconfire.employee.employee;

import org.beaconfire.employee.dto.CreateEmployeeRequest;
import org.beaconfire.employee.model.Employee;
import org.beaconfire.employee.repository.EmployeeRepository;
import org.beaconfire.employee.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmployeeServiceTest {

    @InjectMocks
    private EmployeeService employeeService;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private MongoTemplate mongoTemplate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllEmployees() {
        List<Employee> mockList = Arrays.asList(
                Employee.builder().id("1").firstName("Alice").build(),
                Employee.builder().id("2").firstName("Bob").build()
        );
        when(employeeRepository.findAll()).thenReturn(mockList);

        List<Employee> result = employeeService.getAllEmployees();

        assertEquals(2, result.size());
        verify(employeeRepository).findAll();
    }

    @Test
    void testGetEmployeeById() {
        Employee employee = Employee.builder().id("123").firstName("Test").build();
        when(employeeRepository.findById("123")).thenReturn(Optional.of(employee));

        Optional<Employee> result = employeeService.getEmployeeById("123");

        assertTrue(result.isPresent());
        assertEquals("Test", result.get().getFirstName());
    }
    @Test
    void testGetEmployeeById_NotFound() {
        when(employeeRepository.findById("999")).thenReturn(Optional.empty());

        Optional<Employee> result = employeeService.getEmployeeById("999");

        assertFalse(result.isPresent());
    }

    @Test
    void testGetEmployeeByUserId() {
        Employee employee = Employee.builder().userId("u001").applicationType("profile").build();
        when(mongoTemplate.findOne(any(Query.class), eq(Employee.class))).thenReturn(employee);

        Optional<Employee> result = employeeService.getEmployeeByUserId("u001");

        assertTrue(result.isPresent());
        assertEquals("u001", result.get().getUserId());
    }
    @Test
    void testGetEmployeeByUserId_NotFound() {
        when(mongoTemplate.findOne(any(Query.class), eq(Employee.class))).thenReturn(null);

        Optional<Employee> result = employeeService.getEmployeeByUserId("not_exist");

        assertFalse(result.isPresent());
    }


    @Test
    void testCreateEmployee() {
        CreateEmployeeRequest request = new CreateEmployeeRequest();
        request.setFirstName("Anna");
        request.setLastName("Smith");
        request.setEmail("anna@example.com");
        request.setApplicationType("profile");

        Employee saved = Employee.builder()
                .firstName("Anna")
                .lastName("Smith")
                .email("anna@example.com")
                .applicationType("profile")
                .build();

        when(employeeRepository.save(any(Employee.class))).thenReturn(saved);

        Employee result = employeeService.createEmployee(request);

        assertEquals("Anna", result.getFirstName());
        assertEquals("Smith", result.getLastName());
        assertEquals("anna@example.com", result.getEmail());
        assertEquals("profile", result.getApplicationType());
    }


    @Test
    void testUpdateEmployee() {
        Employee updated = Employee.builder().id("x001").firstName("Updated").build();
        when(employeeRepository.save(any(Employee.class))).thenReturn(updated);

        Employee result = employeeService.updateEmployee("x001", updated);

        assertEquals("x001", result.getId());
        assertEquals("Updated", result.getFirstName());
    }

    @Test
    void testDeleteEmployee() {
        doNothing().when(employeeRepository).deleteById("del001");

        employeeService.deleteEmployee("del001");

        verify(employeeRepository, times(1)).deleteById("del001");
    }

    @Test
    void testGetEmployeesWithFilters() {
        List<Employee> employees = Arrays.asList(
                Employee.builder().id("1").firstName("A").build(),
                Employee.builder().id("2").firstName("B").build()
        );
        when(mongoTemplate.count(any(Query.class), eq(Employee.class))).thenReturn(2L);
        when(mongoTemplate.find(any(Query.class), eq(Employee.class))).thenReturn(employees);

        Page<Employee> result = employeeService.getEmployees("A", "B", "mail", 0, 10, "firstName", "asc");

        assertEquals(2, result.getTotalElements());
        assertEquals(2, result.getContent().size());
    }
    @Test
    void testGetEmployees_NoFilters() {
        List<Employee> employees = Collections.singletonList(
                Employee.builder().id("1").firstName("NoFilter").build()
        );
        when(mongoTemplate.count(any(Query.class), eq(Employee.class))).thenReturn(1L);
        when(mongoTemplate.find(any(Query.class), eq(Employee.class))).thenReturn(employees);

        Page<Employee> result = employeeService.getEmployees(null, null, null, 0, 5, "firstName", "desc");

        assertEquals(1, result.getTotalElements());
        assertEquals("NoFilter", result.getContent().get(0).getFirstName());
    }


    @Test
    void testFindRoommatesByHouseId() {
        List<Employee> roommates = Arrays.asList(
                Employee.builder().userId("1").build(),
                Employee.builder().userId("2").build()
        );
        when(mongoTemplate.find(any(Query.class), eq(Employee.class))).thenReturn(roommates);

        List<Employee> result = employeeService.findRoommatesByHouseId("houseX", "me");

        assertEquals(2, result.size());
    }
    @Test
    void testFindRoommatesByHouseId_NoRoommates() {
        when(mongoTemplate.find(any(Query.class), eq(Employee.class))).thenReturn(Collections.emptyList());

        List<Employee> result = employeeService.findRoommatesByHouseId("house123", "me");

        assertTrue(result.isEmpty());
    }

}