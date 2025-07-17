package org.beaconfire.employee.model;

import lombok.Data;
import java.util.Date;

@Data
public class WorkAuthorization {
    private Boolean isUsCitizen;
    private Boolean greenCardHolder;
    private String type; // H1B, L2, F1, H4, OTHER, N/A
    private Date startDate;
    private Date endDate;
    private Date lastModificationDate;
}

