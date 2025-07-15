package org.beaconfire.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class UpdateEmployeeRequest {
    private String id;

    private String firstName;
    private String lastName;
    private String middleName;
    private String preferredName;
    private String cellPhone;
    private String workPhone;
    private String gender;
    private String ssn;
    private LocalDate dob;
    private LocalDate startDate;
    private LocalDate endDate;
    private String workAuthType;
    private LocalDate workAuthStartDate;
    private LocalDate workAuthEndDate;
    private DriverLicenseDTO driverLicense;
}
