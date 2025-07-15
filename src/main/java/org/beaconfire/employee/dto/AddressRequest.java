package org.beaconfire.employee.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressRequest {

    private String id;
    private String type;
    private String addressLine1;
    private String city;
    private String state;
    private String zipCode;
}
