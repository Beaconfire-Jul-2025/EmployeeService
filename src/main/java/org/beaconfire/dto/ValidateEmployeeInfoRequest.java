package org.beaconfire.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.beaconfire.model.Address;

import java.time.LocalDate;



@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ValidateEmployeeInfoRequest {
    private String firstName;
    private String lastName;
    private String middleName;
    private String preferredName;
    private String avatarUrl;
    private Address currentAddress;
    private String cellPhone;
    private String workPhone;
    private String email; // 不可编辑

    private String ssn;
    private LocalDate dateOfBirth;
    private String gender;

    private String workAuthorizationType;
    private LocalDate workAuthStartDate;
    private LocalDate workAuthEndDate;
    private String otherWorkAuthDescription;
    private String workAuthDocumentPath;

    private Boolean hasDriverLicense;
    private String driverLicenseNumber;
    private LocalDate driverLicenseExpiration;
    private String driverLicensePath;

    // Reference
    private String refFirstName;
    private String refLastName;
    private String refMiddleName;
    private String refPhone;
    private String refAddress;
    private String refEmail;
    private String refRelationship;

    // Emergency Contact
    private String emergencyFirstName;
    private String emergencyLastName;
    private String emergencyMiddleName;
    private String emergencyPhone;
    private String emergencyEmail;
    private String emergencyRelationship;
}
