package org.beaconfire.employee.service;

import org.beaconfire.employee.model.Employee;
import org.beaconfire.employee.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.beaconfire.employee.dto.CreateEmployeeRequest;

@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Optional<Employee> getEmployeeById(String profileId) {
        return employeeRepository.findById(profileId);
    }

    public Optional<Employee> getEmployeeByUserId(String userId) {
        Query query = new Query(Criteria.where("userId").is(userId)
            .and("applicationType").is("profile"));
        Employee employee = mongoTemplate.findOne(query, Employee.class);
        return Optional.ofNullable(employee);
    }

    public Employee createEmployee(CreateEmployeeRequest request) {
        Employee employee = Employee.builder()
            .firstName(request.getFirstName())
            .lastName(request.getLastName())
            .middleName(request.getMiddleName())
            .preferredName(request.getPreferredName())
            .avatarPath(request.getAvatarPath())
            .email(request.getEmail())
            .cellPhone(request.getCellPhone())
            .alternatePhone(request.getAlternatePhone())
            .gender(request.getGender())
            .ssn(request.getSsn())
            .dob(request.getDob())
            .startDate(request.getStartDate())
            .endDate(request.getEndDate())
            .houseId(request.getHouseId())
            .addresses(request.getAddresses())
            .workAuthorization(request.getWorkAuthorization())
            .driverLicense(request.getDriverLicense())
            .emergencyContacts(request.getEmergencyContacts())
            .references(request.getReferences())
            .personalDocuments(request.getPersonalDocuments())
            .applicationType(request.getApplicationType())
            .build();
        return employeeRepository.save(employee);
    }

    public Employee updateEmployee(String profileId, Employee employee) {
        employee.setId(profileId);
        return employeeRepository.save(employee);
    }

    public void deleteEmployee(String profileId) {
        employeeRepository.deleteById(profileId);
    }

    public Page<Employee> getEmployees(String firstName, String lastName, String email, int page, int size, String sortBy, String sortDir) {
        Query query = new Query();
        if (firstName != null && !firstName.isEmpty()) {
            query.addCriteria(Criteria.where("firstName").regex(firstName, "i"));
        }
        if (lastName != null && !lastName.isEmpty()) {
            query.addCriteria(Criteria.where("lastName").regex(lastName, "i"));
        }
        if (email != null && !email.isEmpty()) {
            query.addCriteria(Criteria.where("email").regex(email, "i"));
        }
        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        long total = mongoTemplate.count(query, Employee.class);
        query.with(pageable);
        List<Employee> employees = mongoTemplate.find(query, Employee.class);
        return new org.springframework.data.domain.PageImpl<>(employees, pageable, total);
    }

    public List<Employee> findRoommatesByHouseId(String houseId, String excludeUserId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("houseId").is(houseId)
                .and("applicationType").is("profile")
                .and("userId").ne(excludeUserId));
        return mongoTemplate.find(query, Employee.class);
    }
}
