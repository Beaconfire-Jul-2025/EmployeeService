package org.beaconfire.model;

import lombok.*;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PersonalDocument {
    @Id
    private String id;
    private String path;
    private String title;
    private String comment;
    private LocalDateTime createDate;
}