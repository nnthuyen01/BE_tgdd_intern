package com.group04.tgdd.service;


import com.group04.tgdd.dto.*;
import com.group04.tgdd.model.Users;
import com.group04.tgdd.model.VerificationToken;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface UserService {
    Users saveRegister(UserReq userReq);
    Users saveRegister(UserReq userReq, String prefix);
    Users saveAdmin(UserReq userReq);
    String validateVerificationToken(String token,String email);
    void saveVerificationTokenForUser(Users users, String token);
    Users findById(Long id);
    List<UserResp> findAll(Pageable pageable);
    int totalUser();
    Users updateUser(UpdateUserReq userReq);

    VerificationToken SendToken(String email);
    VerificationToken GetNewOTP(Long uid);
    Users findUserByEmail(String email);

    Users validatePasswordResetToken(String token,String email);

    void changePassword(Users user, String newPassword);

    boolean checkIfValidOldPassword(Users user, String oldPassword);

    String upAvartar(MultipartFile file) throws IOException;

    Map<String,String> login(LoginRequest loginRequest, HttpServletRequest request);
    Map<String,String> login(phoneLoginRequest loginRequest, HttpServletRequest request);
    Users getCurrentUser();

    void disableUserById(Long usersId);

    void deleteAddressUser(Long addressId);

    String registerByPhone(UserReq userReq, String prefix);

    String validateOTP(PhoneVerifyReq phoneVerifyReq);
}
