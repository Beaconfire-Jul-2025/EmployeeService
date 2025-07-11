package org.beaconfire.dto;


import lombok.Data;

@Data
public class CreateEmployeeRequest {
    private String userId;
    private String firstName;
    private String lastName;
    private String middleName;
    private String email;
}
