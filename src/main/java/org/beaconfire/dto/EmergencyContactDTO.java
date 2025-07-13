package org.beaconfire.dto;

import lombok.Data;

@Data
public class EmergencyContactDTO {
    private String id;
    private String firstName;
    private String lastName;
    private String relationship;
    private String cellPhone;
    private String email;
    private String type;
}
