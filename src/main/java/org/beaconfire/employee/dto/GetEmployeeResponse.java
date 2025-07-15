package org.beaconfire.employee.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetEmployeeResponse {
    private String id;
    private String userId;
    private String firstName;
    private String lastName;
    private String email;
    private String gender;
    private LocalDateTime dob;
    private List<AddressRequest> addresses;
    private LocalDateTime startDate;
    private WorkAuthorizationRequest workAuthorization;
    private DriverLicenseDTO driverLicense;
    private List<EmergencyContactRequest> emergencyContacts;
    private String applicationType;
}
