package org.beaconfire.employee.controller;

import org.beaconfire.employee.dto.TokenRequest;
import org.beaconfire.employee.dto.TokenResponse;
import org.beaconfire.employee.jwt.JwtTokenUtil;
import org.beaconfire.employee.service.EmailServiceClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/employees")
public class RegistrationTokenController {
    private final JwtTokenUtil jwtTokenUtil;
    private final EmailServiceClient emailServiceClient;

    public RegistrationTokenController(
            JwtTokenUtil jwtTokenUtil,
            EmailServiceClient emailServiceClient) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.emailServiceClient = emailServiceClient;
    }

    @PostMapping("/registration-token")
    public ResponseEntity<TokenResponse> generateToken(@RequestBody TokenRequest request) {
        String email = request.getEmail();
        // Generate JWT token
        String token = jwtTokenUtil.generateToken(email);
        // Generate registration link
        String registrationLink = "https://localhost:8080/register?token=" + token;
        // Call EmailService to send email
        emailServiceClient.sendRegistrationEmail(email, registrationLink);
        return ResponseEntity.ok(new TokenResponse(token, registrationLink));
    }
}
