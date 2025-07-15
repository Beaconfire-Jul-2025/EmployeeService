package org.beaconfire.employee.model;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Address {

    private String id;
    private String type;
    private String addressLine1;
    private String city;
    private String state;
    private String zipCode;
}