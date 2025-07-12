package org.beaconfire.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.beaconfire.dto.CreateEmployeeRequest;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeMessageListener {

    private final EmployeeService employeeService;
    private final ObjectMapper objectMapper;

    @RabbitListener(queues = "employee.create.queue")
    public void receiveEmployeeMessage(String message) {
        try {
            log.info("Received message from queue: {}", message);

            // JSON ->CreateEmployeeRequest
            CreateEmployeeRequest request = objectMapper.readValue(message, CreateEmployeeRequest.class);

            // call service to create employee
            employeeService.createEmployee(request);
            log.info("Employee created successfully!");
        } catch (Exception e) {
            log.error("Error processing employee creation message", e);
        }
    }
}
