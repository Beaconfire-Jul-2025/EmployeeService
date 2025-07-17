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
            @RequestParam(defaultValue = "userId") String sortBy,
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

    @GetMapping("/{userId}")
    public Employee getEmployeeById(@PathVariable String userId) {
        Optional<Employee> employee = employeeService.getEmployeeById(userId);
        return employee.orElse(null);
    }

    @PostMapping
    public Map<String, String> createEmployee(@RequestBody CreateEmployeeRequest request) {
        Employee employee = employeeService.createEmployee(request);
        Map<String, String> response = new HashMap<>();
        response.put("employeeId", employee.getUserId());
        return response;
    }

    @PutMapping("/{userId}")
    public Employee updateEmployee(@PathVariable String userId, @RequestBody Employee employee) {
        return employeeService.updateEmployee(userId, employee);
    }

    @DeleteMapping("/{userId}")
    public void deleteEmployee(@PathVariable String userId) {
        employeeService.deleteEmployee(userId);
    }

    @GetMapping("/profile")
    public Employee getProfile() {
        Object principal = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = principal.toString();
        Optional<Employee> employee = employeeService.getEmployeeById(userId);
        return employee.orElse(null);
    }
}
