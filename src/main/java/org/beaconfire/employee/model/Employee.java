package org.beaconfire.employee.model;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;



import java.time.LocalDateTime;
import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "Employee")
public class Employee {

    @Id
    private String id;

    private String userId;

    private String firstName;
    private String lastName;
    private String preferredName;
    private String email;

    private String gender;

    private LocalDateTime dob;

    private List<Address> addresses;

    private String cellPhone;
    private String workPhone;
    private WorkAuthorization workAuthorization;

    private List<EmergencyContact> emergencyContacts;
    private DriverLicense driverLicense;
    private LocalDateTime startDate;
    private String houseId;

    private String applicationType;
    private List<PersonalDocument> personalDocuments;
}
