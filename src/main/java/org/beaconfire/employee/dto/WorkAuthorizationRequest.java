package org.beaconfire.employee.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkAuthorizationRequest {

    private Boolean isUsCitizen;
    private Boolean greenCardHolder;
    private String type;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime lastModificationDate;
}
