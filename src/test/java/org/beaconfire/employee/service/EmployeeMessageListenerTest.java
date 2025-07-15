package org.beaconfire.employee.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.beaconfire.employee.dto.CreateEmployeeRequest;
import org.beaconfire.employee.dto.UpdateEmployeeRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class EmployeeMessageListenerTest {

    @Mock
    private EmployeeService employeeService;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private EmployeeMessageListener employeeMessageListener;




    @Test
    void testReceiveEmployeeMessage_Success() throws Exception {
        String message = "{\"firstName\":\"John\",\"lastName\":\"Doe\",\"email\":\"john.doe@example.com\"}";
        CreateEmployeeRequest mockRequest = new CreateEmployeeRequest();
        mockRequest.setFirstName("John");
        mockRequest.setLastName("Doe");
        mockRequest.setEmail("john.doe@example.com");


        when(objectMapper.readValue(message, CreateEmployeeRequest.class)).thenReturn(mockRequest);


        employeeMessageListener.receiveEmployeeMessage(message);

        verify(employeeService, times(1)).createEmployee(mockRequest);
    }

    @Test
    void testReceiveEmployeeMessage_Exception() throws Exception {
        String message = "{\"firstName\":\"John\"}";


        when(objectMapper.readValue(message, CreateEmployeeRequest.class)).thenThrow(new RuntimeException("Parsing error"));


        employeeMessageListener.receiveEmployeeMessage(message);


        verify(employeeService, never()).createEmployee(any());
    }

    @Test
    void testReceiveUpdateEmployeeMessage_Success() throws Exception {
        String message = "{\"id\":\"123\",\"firstName\":\"UpdatedName\"}";
        UpdateEmployeeRequest mockRequest = new UpdateEmployeeRequest();
        mockRequest.setId("123");
        mockRequest.setFirstName("UpdatedName");

        when(objectMapper.readValue(message, UpdateEmployeeRequest.class)).thenReturn(mockRequest);

        employeeMessageListener.receiveUpdateEmployeeMessage(message);

        verify(employeeService, times(1)).updateEmployee("123", mockRequest);
    }
    @Test
    void testReceiveUpdateEmployeeMessage_Exception() throws Exception {
        String message = "{\"id\":\"123\"}";

        when(objectMapper.readValue(message, UpdateEmployeeRequest.class)).thenThrow(new RuntimeException("Parse error"));

        employeeMessageListener.receiveUpdateEmployeeMessage(message);

        verify(employeeService, never()).updateEmployee(any(), any());
    }
}
