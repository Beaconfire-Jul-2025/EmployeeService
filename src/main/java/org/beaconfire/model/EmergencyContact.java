package org.beaconfire.model;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmergencyContact  {
    private String id;
    private String firstName;
    private String lastName;
    private String relationship;
    private String cellPhone;
}