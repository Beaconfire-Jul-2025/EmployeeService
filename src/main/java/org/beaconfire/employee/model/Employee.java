package org.beaconfire.employee.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document(collection = "Employee")
@Data
@Builder
public class Employee {
    @Id
    private String id; // MongoDB auto-generated _id
    private String userId; // External userId from MySQL
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
