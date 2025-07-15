package org.beaconfire.employee.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.beaconfire.employee.model.PersonalDocument;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetDocumentsResponse {
    private List<PersonalDocument> documents;
    private String message;
}