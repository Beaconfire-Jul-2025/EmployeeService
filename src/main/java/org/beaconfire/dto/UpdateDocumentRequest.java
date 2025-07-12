package org.beaconfire.dto;

import lombok.Data;

@Data
public class UpdateDocumentRequest {
    private String title;
    private String path;
    private String comment;
}