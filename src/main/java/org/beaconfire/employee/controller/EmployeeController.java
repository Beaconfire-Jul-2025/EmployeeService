package org.beaconfire.employee.controller;

import lombok.AllArgsConstructor;
import org.beaconfire.employee.dto.CreateEmployeeRequest;
import org.beaconfire.employee.dto.PageListResponse;
import org.beaconfire.employee.model.Employee;
import org.beaconfire.employee.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping()
@AllArgsConstructor
public class EmployeeController {

    private EmployeeService employeeService;

    @GetMapping
    public PageListResponse<Employee> getEmployees(
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String email,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDir
    ) {
        Page<Employee> employees = employeeService.getEmployees(firstName, lastName, email, page, size, sortBy, sortDir);
        return PageListResponse.<Employee>builder()
                .list(employees.getContent())
                .current(employees.getNumber())
                .pageSize(employees.getSize())
                .total(employees.getTotalElements())
                .build();
    }

    @GetMapping("/{profileId}")
    public Employee getEmployeeById(@PathVariable String profileId) {
        Optional<Employee> employee = employeeService.getEmployeeById(profileId);
        return employee.orElse(null);
    }

    @PostMapping
    public Map<String, String> createEmployee(@RequestBody CreateEmployeeRequest request) {
        Employee employee = employeeService.createEmployee(request);
        Map<String, String> response = new HashMap<>();
        response.put("profileId", employee.getId());
        response.put("userId", employee.getUserId());
        return response;
    }

    @PutMapping("/{profileId}")
    public Employee updateEmployee(@PathVariable String profileId, @RequestBody Employee employee) {
        return employeeService.updateEmployee(profileId, employee);
    }

    @DeleteMapping("/{profileId}")
    public void deleteEmployee(@PathVariable String profileId) {
        employeeService.deleteEmployee(profileId);
    }
}
