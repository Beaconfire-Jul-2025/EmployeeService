package org.beaconfire.dto;

import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateEmployeeRequest {

    private String id;

    private String firstName;
    private String lastName;
    private String email;

    private String gender;
    private LocalDateTime dob;

    private List<AddressRequest> addresses;
    private LocalDateTime startDate;

    private WorkAuthorizationRequest workAuthorization;

    private List<EmergencyContactRequest> emergencyContacts;
    private DriverLicenseDTO driverLicense;

    private String applicationType;
}
