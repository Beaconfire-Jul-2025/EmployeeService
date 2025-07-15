package org.beaconfire.model;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkAuthorization {
    private Boolean isUsCitizen;
    private Boolean greenCardHolder;
    private String type;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime lastModificationDate;
}