package org.beaconfire.service;

import org.beaconfire.dto.*;
import org.beaconfire.exception.DocumentNotFoundException;
import org.beaconfire.exception.EmployeeAlreadyExistsException;
import org.beaconfire.exception.EmployeeNotFoundException;
import org.beaconfire.model.Employee;
import org.beaconfire.model.PersonalDocument;
import org.beaconfire.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
    @Test
    void updateDocument_Success() {
        String employeeId = "EMP123";

        PersonalDocument doc = new PersonalDocument();
        doc.setTitle("Old Title");
        doc.setPath("s3-url");
        doc.setComment("Old comment");

        List<PersonalDocument> documents = new ArrayList<>();
        documents.add(doc);

        Employee employee = new Employee();
        employee.setId(employeeId);
        employee.setPersonalDocuments(documents);

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));

        UpdateDocumentRequest request = new UpdateDocumentRequest();
        request.setTitle("New Title");
        request.setPath("s3-url");
        request.setComment("Updated comment");

        employeeService.updateDocument(employeeId, request);

        // 验证修改是否正确
        assertEquals("New Title", doc.getTitle());
        assertEquals("Updated comment", doc.getComment());
        assertEquals("s3-url", doc.getPath());

        verify(employeeRepository, times(1)).save(employee);
    }

    // 测试：员工不存在
    @Test
    void updateDocument_EmployeeNotFound() {
        String employeeId = "EMP404";

        UpdateDocumentRequest request = new UpdateDocumentRequest();
        request.setPath("s3-url");
        request.setTitle("New Title");
        request.setComment("New comment");

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.empty());

        assertThrows(EmployeeNotFoundException.class, () -> employeeService.updateDocument(employeeId, request));

        verify(employeeRepository, times(1)).findById(employeeId);
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    // 测试：文档不存在
    @Test
    void updateDocument_DocumentNotFound() {
        String employeeId = "EMP123";

        List<PersonalDocument> documents = new ArrayList<>();
        // 注意：这里没有添加 path 为 "s3-url" 的文档

        Employee employee = new Employee();
        employee.setId(employeeId);
        employee.setPersonalDocuments(documents);

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));

        UpdateDocumentRequest request = new UpdateDocumentRequest();
        request.setPath("s3-url");
        request.setTitle("New Title");
        request.setComment("New comment");

        assertThrows(DocumentNotFoundException.class, () -> employeeService.updateDocument(employeeId, request));

        verify(employeeRepository, times(1)).findById(employeeId);
        verify(employeeRepository, never()).save(any(Employee.class));
    }
    @Test
    public void testGetEmployeesByHouseId() {
        // 准备测试数据
        Employee emp1 = Employee.builder()
                .firstName("John")
                .preferredName("Johnny")
                .cellPhone("111-111-1111")
                .build();

        Employee emp2 = Employee.builder()
                .firstName("Alice")
                .preferredName("")
                .cellPhone("222-222-2222")
                .build();

        Employee emp3 = Employee.builder()
                .firstName("Bob")
                .preferredName(null)
                .cellPhone("333-333-3333")
                .build();

        List<Employee> mockEmployees = Arrays.asList(emp1, emp2, emp3);

        // Mock repository 返回数据
        when(employeeRepository.findByHouseId("house123")).thenReturn(mockEmployees);

        // 调用方法
        List<GetEmployeeByHouseResponse> responses = employeeService.getEmployeesByHouseId("house123");

        // 断言
        assertEquals(3, responses.size());

        assertEquals("Johnny", responses.get(0).getName());
        assertEquals("111-111-1111", responses.get(0).getPhone());

        assertEquals("Alice", responses.get(1).getName());
        assertEquals("222-222-2222", responses.get(1).getPhone());

        assertEquals("Bob", responses.get(2).getName());
        assertEquals("333-333-3333", responses.get(2).getPhone());
    }
    @Test
    void testGetAllEmployees() {
        // 准备 mock 数据
        Employee emp1 = new Employee();
        emp1.setId("1");
        emp1.setFirstName("Alice");
        emp1.setLastName("Smith");
        emp1.setPreferredName("Ali");
        emp1.setEmail("alice@example.com");
        emp1.setCellPhone("123-456-7890");

        Employee emp2 = new Employee();
        emp2.setId("2");
        emp2.setFirstName("Bob");
        emp2.setLastName("Johnson");
        emp2.setPreferredName(null);
        emp2.setEmail("bob@example.com");
        emp2.setCellPhone("987-654-3210");

        List<Employee> mockEmployees = Arrays.asList(emp1, emp2);

        // 设置 repository 返回值
        when(employeeRepository.findAll()).thenReturn(mockEmployees);

        // 调用被测方法
        List<GetEmployeeResponse> results = employeeService.getAllEmployees();

        // 断言
        assertEquals(2, results.size());

        GetEmployeeResponse res1 = results.get(0);
        assertEquals("1", res1.getId());
        assertEquals("Alice", res1.getFirstName());
        assertEquals("Smith", res1.getLastName());
        assertEquals("Ali", res1.getPreferredName());
        assertEquals("alice@example.com", res1.getEmail());
        assertEquals("123-456-7890", res1.getCellPhone());

        GetEmployeeResponse res2 = results.get(1);
        assertEquals("2", res2.getId());
        assertEquals("Bob", res2.getFirstName());
        assertEquals("Johnson", res2.getLastName());
        assertNull(res2.getPreferredName());
        assertEquals("bob@example.com", res2.getEmail());
        assertEquals("987-654-3210", res2.getCellPhone());

        // 验证 repository 是否被调用一次
        verify(employeeRepository, times(1)).findAll();
    }


}
