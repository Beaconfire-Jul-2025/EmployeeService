package org.beaconfire.model;

import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkAuthorization {
    private Boolean isUsCitizen;
    private Boolean greenCardHolder;
    private String type;
}