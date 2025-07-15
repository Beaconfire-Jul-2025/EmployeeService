package org.beaconfire.model;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


import lombok.*;
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
    private String email;

    private String gender;

    private LocalDate dob;         // 出生日期
    private LocalDate startDate;   // 入职日期

    private List<Address> addresses;

    private WorkAuthorization workAuthorization;

    private List<EmergencyContact> emergencyContacts;

    private String applicationType;  // 例如：ONBOARD
}
