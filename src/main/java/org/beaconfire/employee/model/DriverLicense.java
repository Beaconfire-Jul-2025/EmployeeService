package org.beaconfire.employee.model;

import lombok.Data;
import java.util.Date;

@Data
public class DriverLicense {
    private Boolean hasLicense;
    private String licenseNumber;
    private Date expirationDate;
}

