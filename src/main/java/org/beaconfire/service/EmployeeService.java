package org.beaconfire.service;

import org.beaconfire.model.Employee;
import org.beaconfire.model.PersonalDocument;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {

    Employee getEmployeeById(String id);


}