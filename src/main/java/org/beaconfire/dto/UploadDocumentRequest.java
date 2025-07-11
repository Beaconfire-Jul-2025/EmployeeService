package org.beaconfire.dto;

import lombok.Data;

@Data
public class UploadDocumentRequest {
    private String title;
    private String path;   // sudo path for test the function ,wait for AWS key
    private String comment;
}