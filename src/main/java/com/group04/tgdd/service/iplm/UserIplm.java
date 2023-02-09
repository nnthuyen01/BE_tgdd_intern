package com.group04.tgdd.service.iplm;


import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.group04.tgdd.dto.*;
import com.group04.tgdd.exception.AppException;
import com.group04.tgdd.exception.NotFoundException;
import com.group04.tgdd.model.Address;
import com.group04.tgdd.model.Users;
import com.group04.tgdd.model.VerificationToken;
import com.group04.tgdd.repository.AddressRepo;
import com.group04.tgdd.repository.UsersRepo;
import com.group04.tgdd.repository.VerificationRepo;
import com.group04.tgdd.service.Cloudinary.CloudinaryUpload;
import com.group04.tgdd.service.UserService;
import com.group04.tgdd.service.auth.UserDetailIplm;

import com.group04.tgdd.service.twilio.TwillioService;
import com.group04.tgdd.utils.JWTProvider;
import com.group04.tgdd.utils.Utils;
import com.group04.tgdd.utils.constant.RoleConstant;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.validator.GenericValidator;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class UserIplm implements UserService {
    private final PasswordEncoder passwordEncoder;
    private final UsersRepo userRepo;
    private final VerificationRepo verificationRepo;
    private final CloudinaryUpload cloudinaryUpload;
    private final AuthenticationManager authenticationManager;
    private final AddressRepo addressRepo;
    private final ModelMapper modelMapper;

    private final TwillioService twillioService;
    @Override
    public Users saveRegister(UserReq userReq) {
        if (!GenericValidator.isEmail(userReq.getEmail()))
            throw new AppException(400, "Wrong email");
        boolean check = userRepo.existsByEmail(userReq.getEmail()) || userRepo.existsByPhone(userReq.getPhone());
        if (check) {
            throw new AppException(400,"Email or phone already exits");
        }
        userReq.setPassword(passwordEncoder.encode(userReq.getPassword()));
        Users users = modelMapper.map(userReq,Users.class);

        users.setRole(RoleConstant.ROLE_USER);
        users.getAddresses().add(new Address(null,userReq.getAddress(),true,users));

        return userRepo.save(users);
    }
    @Override
    public Users saveRegister(UserReq userReq, String prefix) {


        if (!GenericValidator.isEmail(userReq.getEmail()))
            throw new AppException(400, "Wrong email");
        boolean check = userRepo.existsByEmail(userReq.getEmail()) || userRepo.existsByPhone(userReq.getPhone());
        if (check) {
            throw new AppException(400,"Email or phone already exits");
        }
        userReq.setPassword(passwordEncoder.encode(userReq.getPassword()));
        Users users = modelMapper.map(userReq,Users.class);

        users.setRole(RoleConstant.ROLE_USER);
        users.getAddresses().add(new Address(null,userReq.getAddress(),true,users));

        return userRepo.save(users);
    }
    @Override
    public Users saveAdmin(UserReq userReq) {
        boolean check = userRepo.existsByEmail(userReq.getEmail()) || userRepo.existsByPhone(userReq.getPhone());
        if (check) {
            throw new AppException(200, "Email or phone already exits");
        }
        Users users = this.modelMapper.map(userReq, Users.class);

        users.getAddresses().add(new Address(null,userReq.getAddress(),true,users));
        users.setEnable(true);
        users.setRole(RoleConstant.ROLE_ADMIN);
        users.setPassword(passwordEncoder.encode(userReq.getPassword()));
        return userRepo.save(users);
    }

    @Override
    public String validateVerificationToken(String token, String email) {

        VerificationToken verificationToken
                = verificationRepo.findVerificationTokenByTokenAndUser_Email(token, email);

        if (verificationToken == null) {
            return "invalid";
        }

        Users user = verificationToken.getUser();
        Calendar cal = Calendar.getInstance();

        if ((verificationToken.getExpirationTime().getTime()
                - cal.getTime().getTime()) <= 0) {
            verificationRepo.delete(verificationToken);
            return "expired";
        }
        verificationToken.setToken(null);
        verificationRepo.save(verificationToken);
        user.setEnable(true);
        user.setRole(RoleConstant.ROLE_USER);
        userRepo.save(user);

        return "valid";
    }

    @Override
    public void saveVerificationTokenForUser(Users users, String token) {
        VerificationToken verificationToken = new VerificationToken(users, token);
        System.out.println(verificationToken.getExpirationTime());
        verificationRepo.save(verificationToken);
    }


    //phan trang user
    @Override
    public List<UserResp> findAll(Pageable pageable) {
        List<UserResp> results = new ArrayList<>();
        List<Users> users = userRepo.findAll(pageable).getContent();

        for (Users item : users) {
            UserResp userResp = modelMapper.map(item, UserResp.class);


            results.add(userResp);

        }
        return results;
    }

    @Override
    public int totalUser() {
        return (int) userRepo.count();
    }

    @Override
    public Users findById(Long id) {
        Optional<Users> user = userRepo.findById(id);
        return user.orElse(null);
    }


    @Override
    public Users updateUser(UpdateUserReq userReq) {
        Users userUpdate = findById(Utils.getIdCurrentUser());
        if (userUpdate != null) {
            if (userReq.getName()!=null && !userReq.getName().trim().equals(""))
                userUpdate.setName(userReq.getName().trim().replaceAll("  "," "));
            if (userReq.getGender()!=null && !userReq.getGender().trim().equals(""))
                userUpdate.setGender(userReq.getGender().trim().replaceAll("  "," "));
            if (userReq.getPhone()!=null && !userReq.getPhone().trim().equals(""))
                userUpdate.setPhone(userReq.getPhone().trim().replaceAll("  "," "));
            if (userReq.getAddresses()!=null && userReq.getAddresses().size()>0) {
                boolean checkAddressDf = false;
                for (Address address: userReq.getAddresses()){
                    address.setUsers(userUpdate);
                    if (address.isIdDefault())
                        checkAddressDf=true;
                };
                if (checkAddressDf){
                    userUpdate.getAddresses().forEach(address -> {
                        if (address.isIdDefault())
                            address.setIdDefault(false);
                    });
                }
                userUpdate.setAddresses(userReq.getAddresses());

            }
            userRepo.save(userUpdate);
            userUpdate.getAddresses();
            return userUpdate;
        } else return null;
    }

    @Override
    public VerificationToken SendToken(String email) {
        VerificationToken verificationToken = verificationRepo.findVerificationTokenByUserEmail(email);
        if (verificationToken != null) {
            String token = RandomStringUtils.randomAlphanumeric(6).toUpperCase();
            verificationToken.setToken(token);
            verificationRepo.save(verificationToken);
            return verificationToken;
        }
        return null;
    }

    @Override
    public VerificationToken GetNewOTP(Long uid) {
        VerificationToken verificationToken = verificationRepo.findVerificationTokenByUserId(uid);
        String token = RandomStringUtils.randomAlphanumeric(6).toUpperCase();
        verificationToken.setToken(token);
        verificationRepo.save(verificationToken);
        return verificationToken;
    }

    @Override
    public String validateOTP(PhoneVerifyReq phoneVerifyReq) {
        // Find user
        Users verifyingUser = userRepo.findById(phoneVerifyReq.getUserID()).get();
        // Get user's phone number from database
        String verifyingPhoneNumber = verifyingUser.getPhone();
        // Get Token requested
        String token = phoneVerifyReq.getToken();

        if(verifyPhoneNumber(verifyingPhoneNumber)){

            String formattedPhoneNumber = formatPhoneNumber(verifyingPhoneNumber);
            VerificationToken verificationToken
                    = verificationRepo.findVerificationTokenByTokenAndUserPhone(token, formattedPhoneNumber);

            if (verificationToken == null) {
                return "invalid";
            }

            Users user = verificationToken.getUser();
            Calendar cal = Calendar.getInstance();

            if ((verificationToken.getExpirationTime().getTime()
                    - cal.getTime().getTime()) <= 0) {
                verificationRepo.delete(verificationToken);
                return "expired";
            }
            verificationToken.setToken(null);
            verificationRepo.save(verificationToken);
            user.setEnable(true);
            user.setRole(RoleConstant.ROLE_USER);
            userRepo.save(user);

            return "valid";
        }
        else{
            return "invalid";
        }

    }
    public boolean verifyPhoneNumber(String phone) {
        // Get Utility Instance
        PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
        Phonenumber.PhoneNumber numberProto = new Phonenumber.PhoneNumber();
        try {
            numberProto = phoneNumberUtil.parse(phone, "VN");
        } catch (NumberParseException e) {
            System.err.println("NumberParseException was thrown: " + e);
        }
        return phoneNumberUtil.isValidNumber(numberProto);
    }
    public String formatPhoneNumber(String phone){
        // Get Utility Instance
        PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
        Phonenumber.PhoneNumber numberProto = new Phonenumber.PhoneNumber();
        try {
            numberProto = phoneNumberUtil.parse(phone, "VN");
        } catch (NumberParseException e) {
            System.err.println("NumberParseException was thrown: " + e);
        }
        String formattedNum = phoneNumberUtil.format(numberProto, PhoneNumberUtil.PhoneNumberFormat.E164);
        return formattedNum;
    }
    @Override
    public String registerByPhone(UserReq userReq, String prefix) {
        // Get Utility Instance
        PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
        // +84 + 091913123213
        String originalNum = "+" + prefix + userReq.getPhone();
        // if phone number is valid
        if(phoneNumberUtil.isPossibleNumber(originalNum, prefix)){
            //Format the number
            Phonenumber.PhoneNumber numberProto = new Phonenumber.PhoneNumber();
            try {
                numberProto = phoneNumberUtil.parse(originalNum, "VN");
            } catch (NumberParseException e) {
                System.err.println("NumberParseException was thrown: " + e);
            }

            String formattedNum = phoneNumberUtil.format(numberProto, PhoneNumberUtil.PhoneNumberFormat.E164);
            // Save the user
            userReq.setPhone(formattedNum);
            Users users = saveRegister(userReq, prefix);
            //Send Token
            String token = RandomStringUtils.randomAlphanumeric(6).toUpperCase();
            saveVerificationTokenForUser(users,token);
            twillioService.sendSMS(formattedNum, token);

            return formattedNum;
        }
        else{
            return null;
        }

    }

    @Override
    public Users findUserByEmail(String email) {
        return userRepo.findUsersByEmail(email);
    }

    @Override
    public Users validatePasswordResetToken(String token, String email) {
        VerificationToken verificationToken
                = verificationRepo.findVerificationTokenByTokenAndUser_Email(token, email);

        if (verificationToken == null) {
            return null;
        }

        Calendar cal = Calendar.getInstance();

        if ((verificationToken.getExpirationTime().getTime()
                - cal.getTime().getTime()) <= 0) {
            verificationRepo.delete(verificationToken);
            return null;
        }
        Users users = verificationToken.getUser();
        verificationToken.setToken(null);
        verificationRepo.save(verificationToken);
        return users;
    }

    @Override
    public void changePassword(Users user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepo.save(user);
    }

    @Override
    public boolean checkIfValidOldPassword(Users user, String oldPassword) {
        return passwordEncoder.matches(oldPassword, user.getPassword());
    }

    @Override
    public String upAvartar(MultipartFile file) throws IOException {
        Long id = Utils.getIdCurrentUser();
        Users users = findById(id);
        if (users == null)
            throw new AppException(404, "User ID not found");
        String imgUrl = null;
        if (users.getAvatar() != null && users.getAvatar().startsWith("https://res.cloudinary.com/quangdangcloud/image/upload")) {
            imgUrl = users.getAvatar();
            imgUrl = cloudinaryUpload.uploadImage(file,imgUrl);
        }else
            imgUrl = cloudinaryUpload.uploadImage(file,null);
        users.setAvatar(imgUrl);
        userRepo.save(users);
        return imgUrl;
    }

    @Override
    public Map<String, String> login(LoginRequest loginRequest, HttpServletRequest request) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailIplm user = (UserDetailIplm) authentication.getPrincipal();

        String role = user.getUsers().getRole();

        String access_token = JWTProvider.createJWT(user.getUsers(), request);

        Map<String, String> token = new HashMap<>();
        token.put("access_token", access_token);
        token.put("userId", user.getUsers().getId().toString());
        token.put("email", user.getUsers().getEmail());
        token.put("name", user.getUsers().getName());
        token.put("avatar", user.getUsers().getAvatar());
        token.put("role", role);

        return token;

    }
    @Override
    public Map<String, String> login(phoneLoginRequest loginRequest, HttpServletRequest request) {

        String loginPhoneNumber = formatPhoneNumber(loginRequest.getPhone());
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginPhoneNumber,
                        loginRequest.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailIplm user = (UserDetailIplm) authentication.getPrincipal();

        String role = user.getUsers().getRole();

        String access_token = JWTProvider.createJWT(user.getUsers(), request);

        Map<String, String> token = new HashMap<>();
        token.put("access_token", access_token);
        token.put("userId", user.getUsers().getId().toString());
        token.put("email", user.getUsers().getEmail());
        token.put("name", user.getUsers().getName());
        token.put("avatar", user.getUsers().getAvatar());
        token.put("role", role);

        return token;

    }

    @Override
    public Users getCurrentUser() {
        Long id = Utils.getIdCurrentUser();
        Users users = userRepo.findById(id).orElseThrow(() -> new AppException(404, "Not found"));
        return users;
    }

    @Override
    public void disableUserById(Long usersId) {
        Users users = userRepo.findById(usersId).orElseThrow(() ->
                new AppException(404, String.format("UserId = %s not found", usersId)));
        users.setEnable(!users.getEnable());
        userRepo.save(users);
    }

    @Override
    public void deleteAddressUser(Long addressId) {
        try {
            addressRepo.deleteById(addressId);
        }
        catch (Exception e){
            throw new NotFoundException("Address ID: "+ addressId+" not exists");
        }
    }


}
