package org.beaconfire.employee.model;

import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DriverLicense {

    private Boolean hasLicense;
    private String licenseNumber;
    private LocalDateTime expirationDate;
}