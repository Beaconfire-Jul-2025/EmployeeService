package org.beaconfire.employee.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.beaconfire.employee.dto.CreateEmployeeRequest;
import org.beaconfire.employee.dto.UpdateEmployeeRequest;
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
    @RabbitListener(queues = "employee.update.queue")
    public void receiveUpdateEmployeeMessage(String message) {
        try {
            log.info("Received update message from queue: {}", message);

            // JSON → UpdateEmployeeRequest
            UpdateEmployeeRequest request = objectMapper.readValue(message, UpdateEmployeeRequest.class);

            String id = request.getId(); 

            employeeService.updateEmployee(id, request);
            log.info("Employee updated successfully!");
        } catch (Exception e) {
            log.error("Error processing employee update message", e);
        }
    }
}
