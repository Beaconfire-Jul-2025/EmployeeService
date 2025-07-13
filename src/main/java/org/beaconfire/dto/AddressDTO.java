package org.beaconfire.dto;


import lombok.Data;

@Data
public class AddressDTO {
    private String id;
    private String type;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String zipCode;
}
