package org.beaconfire.employee.model;

import lombok.Data;
import java.util.Date;

@Data
public class PersonalDocument {
    private String id;
    private String type; // DRIVER_LICENSE_PROOF, WORK_AUTHORIZATION_PROOF, SSN_CARD, PASSPORT, OTHER
    private String path;
    private String title;
    private String comment;
    private Date createDate;
}

