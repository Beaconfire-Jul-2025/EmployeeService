package org.beaconfire.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetEmployeeResponse {
    private String id;
    private String userId;
    private String firstName;
    private String lastName;
    private String middleName;
    private String preferredName;
    private String email;
    private String avatarPath;
    private String cellPhone;
    private String workPhone;
    private String gender;
    private String ssn;
    private LocalDate dob;
    private LocalDateTime startDate;
    private String houseId;
    private List<AddressRequest> addresses;
    private WorkAuthorizationRequest workAuthorization;
    private DriverLicenseDTO driverLicense;
    private List<EmergencyContactRequest> emergencyContacts;
    private String applicationType;
}
