package org.beaconfire.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkAuthorizationRequest {

    private Boolean isUsCitizen;
    private Boolean greenCardHolder;
    private String type;
}
