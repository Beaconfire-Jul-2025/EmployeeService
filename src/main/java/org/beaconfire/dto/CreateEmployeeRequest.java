package org.beaconfire.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateEmployeeRequest {

    private String userId;

    private String firstName;
    private String lastName;
    private String email;

    private String gender;

    private LocalDate dob;
    private LocalDate startDate;

    private List<AddressRequest> addresses;

    private WorkAuthorizationRequest workAuthorization;

    private List<EmergencyContactRequest> emergencyContacts;

    private String applicationType;
}
