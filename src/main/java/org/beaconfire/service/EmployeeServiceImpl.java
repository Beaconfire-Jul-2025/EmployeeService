package org.beaconfire.service;

import lombok.RequiredArgsConstructor;
import org.beaconfire.dto.*;
import org.beaconfire.exception.DocumentNotFoundException;
import org.beaconfire.exception.EmployeeAlreadyExistsException;
import org.beaconfire.exception.EmployeeNotFoundException;
import org.beaconfire.model.DriverLicense;
import org.beaconfire.model.Employee;
import org.beaconfire.model.PersonalDocument;
import org.beaconfire.model.VisaStatus;
import org.beaconfire.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    @Override
    public String createEmployee(CreateEmployeeRequest request) {
        if (employeeRepository.existsByEmail(request.getEmail())) {
            throw new EmployeeAlreadyExistsException("Employee already exists with email: " + request.getEmail());
        }

        Employee employee = new Employee();

        // basic info
        employee.setUserId(request.getUserId());
        employee.setFirstName(request.getFirstName());
        employee.setLastName(request.getLastName());
        employee.setMiddleName(request.getMiddleName());
        employee.setPreferredName(request.getPreferredName());
        employee.setEmail(request.getEmail());
        employee.setAvatarPath(request.getAvatarPath());
        employee.setCellPhone(request.getCellPhone());
        employee.setWorkPhone(request.getWorkPhone());
        employee.setGender(request.getGender());
        employee.setSsn(request.getSsn());
        employee.setDob(request.getDob());
        employee.setStartDate(request.getStartDate());
        employee.setHouseId(request.getHouseId());

        // address
        employee.setAddressList(request.getAddresses());

        // visa status
        if (request.getWorkAuthorization() != null) {
            VisaStatus visaStatus = new VisaStatus();
            visaStatus.setVisaType(request.getWorkAuthorization().getType());
            visaStatus.setStartDate(request.getWorkAuthorization().getStartDate());
            visaStatus.setEndDate(request.getWorkAuthorization().getEndDate());

            if (request.getWorkAuthorization().getLastModificationDate() != null) {
                visaStatus.setLastModificationDate(request.getWorkAuthorization().getLastModificationDate().atStartOfDay());
            }

            employee.setWorkAuthType(request.getWorkAuthorization().getType());
            employee.setWorkAuthStartDate(request.getWorkAuthorization().getStartDate());
            employee.setWorkAuthEndDate(request.getWorkAuthorization().getEndDate());

            employee.setVisaStatuses(Collections.singletonList(visaStatus));
        }

        // driver license
        if (request.getDriverLicense() != null) {
            DriverLicense driverLicense = new DriverLicense();
            driverLicense.setHasLicense(request.getDriverLicense().getHasLicense());
            driverLicense.setLicenseNumber(request.getDriverLicense().getLicenseNumber());
            driverLicense.setExpirationDate(request.getDriverLicense().getExpirationDate());
            employee.setDriverLicense(driverLicense);
        } else {
            employee.setDriverLicense(null);
        }

        // contact
        employee.setContacts(request.getEmergencyContacts());

        // save
        employeeRepository.save(employee);
        return "Employee created successfully.";
    }



    @Override
    public String validateEmployeeInfo(ValidateEmployeeInfoRequest request){
        if (request.getFirstName() == null || request.getFirstName().trim().isEmpty()) {
            throw new IllegalArgumentException("First Name is required");
        }
        if (request.getLastName() == null || request.getLastName().trim().isEmpty()) {
            throw new IllegalArgumentException("Last Name is required");
        }
        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }
        if (request.getSsn() == null || request.getSsn().trim().isEmpty()) {
            throw new IllegalArgumentException("SSN is required");
        }
        if (request.getDateOfBirth() == null) {
            throw new IllegalArgumentException("Date of Birth is required");
        }
        if (request.getGender() == null || request.getGender().trim().isEmpty()) {
            throw new IllegalArgumentException("Gender is required");
        }
        //If not a citizen or green card, check for work authorization
        if (!"Citizen".equalsIgnoreCase(request.getWorkAuthorizationType())
                && !"Green Card".equalsIgnoreCase(request.getWorkAuthorizationType())) {
            if (request.getWorkAuthStartDate() == null || request.getWorkAuthEndDate() == null) {
                throw new IllegalArgumentException("Work authorization dates are required");
            }
            if (request.getWorkAuthDocumentPath() == null || request.getWorkAuthDocumentPath().trim().isEmpty()) {
                throw new IllegalArgumentException("Work authorization document is required");
            }
        }
        //valid address info
        if (request.getCurrentAddress() == null
                || request.getCurrentAddress().getAddressLine1() == null || request.getCurrentAddress().getAddressLine1().trim().isEmpty()
                || request.getCurrentAddress().getCity() == null || request.getCurrentAddress().getCity().trim().isEmpty()
                || request.getCurrentAddress().getState() == null || request.getCurrentAddress().getState().trim().isEmpty()
                || request.getCurrentAddress().getZipCode() == null || request.getCurrentAddress().getZipCode().trim().isEmpty()) {
            throw new IllegalArgumentException("Current Address with all required fields is required");
        }

        // If employee have a driver's license, check the license information
        if (Boolean.TRUE.equals(request.getHasDriverLicense())) {
            if (request.getDriverLicenseNumber() == null || request.getDriverLicenseNumber().trim().isEmpty()) {
                throw new IllegalArgumentException("Driver license number is required");
            }
            if (request.getDriverLicenseExpiration() == null) {
                throw new IllegalArgumentException("Driver license expiration date is required");
            }
            if (request.getDriverLicensePath() == null || request.getDriverLicensePath().trim().isEmpty()) {
                throw new IllegalArgumentException("Driver license document is required");
            }
        }
        //valid reference info
        if (request.getRefFirstName() == null || request.getRefFirstName().trim().isEmpty()) {
            throw new IllegalArgumentException("Reference First Name is required");
        }
        if (request.getRefPhone() == null || request.getRefPhone().trim().isEmpty()) {
            throw new IllegalArgumentException("Reference Phone is required");
        }
        if (request.getRefEmail() == null || request.getRefEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Reference Email is required");
        }
        if (request.getRefRelationship() == null || request.getRefRelationship().trim().isEmpty()) {
            throw new IllegalArgumentException("Reference Relationship is required");
        }
        // Must have an emergency contact
        if (request.getEmergencyFirstName() == null || request.getEmergencyPhone() == null) {
            throw new IllegalArgumentException("Emergency contact is required");
        }
        if (request.getEmergencyEmail() == null || request.getEmergencyEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Emergency contact email is required");
        }
        if (request.getEmergencyRelationship() == null || request.getEmergencyRelationship().trim().isEmpty()) {
            throw new IllegalArgumentException("Emergency contact relationship is required");
        }
        Employee employee = employeeRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with email: " + request.getEmail()));

        return employee.getId();
    }
    @Override
    public GetEmployeeResponse getEmployeeProfileById(String id) {
        Employee employee = getEmployeeById(id);
        if (employee == null) {
            throw new EmployeeNotFoundException("Employee not found with id: " + id);
        }

        GetEmployeeResponse response = new GetEmployeeResponse();
        response.setId(employee.getId());
        response.setUserId(employee.getUserId());
        response.setFirstName(employee.getFirstName());
        response.setLastName(employee.getLastName());
        response.setMiddleName(employee.getMiddleName());
        response.setPreferredName(employee.getPreferredName());
        response.setEmail(employee.getEmail());
        response.setAvatarPath(employee.getAvatarPath());
        response.setCellPhone(employee.getCellPhone());
        response.setWorkPhone(employee.getWorkPhone());
        response.setGender(employee.getGender());
        response.setSsn(employee.getSsn());
        response.setDob(employee.getDob());
        response.setStartDate(employee.getStartDate());
        response.setHouseId(employee.getHouseId());

        // addresses
        if (employee.getAddressList() != null) {
            List<AddressDTO> addressDTOs = employee.getAddressList().stream().map(addr -> {
                AddressDTO dto = new AddressDTO();
                dto.setId(addr.getId());
                dto.setAddressLine1(addr.getAddressLine1());
                dto.setAddressLine2(addr.getAddressLine2());
                //dto.setType(addr.getType());
                dto.setCity(addr.getCity());
                dto.setState(addr.getState());
                dto.setZipCode(addr.getZipCode());
                return dto;
            }).collect(Collectors.toList());
            response.setAddresses(addressDTOs);
        } else {
            response.setAddresses(new ArrayList<>());
        }

        // work authorization
        if (employee.getVisaStatuses() != null && !employee.getVisaStatuses().isEmpty()) {
            VisaStatus visa = employee.getVisaStatuses().get(0);
            WorkAuthorizationDTO waDTO = new WorkAuthorizationDTO();
            waDTO.setType(visa.getVisaType());
            waDTO.setStartDate(visa.getStartDate());
            waDTO.setEndDate(visa.getEndDate());
            waDTO.setLastModificationDate(
                    visa.getLastModificationDate() != null ? visa.getLastModificationDate().toLocalDate() : null
            );
            waDTO.setIsUsCitizen(false);
            waDTO.setGreenCardHolder(false);
            response.setWorkAuthorization(waDTO);
        }

        // driver license
        DriverLicenseDTO dlDTO = new DriverLicenseDTO();
        if (employee.getDriverLicense() != null) {
            dlDTO.setHasLicense(employee.getDriverLicense().getHasLicense());
            dlDTO.setLicenseNumber(employee.getDriverLicense().getLicenseNumber());
            dlDTO.setExpirationDate(employee.getDriverLicense().getExpirationDate());
        } else {
            dlDTO.setHasLicense(false);
        }
        response.setDriverLicense(dlDTO);

        // emergency contacts
        if (employee.getContacts() != null) {
            List<EmergencyContactDTO> contactDTOs = employee.getContacts().stream().map(c -> {
                EmergencyContactDTO dto = new EmergencyContactDTO();
                dto.setId(c.getId());
                dto.setFirstName(c.getFirstName());
                dto.setLastName(c.getLastName());
                dto.setCellPhone(c.getCellPhone());
                dto.setEmail(c.getEmail());
                dto.setRelationship(c.getRelationship());
                dto.setType(c.getType());
                return dto;
            }).collect(Collectors.toList());
            response.setEmergencyContacts(contactDTOs);
        } else {
            response.setEmergencyContacts(new ArrayList<>());
        }
        //response.setApplicationType(employee.getApplicationType());


        return response;
    }

    @Override
    public Employee getEmployeeById(String id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));
    }
    @Override
    public Employee registerEmployee(CreateEmployeeRequest request) {
        if (employeeRepository.existsByEmail(request.getEmail())) {
            throw new EmployeeAlreadyExistsException(request.getEmail());
        }
        Employee employee = Employee.builder()
                .userId(request.getUserId())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .middleName(request.getMiddleName())
                .email(request.getEmail())
                .activeFlag(true)
                .createDate(LocalDateTime.now())
                .lastModificationDate(LocalDateTime.now())
                .build();
        employee.setPersonalDocuments(new ArrayList<>());

        return employeeRepository.save(employee);
    }
    @Override
    public Employee updateEmployee(String id, UpdateEmployeeRequest request) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));

        employee.setFirstName(request.getFirstName());
        employee.setLastName(request.getLastName());
        employee.setMiddleName(request.getMiddleName());
        employee.setPreferredName(request.getPreferredName());
        employee.setCellPhone(request.getCellPhone());
        employee.setWorkPhone(request.getWorkPhone());
        employee.setGender(request.getGender());
        employee.setSsn(request.getSsn());
        employee.setDob(request.getDob());
        employee.setStartDate(request.getStartDate());
        employee.setEndDate(request.getEndDate());
        employee.setWorkAuthType(request.getWorkAuthType());
        employee.setWorkAuthStartDate(request.getWorkAuthStartDate());
        employee.setWorkAuthEndDate(request.getWorkAuthEndDate());

        DriverLicenseDTO driverLicenseDTO = request.getDriverLicense();
        if (driverLicenseDTO != null) {
            DriverLicense driverLicense = new DriverLicense();
            driverLicense.setHasLicense(driverLicenseDTO.getHasLicense());
            driverLicense.setLicenseNumber(driverLicenseDTO.getLicenseNumber());
            driverLicense.setExpirationDate(driverLicenseDTO.getExpirationDate());
            employee.setDriverLicense(driverLicense);
        } else {
            employee.setDriverLicense(null);
        }

        employee.setLastModificationDate(LocalDateTime.now());

        return employeeRepository.save(employee);
    }
    @Override
    public void uploadDocument(String employeeId, UploadDocumentRequest request) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id: " + employeeId));

        PersonalDocument document = new PersonalDocument();
        document.setTitle(request.getTitle());
        document.setPath(request.getPath());
        document.setComment(request.getComment());
        document.setCreateDate(LocalDateTime.now());

        if (employee.getPersonalDocuments() == null) {
            employee.setPersonalDocuments(new ArrayList<>());
        }
        employee.getPersonalDocuments().add(document);
        employeeRepository.save(employee);
    }
    @Override
    public GetDocumentsResponse getDocumentsByEmployeeId(String employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id: " + employeeId));

        List<PersonalDocument> documents = employee.getPersonalDocuments();

        GetDocumentsResponse response = new GetDocumentsResponse();
        response.setDocuments(documents);
        response.setMessage("Documents retrieved");

        return response;
    }
    @Override
    public void updateDocument(String employeeId, UpdateDocumentRequest request) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id: " + employeeId));

        Optional<PersonalDocument> optionalDoc = employee.getPersonalDocuments().stream()
                .filter(doc -> doc.getPath().equals(request.getPath()))
                .findFirst();

        if (!optionalDoc.isPresent()) {
            throw new DocumentNotFoundException("Document not found for the given path");
        }

        PersonalDocument doc = optionalDoc.get();
        doc.setTitle(request.getTitle());
        doc.setComment(request.getComment());
        doc.setPath(request.getPath());

        employeeRepository.save(employee);
    }
    @Override
    public List<GetEmployeeByHouseResponse> getEmployeesByHouseId(String houseId) {
        List<Employee> employees = employeeRepository.findByHouseId(houseId);
        List<GetEmployeeByHouseResponse> responses = new ArrayList<>();

        for (Employee e : employees) {
            String name = (e.getPreferredName()!= null && !e.getPreferredName().isEmpty())
                    ? e.getPreferredName()
                    : e.getFirstName();
            String phone = e.getCellPhone();
            responses.add(new GetEmployeeByHouseResponse(name, phone));
        }
        return responses;
    }

    @Override
    public List<GetEmployeeResponse> searchEmployeesByName(String name) {
        List<Employee> employees = employeeRepository
                .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrPreferredNameContainingIgnoreCase(
                        name, name, name);

        List<GetEmployeeResponse> responses = new ArrayList<>();
        for (Employee e : employees) {
            GetEmployeeResponse response = new GetEmployeeResponse();
            response.setId(e.getId());
            response.setFirstName(e.getFirstName());
            response.setLastName(e.getLastName());
            response.setPreferredName(e.getPreferredName());
            response.setEmail(e.getEmail());
            response.setCellPhone(e.getCellPhone());
            responses.add(response);
        }
        return responses;
    }

    @Override
    public List<GetEmployeeResponse> getAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();

        List<GetEmployeeResponse> responses = new ArrayList<>();
        for (Employee e : employees) {
            GetEmployeeResponse response = new GetEmployeeResponse();
            response.setId(e.getId());
            response.setFirstName(e.getFirstName());
            response.setLastName(e.getLastName());
            response.setPreferredName(e.getPreferredName());
            response.setEmail(e.getEmail());
            response.setCellPhone(e.getCellPhone());
            responses.add(response);
        }
        return responses;
    }



}

