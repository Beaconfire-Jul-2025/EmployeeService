package org.beaconfire.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.beaconfire.dto.CreateEmployeeRequest;
import org.beaconfire.dto.GetEmployeeResponse;
import org.beaconfire.dto.UpdateEmployeeRequest;
import org.beaconfire.dto.UploadDocumentRequest;
import org.beaconfire.model.Employee;
import org.beaconfire.model.PersonalDocument;
import org.beaconfire.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmployeeController.class)
@AutoConfigureMockMvc(addFilters = false)
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    private Employee sampleEmployee;

    @BeforeEach
    void setUp() {
        sampleEmployee = Employee.builder()
                .id("123")
                .userId("AUTH123")
                .firstName("Alice")
                .lastName("Smith")
                .email("alice@example.com")
                .personalDocuments(new ArrayList<>())
                .build();
    }

    @Test
    void testCreateEmployee() throws Exception {
        CreateEmployeeRequest request = new CreateEmployeeRequest();
        request.setUserId("AUTH123");
        request.setFirstName("Alice");
        request.setLastName("Smith");
        request.setEmail("alice@example.com");

        when(employeeService.registerEmployee(any(CreateEmployeeRequest.class))).thenReturn(sampleEmployee);

        mockMvc.perform(post("/employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.employeeId").value("123"))
                .andExpect(jsonPath("$.message").value("Employee profile created"));
    }

    @Test
    void testGetEmployee() throws Exception {
        when(employeeService.getEmployeeById("123")).thenReturn(sampleEmployee);

        mockMvc.perform(get("/employee/123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.employee.id").value("123"))
                .andExpect(jsonPath("$.message").value("Employee profile retrieved"));
    }

    @Test
    void testUpdateEmployee() throws Exception {
        UpdateEmployeeRequest request = new UpdateEmployeeRequest();
        request.setFirstName("UpdatedName");

        when(employeeService.updateEmployee(Mockito.eq("123"), any(UpdateEmployeeRequest.class))).thenReturn(sampleEmployee);

        mockMvc.perform(put("/employee/123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Employee profile updated"));
    }

    @Test
    void testGetEmployeeDocuments() throws Exception {
        List<PersonalDocument> docs = new ArrayList<>();
        docs.add(PersonalDocument.builder().title("Driver License").path("path/to/doc").build());

        when(employeeService.getEmployeeDocuments("123")).thenReturn(docs);

        mockMvc.perform(get("/employee/123/documents"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.documents[0].title").value("Driver License"))
                .andExpect(jsonPath("$.message").value("Documents retrieved"));
    }

    @Test
    void testAddDocumentToEmployee() throws Exception {
        UploadDocumentRequest request = new UploadDocumentRequest();
        request.setTitle("Driver License");
        request.setPath("path/to/doc");
        request.setComment("Uploaded new license");

        // 不需要返回值，只需要 verify 调用
        mockMvc.perform(post("/employee/123/documents")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Document uploaded"));
    }
}

