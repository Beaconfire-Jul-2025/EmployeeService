package org.beaconfire.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetEmployeeByHouseResponse {
    private String name;
    private String phone;
}
