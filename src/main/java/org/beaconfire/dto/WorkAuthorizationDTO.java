package org.beaconfire.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class WorkAuthorizationDTO {
    private Boolean isUsCitizen;
    private Boolean greenCardHolder;
    private String type;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate lastModificationDate;
}
