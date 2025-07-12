package org.beaconfire.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "Employee")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Employee {

    @Id
    private String id;

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
    private LocalDate dob;


    // Employment Info
    private LocalDate startDate;
    private LocalDate endDate;

    private String workAuthType;
    private LocalDate workAuthStartDate;
    private LocalDate workAuthEndDate;

    private String driverLicense;
    private LocalDate driverLicenseExpiration;

    // Housing Info
    private String houseId;

    // Embedded documents
    private List<Address> addressList;
    private List<Contact> contacts;
    private List<VisaStatus> visaStatuses;
    private List<PersonalDocument> personalDocuments;

    // Status
    private Boolean activeFlag;
    private LocalDateTime createDate;
    private LocalDateTime lastModificationDate;

}
