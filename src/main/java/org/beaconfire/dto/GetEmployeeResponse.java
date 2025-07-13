package org.beaconfire.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
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
    private LocalDate startDate;
    private String houseId;
    private List<AddressDTO> addresses;
    private WorkAuthorizationDTO workAuthorization;
    private DriverLicenseDTO driverLicense;
    private List<EmergencyContactDTO> emergencyContacts;
    private String applicationType;
}
