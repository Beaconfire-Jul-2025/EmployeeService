package org.beaconfire.employee.controller;

import lombok.AllArgsConstructor;
import org.beaconfire.employee.model.Employee;
import org.beaconfire.employee.service.EmployeeService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/profile")
@AllArgsConstructor
public class ProfileController {
    private final static String APPLICATION_TYPE_PROFILE = "PROFILE";
    private EmployeeService employeeService;

    @GetMapping
    @PreAuthorize("hasAnyRole('EMPLOYEE')")
    public Employee getProfile() {
        Object principal = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = principal.toString();
        Optional<Employee> employee = employeeService.getEmployeeByUserId(userId, APPLICATION_TYPE_PROFILE);
        return employee.orElse(null);
    }

    @PutMapping
    @PreAuthorize("hasAnyRole('EMPLOYEE')")
    public Employee updateProfile(@RequestBody Employee profileUpdate) {
        Object principal = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = principal.toString();
        Optional<Employee> existingProfile = employeeService.getEmployeeByUserId(userId, APPLICATION_TYPE_PROFILE);
        if (existingProfile.isPresent()) {
            Employee profile = existingProfile.get();
            profileUpdate.setId(profile.getId());
            profileUpdate.setUserId(userId);
            profileUpdate.setApplicationType(APPLICATION_TYPE_PROFILE);
            return employeeService.updateEmployee(profile.getId(), profileUpdate);
        } else {
            throw new IllegalStateException("No profile found for userId: " + userId);
        }
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasAnyRole('HR', 'COMPOSITE')")
    public Employee getProfileByUserId(@PathVariable String userId) {
        Optional<Employee> employee = employeeService.getEmployeeByUserId(userId, APPLICATION_TYPE_PROFILE);
        return employee.orElse(null);
    }
}
