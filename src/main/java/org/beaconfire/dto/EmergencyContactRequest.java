package org.beaconfire.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmergencyContactRequest {

    private String id;
    private String firstName;
    private String lastName;
    private String relationship;
    private String cellPhone;
    private String email;
}
