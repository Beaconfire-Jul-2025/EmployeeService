package org.beaconfire.employee.service;

import org.beaconfire.employee.dto.*;
import org.beaconfire.employee.exception.DocumentAlreadyExistsException;
import org.beaconfire.employee.exception.DocumentNotFoundException;
import org.beaconfire.employee.exception.EmployeeAlreadyExistsException;
import org.beaconfire.employee.exception.EmployeeNotFoundException;
import org.beaconfire.employee.model.Address;
import org.beaconfire.employee.model.Employee;
import org.beaconfire.employee.model.PersonalDocument;

import org.beaconfire.employee.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
    void testCreateEmployee_success() {
        CreateEmployeeRequest request = CreateEmployeeRequest.builder()
                .userId("u1")
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .dob(LocalDate.of(1990, 1, 1).atStartOfDay())
                .gender("Male")
                .addresses(Collections.singletonList(AddressRequest.builder()
                        .id("a1").type("Home").addressLine1("123 St").city("City").state("State").zipCode("00000").build()))
                .emergencyContacts(Collections.singletonList(EmergencyContactRequest.builder()
                        .id("ec1").firstName("Jane").relationship("Friend").cellPhone("1234567890").build()))
                .applicationType("Full-time")
                .build();

        when(employeeRepository.existsByEmail(request.getEmail())).thenReturn(false);

        String result = employeeService.createEmployee(request);

        assertThat(result).isEqualTo("Employee created successfully.");
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @Test
    void testCreateEmployee_alreadyExists() {
        CreateEmployeeRequest request = CreateEmployeeRequest.builder()
                .email("duplicate@example.com")
                .build();

        when(employeeRepository.existsByEmail(request.getEmail())).thenReturn(true);

        assertThatThrownBy(() -> employeeService.createEmployee(request))
                .isInstanceOf(EmployeeAlreadyExistsException.class)
                .hasMessageContaining("Employee already exists");
    }

    @Test
    void testValidateEmployeeInfo_success() {
        ValidateEmployeeInfoRequest request = ValidateEmployeeInfoRequest.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .dob(LocalDate.of(1990, 1, 1).atStartOfDay())
                .gender("Male")
                .addresses(Collections.singletonList(AddressRequest.builder()
                        .addressLine1("123").city("City").state("State").zipCode("00000").build()))
                .emergencyContacts(Collections.singletonList(EmergencyContactRequest.builder()
                        .firstName("Jane").relationship("Friend").cellPhone("1234567890").build()))
                .build();

        Employee employee = Employee.builder().id("emp1").email("john.doe@example.com").build();
        when(employeeRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(employee));

        String id = employeeService.validateEmployeeInfo(request);

        assertThat(id).isEqualTo("emp1");
    }

    @Test
    void testValidateEmployeeInfo_missingFirstName() {
        ValidateEmployeeInfoRequest request = ValidateEmployeeInfoRequest.builder()
                .firstName("")
                .lastName("Doe")
                .email("john@example.com")
                .dob(LocalDate.of(1990, 1, 1).atStartOfDay())
                .gender("Male")
                .addresses(Collections.singletonList(AddressRequest.builder()
                        .addressLine1("123").city("City").state("State").zipCode("00000").build()))
                .emergencyContacts(Collections.singletonList(EmergencyContactRequest.builder()
                        .firstName("Jane").relationship("Friend").cellPhone("1234567890").build()))
                .build();

        assertThatThrownBy(() -> employeeService.validateEmployeeInfo(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("First Name is required");
    }

    @Test
    void testGetEmployeeProfileById_success() {
        Employee employee = Employee.builder()
                .id("emp1")
                .userId("u1")
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .dob(LocalDate.of(1990, 1, 1).atStartOfDay())
                .startDate(LocalDate.of(2020, 1, 1).atStartOfDay())
                .addresses(Collections.singletonList(Address.builder().id("a1").addressLine1("123").city("City").state("State").zipCode("00000").build()))
                .build();

        when(employeeRepository.findById("emp1")).thenReturn(Optional.of(employee));

        GetEmployeeResponse response = employeeService.getEmployeeProfileById("emp1");

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo("emp1");
        assertThat(response.getAddresses()).hasSize(1);
    }

    @Test
    void testGetEmployeeProfileById_notFound() {
        when(employeeRepository.findById("emp1")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> employeeService.getEmployeeProfileById("emp1"))
                .isInstanceOf(EmployeeNotFoundException.class);
    }

    @Test
    void testUploadDocument_success() {
        Employee employee = Employee.builder().id("emp1").personalDocuments(new ArrayList<>()).build();
        when(employeeRepository.findById("emp1")).thenReturn(Optional.of(employee));

        UploadDocumentRequest request = new UploadDocumentRequest();
        request.setTitle("Resume");
        request.setPath("resume.pdf");
        request.setComment("My Resume");

        employeeService.uploadDocument("emp1", request);

        assertThat(employee.getPersonalDocuments()).hasSize(1);
        assertThat(employee.getPersonalDocuments().get(0).getTitle()).isEqualTo("Resume");
    }

    @Test
    void testUpdateDocument_success() {
        PersonalDocument doc = new PersonalDocument();
        doc.setPath("old.pdf");
        List<PersonalDocument> docs = new ArrayList<>();
        docs.add(doc);

        Employee employee = Employee.builder().id("emp1").personalDocuments(docs).build();
        when(employeeRepository.findById("emp1")).thenReturn(Optional.of(employee));

        UpdateDocumentRequest request = new UpdateDocumentRequest();
        request.setPath("old.pdf");
        request.setTitle("Updated");
        request.setComment("Updated comment");

        employeeService.updateDocument("emp1", request);

        assertThat(employee.getPersonalDocuments().get(0).getTitle()).isEqualTo("Updated");
    }
    @Test
    public void testUploadDocument_ThrowsWhenDocumentAlreadyExists() {
        // Arrange
        String employeeId = "emp123";
        UploadDocumentRequest request = new UploadDocumentRequest();
        request.setPath("/documents/driver_license.pdf");
        request.setType("DRIVER_LICENSE");
        request.setTitle("Driver License");

        PersonalDocument existingDoc = new PersonalDocument();
        existingDoc.setPath("/documents/driver_license.pdf");

        Employee employee = new Employee();
        employee.setId(employeeId);
        employee.setPersonalDocuments(new ArrayList<>());
        employee.getPersonalDocuments().add(existingDoc);

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));

        // Act & Assert
        assertThrows(DocumentAlreadyExistsException.class, () -> {
            employeeService.uploadDocument(employeeId, request);
        });
    }

    @Test
    void testUpdateDocument_notFound() {
        Employee employee = Employee.builder().id("emp1").personalDocuments(new ArrayList<>()).build();
        when(employeeRepository.findById("emp1")).thenReturn(Optional.of(employee));

        UpdateDocumentRequest request = new UpdateDocumentRequest();
        request.setPath("missing.pdf");

        assertThatThrownBy(() -> employeeService.updateDocument("emp1", request))
                .isInstanceOf(DocumentNotFoundException.class);
    }

    @Test
    void testSearchEmployeesByName_success() {
        Employee e1 = Employee.builder().id("1").firstName("John").lastName("Smith").build();
        Employee e2 = Employee.builder().id("2").firstName("Johnny").lastName("Doe").build();

        when(employeeRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase("jo", "jo"))
                .thenReturn(Arrays.asList(e1, e2));

        List<GetEmployeeResponse> responses = employeeService.searchEmployeesByName("jo");

        assertThat(responses).hasSize(2);
        assertThat(responses.get(0).getId()).isEqualTo("1");
    }

    @Test
    void testGetAllEmployees_success() {
        Employee e1 = Employee.builder().id("1").firstName("John").build();
        Employee e2 = Employee.builder().id("2").firstName("Jane").build();
        when(employeeRepository.findAll()).thenReturn(Arrays.asList(e1, e2));

        List<GetEmployeeResponse> responses = employeeService.getAllEmployees();

        assertThat(responses).hasSize(2);
    }

    @Test
    void testGetAllEmployeesPaged_success() {
        Employee e1 = Employee.builder().id("1").firstName("John").build();
        Employee e2 = Employee.builder().id("2").firstName("Jane").build();
        Page<Employee> page = new PageImpl<>(Arrays.asList(e1, e2));

        when(employeeRepository.findAll(any(Pageable.class))).thenReturn(page);

        Page<GetEmployeeResponse> responses = employeeService.getAllEmployeesPaged(PageRequest.of(0, 10));

        assertThat(responses.getContent()).hasSize(2);
    }
}
