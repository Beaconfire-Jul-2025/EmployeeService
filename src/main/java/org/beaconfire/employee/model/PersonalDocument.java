package org.beaconfire.employee.model;

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PersonalDocument {

    private String path;
    private String title;
    private String comment;
    private LocalDateTime createDate;
}