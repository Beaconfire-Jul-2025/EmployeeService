package org.beaconfire.dto;

import lombok.Data;
import org.beaconfire.model.Address;
import org.beaconfire.model.Contact;

import java.time.LocalDate;
import java.util.List;

@Data
public class CreateEmployeeRequest {
    private String userId;
    private String firstName;
    private String lastName;
    private String middleName;
    private String preferredName;
    private String email;
    private String avatarPath;
    private String cellPhone;
    private String workPhone;
    private String gender;
    private String ssn;
    private LocalDate dob;
    private LocalDate startDate;
    private String houseId;
    private String applicationType;

    private List<Address> addresses;
    private WorkAuthorizationDTO workAuthorization;
    private DriverLicenseDTO driverLicense;
    private List<Contact> emergencyContacts;
}
