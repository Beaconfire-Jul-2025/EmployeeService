package org.beaconfire.employee.controller;

import lombok.AllArgsConstructor;
import org.beaconfire.employee.model.Employee;
import org.beaconfire.employee.service.EmployeeService;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@RequestMapping("/profile")
@AllArgsConstructor
public class ProfileController {
    private EmployeeService employeeService;

    @GetMapping
    public Employee getProfile() {
        Object principal = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = principal.toString();
        Optional<Employee> employee = employeeService.getEmployeeByUserId(userId);
        return employee.orElse(null);
    }

    @PutMapping
    public Employee updateProfile(@RequestBody Employee profileUpdate) {
        Object principal = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = principal.toString();
        Optional<Employee> existingProfile = employeeService.getEmployeeByUserId(userId);
        if (existingProfile.isPresent()) {
            Employee profile = existingProfile.get();
            profileUpdate.setId(profile.getId());
            profileUpdate.setUserId(userId);
            profileUpdate.setApplicationType("profile");
            return employeeService.updateEmployee(profile.getId(), profileUpdate);
        } else {
            throw new IllegalStateException("No profile found for userId: " + userId);
        }
    }
}
