package org.beaconfire.employee.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ValidateEmployeeInfoResponse {
    private String employeeId;
    private String message;
}
