package org.beaconfire.service;

import org.beaconfire.dto.CreateEmployeeRequest;
import org.beaconfire.dto.GetDocumentsResponse;
import org.beaconfire.dto.UpdateDocumentRequest;
import org.beaconfire.dto.UploadDocumentRequest;
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

}
