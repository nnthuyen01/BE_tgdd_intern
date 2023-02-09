package com.group04.tgdd.controller;

import com.group04.tgdd.dto.*;
import com.group04.tgdd.model.Users;
import com.group04.tgdd.model.VerificationToken;
import com.group04.tgdd.service.UserService;
import com.group04.tgdd.service.email.EmaiType;
import com.group04.tgdd.service.email.EmailSenderService;

import freemarker.template.TemplateException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@SecurityRequirement(name = "AUTHORIZATION")
public class SignUpController {
    private final UserService userService;
    private final EmailSenderService emailSenderService;

    final String TITLE_SUBJECT_EMAIL = "TDGG Register TOKEN";
    final String RESET_PASSWORD_TOKEN = "Reset Password Token";

    @PostMapping("/register-email")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserReq userReq) throws MessagingException, TemplateException, IOException {
        Users users = userService.saveRegister(userReq);

        String token = RandomStringUtils.randomAlphanumeric(6).toUpperCase();
        userService.saveVerificationTokenForUser(users,token);

        Map<String,Object> model = new HashMap<>();
        model.put("token",token);
        model.put("title", TITLE_SUBJECT_EMAIL);
        model.put("subject", TITLE_SUBJECT_EMAIL);
        emailSenderService.sendEmail(userReq.getEmail(), model, EmaiType.REGISTER);

        return ResponseEntity.ok(new ResponseDTO(true,"Sending email",
                null));
    }

    @Operation(summary = "Register by phone number sending OTP")
    @GetMapping("/register-phone")
    public ResponseEntity<?> registerByPhone(@Valid @RequestBody UserReq userReq, @RequestParam(defaultValue = ("+84")) String prefix){
        return ResponseEntity.ok(new ResponseDTO(true,"Sending OTP",
                userService.registerByPhone(userReq, prefix)));
    }

    @Operation(summary = "Verify authentication by phone SMS OTP")
    @RequestMapping(value = "/verifyRegistration-phone", method = RequestMethod.GET)
    public ResponseEntity<?> verifyRegistrationWithPhone(@RequestBody PhoneVerifyReq phoneVerifyReq) {
        String result = userService.validateOTP(phoneVerifyReq);
        if(!result.equals("valid")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok("validated");
    }
    @Operation(summary = "Resend OTP")
    @GetMapping("/resendOTP")
    public ResponseEntity<?> resendOTP(@RequestParam("uid") Long uid) {
        VerificationToken verificationToken
                = userService.GetNewOTP(uid);

        return ResponseEntity.ok(new ResponseDTO(true,"Resending OTP",
                null));
    }

    @RequestMapping(value = "/verifyRegistration", method = RequestMethod.GET)
    public ResponseEntity<?> verifyRegistration(@RequestParam("token") String token,
                                                @RequestParam("email") String email) {
        String result = userService.validateVerificationToken(token,email);
        if(!result.equals("valid")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok("");
    }

    @PostMapping("/registerTest")
    public ResponseEntity<?> registerUserTest(@RequestBody UserReq userReq) throws MessagingException {
        Users users = userService.saveAdmin(userReq);

        return ResponseEntity.ok(new ResponseDTO(true,"Success",
                null));
    }

    @GetMapping("/resendVerifyToken")
    public ResponseEntity<?> resendVerificationToken(@RequestParam("email") String email
                                                     ) throws MessagingException, TemplateException, IOException {
        VerificationToken verificationToken
                = userService.SendToken(email);
        Map<String,Object> model = new HashMap<>();
        model.put("token",verificationToken.getToken());
        model.put("title",TITLE_SUBJECT_EMAIL);
        model.put("subject", TITLE_SUBJECT_EMAIL);
        emailSenderService.sendEmail(email, model,EmaiType.REGISTER);

        return ResponseEntity.ok(new ResponseDTO(true,"Verification TOKEN Sent",
                null));
    }
    @PostMapping("/resetPassword")
    public ResponseEntity<?> resetPassword(@RequestParam String email, HttpServletRequest request) throws MessagingException, TemplateException, IOException {
        Users user = userService.findUserByEmail(email);
        if (user!= null && user.getEnable()){
            String token = "";
            token = userService.SendToken(email).getToken();
            Map<String,Object> model = new HashMap<>();
            model.put("token",token);

            model.put("title", RESET_PASSWORD_TOKEN);
            model.put("subject", RESET_PASSWORD_TOKEN);
            //Send email
            emailSenderService.sendEmail(user.getEmail(), model,EmaiType.REGISTER);
            log.info("Reset password: {}",
                    token);
            return ResponseEntity.ok(new ResponseDTO(true,"Sent email reset token",
                    null));
        }
        return ResponseEntity.badRequest().body(new ResponseDTO(false,"Not found email",
                null));
    }

    @PostMapping("/savePassword")
    public ResponseEntity<?> savePassword(@Valid @RequestBody PasswordDTO passwordDTO) {
        Users result = userService.validatePasswordResetToken(passwordDTO.getToken(), passwordDTO.getEmail());
        if(result == null) {

            return ResponseEntity.badRequest().body(new ResponseDTO(false,"Invalid token",
                    null));
        }
        if (!result.getEnable()){
            return ResponseEntity.ok().body(new ResponseDTO(false,"Email not verify",
                    null));
        }
        userService.changePassword(result, passwordDTO.getNewPassword());
        return ResponseEntity.ok().body(new ResponseDTO(true,"Change password successfully",
                null));
    }

    @PutMapping("/changePassword")
    public ResponseEntity<?> changePassword(@Valid @RequestBody PasswordDTO passwordDTO){
        Users user = userService.findUserByEmail(passwordDTO.getEmail());
        if(!userService.checkIfValidOldPassword(user,passwordDTO.getOldPassword())) {
            return ResponseEntity.ok().body(new ResponseDTO(false,"Invalid Old Password",
                    null));
        }
        //Save New Password
        userService.changePassword(user, passwordDTO.getNewPassword());
        return ResponseEntity.ok().body(new ResponseDTO(true,"Password Changed Successfully",
                null));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest, HttpServletRequest request){

        return ResponseEntity.ok(
                new ResponseDTO(true,"Success",userService.login(loginRequest,request))
        );
    }

    @PostMapping("/phoneLogin")
    public ResponseEntity<?> loginWithPhone(@RequestBody phoneLoginRequest loginRequest, HttpServletRequest request){
        return ResponseEntity.ok(
                new ResponseDTO(true,"Success",userService.login(loginRequest,request))
        );
    }
}
