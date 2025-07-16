package org.beaconfire.employee.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UploadDocumentRequest {
    private String type;
    private String title;
    private String path;
    private String comment;
    private LocalDate createDate;
}