package org.beaconfire.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DriverLicense {
    private Boolean hasLicense;
    private String licenseNumber;
    private LocalDate expirationDate;
}
