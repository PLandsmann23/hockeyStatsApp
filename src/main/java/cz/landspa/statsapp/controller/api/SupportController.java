package cz.landspa.statsapp.controller.api;

import cz.landspa.statsapp.model.DTO.SupportEmail;
import cz.landspa.statsapp.model.User;
import cz.landspa.statsapp.service.EmailService;
import cz.landspa.statsapp.service.UserService;
import cz.landspa.statsapp.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/support")
public class SupportController {

    private final UserService userService;

    private final SecurityUtil securityUtil;
    private final EmailService emailService;

    public SupportController(UserService userService, SecurityUtil securityUtil, EmailService emailService) {
        this.userService = userService;
        this.securityUtil = securityUtil;
        this.emailService = emailService;
    }

    @PostMapping("/send")
    public ResponseEntity<?> sendSupportEmail(@RequestBody SupportEmail supportEmail){
        try {
            User user = userService.getUserByUsername(securityUtil.getCurrentUsername());
            supportEmail.setEmail(user.getEmail());

            emailService.sendEmail(supportEmail.getEmail(), "support@patriklandsmann.cz", supportEmail.getSubject(), supportEmail.getMessage());

            return new ResponseEntity<>(HttpStatus.OK);

        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
}
