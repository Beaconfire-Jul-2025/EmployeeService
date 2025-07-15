package org.beaconfire.employee.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ValidateEmployeeInfoRequest {

    private String firstName;
    private String lastName;
    private String email;
    private String gender;
    private LocalDateTime dob;

    private List<AddressRequest> addresses;

    private WorkAuthorizationRequest workAuthorization;

    private DriverLicenseDTO driverLicense;

    private List<EmergencyContactRequest> emergencyContacts;

    private String applicationType;
}
