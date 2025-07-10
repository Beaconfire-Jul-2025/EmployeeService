package org.beaconfire.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.beaconfire.dto.CreateEmployeeRequest;
import org.beaconfire.dto.UpdateEmployeeRequest;
import org.beaconfire.dto.UploadDocumentRequest;
import org.beaconfire.exception.EmployeeAlreadyExistsException;
import org.beaconfire.exception.EmployeeNotFoundException;
import org.beaconfire.model.Employee;
import org.beaconfire.model.PersonalDocument;
import org.beaconfire.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
//    private final AmazonS3 amazonS3;

//    @Value("${aws.s3.bucketName}")
//    private String bucketName;

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
    public Employee getEmployeeById(String id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));
    }

    @Override
    public List<PersonalDocument> getEmployeeDocuments(String id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));

        return employee.getPersonalDocuments();
    }
    @Override
    public Optional<Employee> getEmployeeByEmail(String email) {
        return employeeRepository.findByEmail(email);
    }

    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
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
        employee.setAlternatePhone(request.getAlternatePhone());
        employee.setGender(request.getGender());
        employee.setSsn(request.getSsn());
        employee.setDob(request.getDob());
        employee.setStartDate(request.getStartDate());
        employee.setEndDate(request.getEndDate());
        employee.setWorkAuthType(request.getWorkAuthType());
        employee.setWorkAuthStartDate(request.getWorkAuthStartDate());
        employee.setWorkAuthEndDate(request.getWorkAuthEndDate());
        employee.setDriverLicense(request.getDriverLicense());
        employee.setDriverLicenseExpiration(request.getDriverLicenseExpiration());
        employee.setLastModificationDate(LocalDateTime.now());

        return employeeRepository.save(employee);
    }


    @Override
    public void deleteEmployee(String id) {
        employeeRepository.deleteById(id);
    }
    //sudo path for test the function ,wait for AWS key
    @Override
    public void addDocumentToEmployee(String id, UploadDocumentRequest request) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));

        if (employee.getPersonalDocuments() == null) {
            employee.setPersonalDocuments(new ArrayList<>());
        }

        PersonalDocument document = PersonalDocument.builder()
                .title(request.getTitle())
                .path(request.getPath()) // test
                .comment(request.getComment())
                .createDate(LocalDateTime.now())
                .build();

        employee.getPersonalDocuments().add(document);
        employee.setLastModificationDate(LocalDateTime.now());

        employeeRepository.save(employee);
    }


//    @Override
//    public String uploadDocument(MultipartFile file, String employeeId, String docTitle) {
//        Optional<Employee> optionalEmployee = employeeRepository.findById(employeeId);
//        if (!optionalEmployee.isPresent()) {
//            throw new RuntimeException("Employee not found");
//        }
//        Employee employee = optionalEmployee.get();
//        String key = "employee/" + employeeId + "/" + file.getOriginalFilename();
//
//        try {
//            ObjectMetadata metadata = new ObjectMetadata();
//            metadata.setContentLength(file.getSize());
//            amazonS3.putObject(new PutObjectRequest(bucketName, key, file.getInputStream(), metadata));
//
//            String fileUrl = amazonS3.getUrl(bucketName, key).toString();
//
//            // 更新个人文件信息
//            PersonalDocument document = new PersonalDocument();
//            document.setTitle(docTitle);
//            document.setPath(fileUrl);
//            document.setCreateDate(LocalDateTime.now());
//            document.setComment(""); // 需要可再根据需求添加评论
//
//            if (employee.getPersonalDocuments() == null) {
//                employee.setPersonalDocuments(new ArrayList<>());
//            }
//            employee.getPersonalDocuments().add(document);
//
//            employee.setLastModificationDate(LocalDateTime.now());
//            employeeRepository.save(employee);
//
//            return fileUrl;
//        } catch (IOException e) {
//            log.error("Error uploading file to S3", e);
//            throw new RuntimeException("Failed to upload file");
//        }
//    }
}
