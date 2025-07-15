package org.beaconfire.model;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkAuthorization {
    private Boolean isUsCitizen;
    private Boolean greenCardHolder;
    private String type;
}