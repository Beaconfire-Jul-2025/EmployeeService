package org.beaconfire.employee.dto;

import lombok.Data;
import org.beaconfire.employee.model.Address;
import org.beaconfire.employee.model.WorkAuthorization;
import org.beaconfire.employee.model.DriverLicense;
import org.beaconfire.employee.model.EmergencyContact;
import org.beaconfire.employee.model.Reference;
import org.beaconfire.employee.model.PersonalDocument;
import java.util.Date;
import java.util.List;

@Data
public class EmployeeDTO {
    private String userId;
    private String firstName;
    private String lastName;
    private String middleName;
    private String preferredName;
    private String avatarPath;
    private String email;
    private String cellPhone;
    private String alternatePhone;
    private String gender;
    private String ssn;
    private Date dob;
    private Date startDate;
    private Date endDate;
    private String houseId;
    private List<Address> addresses;
    private WorkAuthorization workAuthorization;
    private DriverLicense driverLicense;
    private List<EmergencyContact> emergencyContacts;
    private List<Reference> references;
    private List<PersonalDocument> personalDocuments;
    private String applicationType;
}

