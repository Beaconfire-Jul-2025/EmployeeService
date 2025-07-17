package org.beaconfire.employee.model;

import lombok.Data;

@Data
public class Reference {
    private String id;
    private String firstName;
    private String lastName;
    private String middleName;
    private String phone;
    private String email;
    private String relationship;
    private Address address;
}

