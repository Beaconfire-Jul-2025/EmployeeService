package org.beaconfire.employee.controller;

import lombok.AllArgsConstructor;
import org.beaconfire.employee.dto.RoommateDto;
import org.beaconfire.employee.model.Employee;
import org.beaconfire.employee.service.EmployeeService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/roommate")
@AllArgsConstructor
public class RoommateController {
    private EmployeeService employeeService;

    @GetMapping
    @PreAuthorize("hasAnyRole('EMPLOYEE')")
    public List<RoommateDto> getRoommates() {
        Object principal = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = principal.toString();
        Optional<Employee> existingProfile = employeeService.getEmployeeByUserId(userId);
        if (existingProfile.isPresent()) {
            String houseId = existingProfile.get().getHouseId();
            List<Employee> roommates = employeeService.findRoommatesByHouseId(houseId, userId);
            return roommates.stream().map(e -> new RoommateDto(
                    e.getFirstName(),
                    e.getLastName(),
                    e.getMiddleName(),
                    e.getPreferredName(),
                    e.getAvatarPath(),
                    e.getEmail(),
                    e.getCellPhone(),
                    e.getAlternatePhone()
            )).collect(Collectors.toList());
        } else {
            throw new IllegalStateException("No profile found for userId: " + userId);
        }
    }

    @PreAuthorize("hasAnyRole('HR', 'COMPOSITE')")
    @GetMapping("/{houseId}")
    public List<RoommateDto> getRoommatesByHouseId(@PathVariable String houseId) {
        List<Employee> roommates = employeeService.findRoommatesByHouseId(houseId, null);
        return roommates.stream().map(e -> new RoommateDto(
                e.getFirstName(),
                e.getLastName(),
                e.getMiddleName(),
                e.getPreferredName(),
                e.getAvatarPath(),
                e.getEmail(),
                e.getCellPhone(),
                e.getAlternatePhone()
        )).collect(Collectors.toList());
    }
}
