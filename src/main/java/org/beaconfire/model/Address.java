package org.beaconfire.model;

import lombok.*;
import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Address {
    @Id
    private String id;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String zipCode;
}