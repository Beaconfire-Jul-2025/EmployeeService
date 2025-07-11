package org.beaconfire.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.beaconfire.model.Employee;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetEmployeeResponse {
    private Employee employee;
    private String message;
}
