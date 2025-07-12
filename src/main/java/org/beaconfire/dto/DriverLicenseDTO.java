package org.beaconfire.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class DriverLicenseDTO {
    private Boolean hasLicense;
    private String licenseNumber;
    private LocalDate expirationDate;
}
