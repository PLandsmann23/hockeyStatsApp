package cz.landspa.statsapp.controller.api;

import cz.landspa.statsapp.model.DTO.user.ChangePassword;
import cz.landspa.statsapp.model.User;
import cz.landspa.statsapp.model.UserSetting;
import cz.landspa.statsapp.model.VerificationToken;
import cz.landspa.statsapp.service.EmailService;
import cz.landspa.statsapp.service.UserService;
import cz.landspa.statsapp.service.UserSettingService;
import cz.landspa.statsapp.service.VerificationTokenService;
import cz.landspa.statsapp.util.SecurityUtil;
import cz.landspa.statsapp.util.Util;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/users")
public class UserController {


    private final UserService userService;

    private final SecurityUtil securityUtil;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenService verificationTokenService;
    private final EmailService emailService;
    private final UserSettingService userSettingService;

    public UserController(UserService userService, SecurityUtil securityUtil, PasswordEncoder passwordEncoder, VerificationTokenService verificationTokenService, EmailService emailService, UserSettingService userSettingService) {
        this.userService = userService;
        this.securityUtil = securityUtil;
        this.passwordEncoder = passwordEncoder;
        this.verificationTokenService = verificationTokenService;
        this.emailService = emailService;
        this.userSettingService = userSettingService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> newUser(@RequestBody @Valid User user, HttpServletRequest request) {
        try {

            User newUser = userService.saveUser(user);
            VerificationToken token = verificationTokenService.getTokenByUser(newUser);
            String url = request.getRequestURI() +"/activate?token="+token.getToken();
            emailService.sendVerificationEmail(newUser.getEmail(), Util.getUrl(request)+"/activate?token="+token.getToken());
            return new ResponseEntity<>(HttpStatus.OK);

        } catch (Exception e) {
            Map<String,String> message =new HashMap<>();

            if (userService.getUserByUsername(user.getUsername()) != null && userService.getUserByEmail(user.getEmail()) != null) {

                message.put("username","Uživatel s tímto uživatelským jménem a emailem již existuje");
                message.put("email","Uživatel s tímto uživatelským jménem a emailem již existuje");
            }

            else if (userService.getUserByUsername(user.getUsername()) != null) {
                message.put("username","Uživatel s tímto uživatelským jménem již existuje");
            }

            else if (userService.getUserByEmail(user.getEmail()) != null) {
                message.put("email","Uživatel s tímto emailem již existuje");

            }
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }



    }

    @PostMapping("/changepassword")
    public ResponseEntity<?> changePassword(@RequestBody ChangePassword changePassword){
        try{
            User user = userService.getUserByUsername(securityUtil.getCurrentUsername());

            if(passwordEncoder.matches(changePassword.getOldPassword(), user.getPassword())){
                user.setPassword(passwordEncoder.encode(changePassword.getNewPassword()));
                userService.updateUser(user, user.getId());
            } else {
                throw new RuntimeException("Staré heslo není správné");
            }

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/settings")
    public ResponseEntity<?> changeSettings(@RequestBody UserSetting userSetting){
        try{
            User user = userService.getUserByUsername(securityUtil.getCurrentUsername());
            userSetting.setUser(user);

            return new ResponseEntity<>(userSettingService.updateSetting(user.getId(), userSetting),HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
}
