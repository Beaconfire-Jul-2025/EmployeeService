package org.beaconfire.employee.service;

import lombok.RequiredArgsConstructor;

import org.beaconfire.employee.dto.*;
import org.beaconfire.employee.exception.DocumentAlreadyExistsException;
import org.beaconfire.employee.exception.DocumentNotFoundException;
import org.beaconfire.employee.exception.EmployeeAlreadyExistsException;
import org.beaconfire.employee.exception.EmployeeNotFoundException;
import org.beaconfire.employee.model.*;

import org.beaconfire.employee.repository.EmployeeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
        List<Address> addressList = null;
        if (request.getAddresses() != null) {
            addressList = request.getAddresses().stream().map(addrReq -> Address.builder()
                    .id(addrReq.getId())
                    .type(addrReq.getType())
                    .addressLine1(addrReq.getAddressLine1())
                    .city(addrReq.getCity())
                    .state(addrReq.getState())
                    .zipCode(addrReq.getZipCode())
                    .build()
            ).collect(Collectors.toList());
        }
        List<EmergencyContact> emergencyContacts = null;
        if (request.getEmergencyContacts() != null) {
            emergencyContacts = request.getEmergencyContacts().stream().map(emcReq -> EmergencyContact.builder()
                    .id(emcReq.getId())
                    .firstName(emcReq.getFirstName())
                    .lastName(emcReq.getLastName())
                    .relationship(emcReq.getRelationship())
                    .cellPhone(emcReq.getCellPhone())
                    .build()
            ).collect(Collectors.toList());
        }

        WorkAuthorization workAuth = null;
        if (request.getWorkAuthorization() != null) {
            workAuth = WorkAuthorization.builder()
                    .isUsCitizen(request.getWorkAuthorization().getIsUsCitizen())
                    .greenCardHolder(request.getWorkAuthorization().getGreenCardHolder())
                    .type(request.getWorkAuthorization().getType())
                    .startDate(request.getWorkAuthorization().getStartDate())
                    .endDate(request.getWorkAuthorization().getEndDate())
                    .lastModificationDate(request.getWorkAuthorization().getLastModificationDate())
                    .build();
        }
        DriverLicense driverLicense = null;
        if (request.getDriverLicense() != null) {
            driverLicense = DriverLicense.builder()
                    .hasLicense(request.getDriverLicense().getHasLicense())
                    .licenseNumber(request.getDriverLicense().getLicenseNumber())
                    .expirationDate(request.getDriverLicense().getExpirationDate())

                    .build();
        }

        Employee employee = Employee.builder()
                .userId(request.getUserId())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .gender(request.getGender())
                .dob(request.getDob())
                .addresses(addressList)
                .workAuthorization(workAuth)
                .emergencyContacts(emergencyContacts)
                .startDate(request.getStartDate())
                .driverLicense(driverLicense)
                .applicationType(request.getApplicationType())
                .build();

        employeeRepository.save(employee);
        return "Employee created successfully.";
    }


    @Override
    public String validateEmployeeInfo(ValidateEmployeeInfoRequest request) {
        // 基础字段校验
        if (request.getFirstName() == null || request.getFirstName().trim().isEmpty()) {
            throw new IllegalArgumentException("First Name is required");
        }
        if (request.getLastName() == null || request.getLastName().trim().isEmpty()) {
            throw new IllegalArgumentException("Last Name is required");
        }
        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }
        if (request.getDob() == null) {
            throw new IllegalArgumentException("Date of Birth is required");
        }
        if (request.getGender() == null || request.getGender().trim().isEmpty()) {
            throw new IllegalArgumentException("Gender is required");
        }

        if (request.getAddresses() == null || request.getAddresses().isEmpty()) {
            throw new IllegalArgumentException("At least one address is required");
        }
        AddressRequest primaryAddress = request.getAddresses().get(0);
        if (primaryAddress.getAddressLine1() == null || primaryAddress.getAddressLine1().trim().isEmpty()
                || primaryAddress.getCity() == null || primaryAddress.getCity().trim().isEmpty()
                || primaryAddress.getState() == null || primaryAddress.getState().trim().isEmpty()
                || primaryAddress.getZipCode() == null || primaryAddress.getZipCode().trim().isEmpty()) {
            throw new IllegalArgumentException("Primary address fields are required");
        }

        if (request.getWorkAuthorization() != null) {
            WorkAuthorizationRequest wa = request.getWorkAuthorization();

            // 基础校验
            if (wa.getIsUsCitizen() == null && wa.getGreenCardHolder() == null && (wa.getType() == null || wa.getType().trim().isEmpty())) {
                throw new IllegalArgumentException("Work authorization information is incomplete");
            }

            // 如果不是公民或绿卡，校验日期字段
            boolean notCitizen = Boolean.FALSE.equals(wa.getIsUsCitizen());
            boolean notGreenCard = Boolean.FALSE.equals(wa.getGreenCardHolder());

            if ((notCitizen && notGreenCard) &&
                    (wa.getStartDate() == null || wa.getEndDate() == null)) {
                throw new IllegalArgumentException("Work authorization start and end dates are required for non-citizen and non-green card holders");
            }
        }

        if (request.getDriverLicense() != null && Boolean.TRUE.equals(request.getDriverLicense().getHasLicense())) {
            DriverLicenseDTO dl = request.getDriverLicense();
            if (dl.getLicenseNumber() == null || dl.getLicenseNumber().trim().isEmpty()) {
                throw new IllegalArgumentException("Driver license number is required");
            }
            if (dl.getExpirationDate() == null) {
                throw new IllegalArgumentException("Driver license expiration date is required");
            }
        }

        if (request.getEmergencyContacts() == null || request.getEmergencyContacts().isEmpty()) {
            throw new IllegalArgumentException("At least one emergency contact is required");
        }
        EmergencyContactRequest ec = request.getEmergencyContacts().get(0);
        if (ec.getFirstName() == null || ec.getFirstName().trim().isEmpty()
                || ec.getCellPhone() == null || ec.getCellPhone().trim().isEmpty()
                || ec.getRelationship() == null || ec.getRelationship().trim().isEmpty()) {
            throw new IllegalArgumentException("Emergency contact fields are required");
        }

        Employee employee = employeeRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with email: " + request.getEmail()));

        return employee.getId();
    }

    @Override
    public GetEmployeeResponse getEmployeeProfileById(String id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id: " + id));
        // 地址
        List<AddressRequest> addressDTOs = null;
        if (employee.getAddresses() != null) {
            addressDTOs = employee.getAddresses().stream().map(addr -> AddressRequest.builder()
                    .id(addr.getId())
                    .type(addr.getType())
                    .addressLine1(addr.getAddressLine1())
                    .city(addr.getCity())
                    .state(addr.getState())
                    .zipCode(addr.getZipCode())
                    .build()
            ).collect(Collectors.toList());
        }


        // 工作授权
        WorkAuthorizationRequest workAuthDTO = null;
        if (employee.getWorkAuthorization() != null) {
            Boolean isUsCitizen = employee.getWorkAuthorization().getIsUsCitizen();
            Boolean greenCardHolder = employee.getWorkAuthorization().getGreenCardHolder();

            WorkAuthorizationRequest.WorkAuthorizationRequestBuilder builder = WorkAuthorizationRequest.builder()
                    .isUsCitizen(isUsCitizen)
                    .greenCardHolder(greenCardHolder)
                    .type(employee.getWorkAuthorization().getType());

            // 如果既不是美国公民，也不是绿卡
            if (Boolean.FALSE.equals(isUsCitizen) && Boolean.FALSE.equals(greenCardHolder)) {
                builder
                        .startDate(employee.getWorkAuthorization().getStartDate())
                        .endDate(employee.getWorkAuthorization().getEndDate());
            }

            workAuthDTO = builder.build();
        }


        // 驾照
        DriverLicenseDTO driverLicenseDTO = null;
        if (employee.getDriverLicense() != null) {
            driverLicenseDTO = DriverLicenseDTO.builder()
                    .hasLicense(employee.getDriverLicense().getHasLicense())
                    .licenseNumber(employee.getDriverLicense().getLicenseNumber())
                    .expirationDate(employee.getDriverLicense().getExpirationDate())
                    .build();
        }
        // 紧急联系人
        List<EmergencyContactRequest> contactDTOs = null;
        if (employee.getEmergencyContacts() != null) {
            contactDTOs = employee.getEmergencyContacts().stream().map(c -> EmergencyContactRequest.builder()
                    .id(c.getId())
                    .firstName(c.getFirstName())
                    .lastName(c.getLastName())
                    .relationship(c.getRelationship())
                    .email(c.getEmail())
                    .cellPhone(c.getCellPhone())
                    .build()
            ).collect(Collectors.toList());
        }

        // 组装响应
        GetEmployeeResponse response = GetEmployeeResponse.builder()
                .id(employee.getId())
                .userId(employee.getUserId())
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .email(employee.getEmail())
                .gender(employee.getGender())
                .dob(employee.getDob())
                .startDate(employee.getStartDate())
                .addresses(addressDTOs)
                .workAuthorization(workAuthDTO)
                .driverLicense(driverLicenseDTO)
                .emergencyContacts(contactDTOs)
                .applicationType(employee.getApplicationType())
                .build();

        return response;
    }


    @Override
    public Employee registerEmployee(CreateEmployeeRequest request) {
        // 校验邮箱是否存在
        if (employeeRepository.existsByEmail(request.getEmail())) {
            throw new EmployeeAlreadyExistsException("Employee already exists with email: " + request.getEmail());
        }

        // 地址转换
        List<Address> addressList = null;
        if (request.getAddresses() != null) {
            addressList = request.getAddresses().stream().map(addrReq -> Address.builder()
                    .id(addrReq.getId())
                    .type(addrReq.getType())
                    .addressLine1(addrReq.getAddressLine1())
                    .city(addrReq.getCity())
                    .state(addrReq.getState())
                    .zipCode(addrReq.getZipCode())
                    .build()
            ).collect(Collectors.toList());
        }

        // 紧急联系人转换
        List<EmergencyContact> contactList = null;
        if (request.getEmergencyContacts() != null) {
            contactList = request.getEmergencyContacts().stream().map(c -> EmergencyContact.builder()
                    .id(c.getId())
                    .firstName(c.getFirstName())
                    .lastName(c.getLastName())
                    .relationship(c.getRelationship())
                    .cellPhone(c.getCellPhone())
                    .build()
            ).collect(Collectors.toList());
        }

        // 工作授权转换
        WorkAuthorization workAuth = null;
        if (request.getWorkAuthorization() != null) {
            workAuth = WorkAuthorization.builder()
                    .isUsCitizen(request.getWorkAuthorization().getIsUsCitizen())
                    .greenCardHolder(request.getWorkAuthorization().getGreenCardHolder())
                    .type(request.getWorkAuthorization().getType())
                    .build();
        }
        // 驾照转换
        DriverLicense driverLicense = null;
        if (request.getDriverLicense() != null) {
            driverLicense = DriverLicense.builder()
                    .hasLicense(request.getDriverLicense().getHasLicense())
                    .licenseNumber(request.getDriverLicense().getLicenseNumber())
                    .expirationDate(request.getDriverLicense().getExpirationDate())
                    .build();
        }

        // 创建 Employee 对象
        Employee employee = Employee.builder()
                .userId(request.getUserId())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .gender(request.getGender())
                .dob(request.getDob())
                .startDate(request.getStartDate())
                .addresses(addressList)
                .workAuthorization(workAuth)
                .driverLicense(driverLicense)
                .emergencyContacts(contactList)
                .applicationType(request.getApplicationType())
                .build();

        return employeeRepository.save(employee);
    }

    @Override
    public Employee updateEmployee(String id, UpdateEmployeeRequest request) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));

        // 更新基本信息
        if (request.getFirstName() != null) {
            employee.setFirstName(request.getFirstName());
        }
        if (request.getLastName() != null) {
            employee.setLastName(request.getLastName());
        }
        if (request.getEmail() != null) {
            employee.setEmail(request.getEmail());
        }
        if (request.getGender() != null) {
            employee.setGender(request.getGender());
        }
        if (request.getDob() != null) {
            employee.setDob(request.getDob());
        }
        if (request.getStartDate() != null) {
            employee.setStartDate(request.getStartDate());
        }
        if (request.getApplicationType() != null) {
            employee.setApplicationType(request.getApplicationType());
        }

        // 更新地址
        if (request.getAddresses() != null) {
            List<Address> addressList = request.getAddresses().stream().map(addrReq -> Address.builder()
                    .id(addrReq.getId())
                    .type(addrReq.getType())
                    .addressLine1(addrReq.getAddressLine1())
                    .city(addrReq.getCity())
                    .state(addrReq.getState())
                    .zipCode(addrReq.getZipCode())
                    .build()
            ).collect(Collectors.toList());
            employee.setAddresses(addressList);
        }

        // 更新工作授权
        if (request.getWorkAuthorization() != null) {
            WorkAuthorization workAuth = WorkAuthorization.builder()
                    .isUsCitizen(request.getWorkAuthorization().getIsUsCitizen())
                    .greenCardHolder(request.getWorkAuthorization().getGreenCardHolder())
                    .type(request.getWorkAuthorization().getType())
                    .startDate(request.getWorkAuthorization().getStartDate())
                    .endDate(request.getWorkAuthorization().getEndDate())
                    .lastModificationDate(request.getWorkAuthorization().getLastModificationDate())
                    .build();
            employee.setWorkAuthorization(workAuth);
        }

        // 更新驾驶证
        if (request.getDriverLicense() != null) {
            DriverLicense driverLicense = DriverLicense.builder()
                    .hasLicense(request.getDriverLicense().getHasLicense())
                    .licenseNumber(request.getDriverLicense().getLicenseNumber())
                    .expirationDate(request.getDriverLicense().getExpirationDate())
                    .build();
            employee.setDriverLicense(driverLicense);
        }

        // 更新紧急联系人
        if (request.getEmergencyContacts() != null) {
            List<EmergencyContact> contactList = request.getEmergencyContacts().stream().map(c -> EmergencyContact.builder()
                    .id(c.getId())
                    .firstName(c.getFirstName())
                    .lastName(c.getLastName())
                    .relationship(c.getRelationship())
                    .cellPhone(c.getCellPhone())
                    .build()
            ).collect(Collectors.toList());
            employee.setEmergencyContacts(contactList);
        }

        return employeeRepository.save(employee);
    }


    @Override
    public void uploadDocument(String employeeId, UploadDocumentRequest request) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id: " + employeeId));

        // 先检查 personalDocuments 是否为 null
        if (employee.getPersonalDocuments() == null) {
            employee.setPersonalDocuments(new ArrayList<>());
        }
        List<PersonalDocument> documents = employee.getPersonalDocuments();
        boolean exists = documents.stream()
                .anyMatch(doc -> doc.getPath().equals(request.getPath()));
        if (exists) {
            throw new DocumentAlreadyExistsException("A document with the same path already exists. Please use update instead.");
        }
        PersonalDocument newDocument = new PersonalDocument();
        newDocument.setType(request.getType());
        newDocument.setTitle(request.getTitle());
        newDocument.setPath(request.getPath());
        newDocument.setComment(request.getComment());
        newDocument.setCreateDate(LocalDateTime.now());


        documents.add(newDocument);
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
        if (request.getType() != null) {
            doc.setType(request.getType());
        }
        if (request.getTitle() != null) {
            doc.setTitle(request.getTitle());
        }
        if (request.getComment() != null) {
            doc.setComment(request.getComment());
        }
        if (request.getPath() != null) {
            doc.setPath(request.getPath());
        }
        if (request.getCreateDate() != null) {
            doc.setCreateDate(request.getCreateDate());
        }
        employeeRepository.save(employee);
    }

    @Override
    public List<GetEmployeeResponse> searchEmployeesByName(String name) {
        List<Employee> employees = employeeRepository
                .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(name, name);

        List<GetEmployeeResponse> responses = new ArrayList<>();

        for (Employee e : employees) {
            DriverLicenseDTO driverLicenseDTO = null;
            if (e.getDriverLicense() != null) {
                driverLicenseDTO = DriverLicenseDTO.builder()
                        .hasLicense(e.getDriverLicense().getHasLicense())
                        .licenseNumber(e.getDriverLicense().getLicenseNumber())
                        .expirationDate(e.getDriverLicense().getExpirationDate())
                        .build();
            }

            // 构建地址
            List<AddressRequest> addressDTOs = null;
            if (e.getAddresses() != null) {
                addressDTOs = e.getAddresses().stream().map(addr -> AddressRequest.builder()
                        .id(addr.getId())
                        .type(addr.getType())
                        .addressLine1(addr.getAddressLine1())
                        .city(addr.getCity())
                        .state(addr.getState())
                        .zipCode(addr.getZipCode())
                        .build()
                ).collect(Collectors.toList());
            }

            // 构建紧急联系人
            List<EmergencyContactRequest> contactDTOs = null;
            if (e.getEmergencyContacts() != null) {
                contactDTOs = e.getEmergencyContacts().stream().map(c -> EmergencyContactRequest.builder()
                        .id(c.getId())
                        .firstName(c.getFirstName())
                        .lastName(c.getLastName())
                        .relationship(c.getRelationship())
                        .cellPhone(c.getCellPhone())
                        .build()
                ).collect(Collectors.toList());
            }

            // 构建工作授权
            WorkAuthorizationRequest workAuthDTO = null;
            if (e.getWorkAuthorization() != null) {
                workAuthDTO = WorkAuthorizationRequest.builder()
                        .isUsCitizen(e.getWorkAuthorization().getIsUsCitizen())
                        .greenCardHolder(e.getWorkAuthorization().getGreenCardHolder())
                        .type(e.getWorkAuthorization().getType())
                        .startDate(e.getWorkAuthorization().getStartDate())
                        .endDate(e.getWorkAuthorization().getEndDate())
                        .lastModificationDate(e.getWorkAuthorization().getLastModificationDate())
                        .build();
            }

            // 构建响应对象
            GetEmployeeResponse response = GetEmployeeResponse.builder()
                    .id(e.getId())
                    .userId(e.getUserId())
                    .firstName(e.getFirstName())
                    .lastName(e.getLastName())
                    .email(e.getEmail())
                    .gender(e.getGender())
                    .dob(e.getDob())
                    .addresses(addressDTOs)
                    .workAuthorization(workAuthDTO)
                    .driverLicense(driverLicenseDTO)
                    .emergencyContacts(contactDTOs)
                    .applicationType(e.getApplicationType())
                    .build();

            responses.add(response);
        }

        return responses;
    }


    @Override
    public List<GetEmployeeResponse> getAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        List<GetEmployeeResponse> responses = new ArrayList<>();

        for (Employee e : employees) {
            // DriverLicenseDTO
            DriverLicenseDTO driverLicenseDTO = null;
            if (e.getDriverLicense() != null) {
                driverLicenseDTO = DriverLicenseDTO.builder()
                        .hasLicense(e.getDriverLicense().getHasLicense())
                        .licenseNumber(e.getDriverLicense().getLicenseNumber())
                        .expirationDate(e.getDriverLicense().getExpirationDate())
                        .build();
            }

            // 地址
            List<AddressRequest> addressDTOs = null;
            if (e.getAddresses() != null) {
                addressDTOs = e.getAddresses().stream().map(addr -> AddressRequest.builder()
                        .id(addr.getId())
                        .type(addr.getType())
                        .addressLine1(addr.getAddressLine1())
                        .city(addr.getCity())
                        .state(addr.getState())
                        .zipCode(addr.getZipCode())
                        .build()
                ).collect(Collectors.toList());
            }

            // 紧急联系人
            List<EmergencyContactRequest> contactDTOs = null;
            if (e.getEmergencyContacts() != null) {
                contactDTOs = e.getEmergencyContacts().stream().map(c -> EmergencyContactRequest.builder()
                        .id(c.getId())
                        .firstName(c.getFirstName())
                        .lastName(c.getLastName())
                        .relationship(c.getRelationship())
                        .cellPhone(c.getCellPhone())
                        .build()
                ).collect(Collectors.toList());
            }

            // 工作授权
            WorkAuthorizationRequest workAuthDTO = null;
            if (e.getWorkAuthorization() != null) {
                workAuthDTO = WorkAuthorizationRequest.builder()
                        .isUsCitizen(e.getWorkAuthorization().getIsUsCitizen())
                        .greenCardHolder(e.getWorkAuthorization().getGreenCardHolder())
                        .type(e.getWorkAuthorization().getType())
                        .startDate(e.getWorkAuthorization().getStartDate())
                        .endDate(e.getWorkAuthorization().getEndDate())
                        .lastModificationDate(e.getWorkAuthorization().getLastModificationDate())
                        .build();
            }

            // 构建响应对象
            GetEmployeeResponse response = GetEmployeeResponse.builder()
                    .id(e.getId())
                    .userId(e.getUserId())
                    .firstName(e.getFirstName())
                    .lastName(e.getLastName())
                    .email(e.getEmail())
                    .gender(e.getGender())
                    .dob(e.getDob())
                    .addresses(addressDTOs)
                    .workAuthorization(workAuthDTO)
                    .driverLicense(driverLicenseDTO)
                    .emergencyContacts(contactDTOs)
                    .applicationType(e.getApplicationType())
                    .build();

            responses.add(response);
        }
        return responses;
    }

    @Override
    public Page<GetEmployeeResponse> getAllEmployeesPaged(Pageable pageable) {
        Page<Employee> employeePage = employeeRepository.findAll(pageable);
        return employeePage.map(employee -> convertToResponse(employee));
    }

    @Override
    public Page<GetEmployeeResponse> searchEmployeesByNamePaged(String name, Pageable pageable) {
        Page<Employee> employeePage = employeeRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(name, name, pageable);
        return employeePage.map(employee -> convertToResponse(employee));
    }
    private GetEmployeeResponse convertToResponse(Employee employee) {
        return GetEmployeeResponse.builder()
                .id(employee.getId())
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .email(employee.getEmail())
                .dob(employee.getDob())
                .startDate(employee.getStartDate())
                .build();
    }

    @Override
    public Page<GetEmployeeByHouseResponse> getEmployeesByHouseIdPaged(String houseId, Pageable pageable) {
        // 查询带分页的 Employee
        Page<Employee> employeePage = employeeRepository.findByHouseId(houseId, pageable);

        Page<GetEmployeeByHouseResponse> responsePage = employeePage.map(employee -> {
            // 只取 firstName 当作 name
            String name = employee.getFirstName();

            String phone = null;
            if (employee.getEmergencyContacts() != null && !employee.getEmergencyContacts().isEmpty()) {
                phone = employee.getEmergencyContacts().get(0).getCellPhone();
            }

            return new GetEmployeeByHouseResponse(name, phone);
        });

        return responsePage;
    }
    @Override
    public GetEmployeeResponse getEmployeeProfileByUserId(String userId) {
        Employee employee = employeeRepository.findByUserId(userId)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found for userId: " + userId));

        // 地址
        List<AddressRequest> addressDTOs = null;
        if (employee.getAddresses() != null) {
            addressDTOs = employee.getAddresses().stream().map(addr -> AddressRequest.builder()
                    .id(addr.getId())
                    .type(addr.getType())
                    .addressLine1(addr.getAddressLine1())
                    .city(addr.getCity())
                    .state(addr.getState())
                    .zipCode(addr.getZipCode())
                    .build()
            ).collect(Collectors.toList());
        }

        // 工作授权
        WorkAuthorizationRequest workAuthDTO = null;
        if (employee.getWorkAuthorization() != null) {
            WorkAuthorization wa = employee.getWorkAuthorization();
            WorkAuthorizationRequest.WorkAuthorizationRequestBuilder builder = WorkAuthorizationRequest.builder()
                    .isUsCitizen(wa.getIsUsCitizen())
                    .greenCardHolder(wa.getGreenCardHolder())
                    .type(wa.getType())
                    .startDate(wa.getStartDate())
                    .endDate(wa.getEndDate())
                    .lastModificationDate(wa.getLastModificationDate());
            workAuthDTO = builder.build();
        }

        // 驾照
        DriverLicenseDTO driverLicenseDTO = null;
        if (employee.getDriverLicense() != null) {
            driverLicenseDTO = DriverLicenseDTO.builder()
                    .hasLicense(employee.getDriverLicense().getHasLicense())
                    .licenseNumber(employee.getDriverLicense().getLicenseNumber())
                    .expirationDate(employee.getDriverLicense().getExpirationDate())
                    .build();
        }

        // 紧急联系人
        List<EmergencyContactRequest> contactDTOs = null;
        if (employee.getEmergencyContacts() != null) {
            contactDTOs = employee.getEmergencyContacts().stream().map(c -> EmergencyContactRequest.builder()
                    .id(c.getId())
                    .firstName(c.getFirstName())
                    .lastName(c.getLastName())
                    .relationship(c.getRelationship())
                    .email(c.getEmail())
                    .cellPhone(c.getCellPhone())
                    .build()
            ).collect(Collectors.toList());
        }

        // 构建响应对象
        GetEmployeeResponse response = GetEmployeeResponse.builder()
                .id(employee.getId())
                .userId(employee.getUserId())
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .email(employee.getEmail())
                .gender(employee.getGender())
                .dob(employee.getDob())
                .startDate(employee.getStartDate())
                .addresses(addressDTOs)
                .workAuthorization(workAuthDTO)
                .driverLicense(driverLicenseDTO)
                .emergencyContacts(contactDTOs)
                .applicationType(employee.getApplicationType())
                .build();

        return response;
    }



}

