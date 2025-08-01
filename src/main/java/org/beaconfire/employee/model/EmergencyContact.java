package org.beaconfire.employee.model;

import lombok.Data;

@Data
public class EmergencyContact {
    private String id;
    private String firstName;
    private String lastName;
    private String middleName;
    private String cellPhone;
    private String alternatePhone;
    private String email;
    private String relationship;
    private Address address;
}

