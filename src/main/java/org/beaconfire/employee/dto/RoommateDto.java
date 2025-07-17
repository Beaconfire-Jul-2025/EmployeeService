package org.beaconfire.employee.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoommateDto {
    private String firstName;
    private String lastName;
    private String middleName;
    private String preferredName;
    private String avatarPath;
    private String email;
    private String cellPhone;
    private String alternatePhone;
}

