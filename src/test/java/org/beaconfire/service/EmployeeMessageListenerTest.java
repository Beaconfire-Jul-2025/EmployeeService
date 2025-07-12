package org.beaconfire.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.beaconfire.dto.CreateEmployeeRequest;
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


    private static final Logger log = LoggerFactory.getLogger(EmployeeMessageListenerTest.class);


    @Test
    void testReceiveEmployeeMessage_Success() throws Exception {
        // 准备假数据
        String message = "{\"firstName\":\"John\",\"lastName\":\"Doe\",\"email\":\"john.doe@example.com\"}";
        CreateEmployeeRequest mockRequest = new CreateEmployeeRequest();
        mockRequest.setFirstName("John");
        mockRequest.setLastName("Doe");
        mockRequest.setEmail("john.doe@example.com");

        // mock ObjectMapper
        when(objectMapper.readValue(message, CreateEmployeeRequest.class)).thenReturn(mockRequest);

        // 执行方法
        employeeMessageListener.receiveEmployeeMessage(message);

        // 验证 service 是否被调用
        verify(employeeService, times(1)).createEmployee(mockRequest);
    }

    @Test
    void testReceiveEmployeeMessage_Exception() throws Exception {
        String message = "{\"firstName\":\"John\"}";

        // mock 抛出异常
        when(objectMapper.readValue(message, CreateEmployeeRequest.class)).thenThrow(new RuntimeException("Parsing error"));

        // 执行
        employeeMessageListener.receiveEmployeeMessage(message);

        // 验证 service 不被调用
        verify(employeeService, never()).createEmployee(any());
    }

}
