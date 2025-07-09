package org.beaconfire.employee.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "email-service",
        url = "${email-service.url}"
)
public interface EmailServiceClient  {
    @PostMapping("/emails/registration")
    void sendRegistrationEmail(
            @RequestParam String email,
            @RequestParam String registrationLink
    );
}
