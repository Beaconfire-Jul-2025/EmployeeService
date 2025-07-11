package org.beaconfire.model;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Contact {

    private String firstName;
    private String lastName;
    private String cellPhone;
    private String alternatePhone;
    private String email;
    private String relationship;
    private String type;
}