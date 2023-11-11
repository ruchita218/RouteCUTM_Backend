package com.centurionuniversity.routecutm.ServiceLayer;

import com.centurionuniversity.routecutm.*;
import com.centurionuniversity.routecutm.RqstRes.LoginCredentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationServiceImpl implements AuthenticationService{
    private UserInfoRepository userInfoRepository;
    private BusInfoRepository busInfoRepository;
    private AdminInfoRepository adminInfoRepository;
    private DriverInfoRepository driverInfoRepository;
    private AttendanceInfoRepository attendanceInfoRepository;

    @Autowired
    public AuthenticationServiceImpl(UserInfoRepository userInfoRepository,BusInfoRepository busInfoRepository,AdminInfoRepository adminInfoRepository,DriverInfoRepository driverInfoRepository,AttendanceInfoRepository attendanceInfoRepository) {
        this.userInfoRepository = userInfoRepository;
        this.busInfoRepository = busInfoRepository;
        this.adminInfoRepository=adminInfoRepository;
        this.driverInfoRepository=driverInfoRepository;
        this.attendanceInfoRepository=attendanceInfoRepository;
    }
    @Override
    public Object login(LoginCredentials loginInfo) {
        if (loginInfo.getEmail() == "" || loginInfo.getPassword() == "") {
            return "Cannot log in. Please enter both email and password.";
        }

        Optional<AdminInfo> adminOptional = adminInfoRepository.findByEmail(loginInfo.getEmail());
        Optional<DriverInfo> driverOptional = driverInfoRepository.findByEmail(loginInfo.getEmail());
        Optional<UserInfo> userOptional = userInfoRepository.findByEmail(loginInfo.getEmail());
        if (adminOptional.isPresent()) {
            AdminInfo admin = adminOptional.get();
            if (admin.getPassword().equals(loginInfo.getPassword())) {
                // Successful login, return admin information
                //return "Logged in successfully: Your Email is : "+admin.getEmail();
                return admin;

            } else {
                return "Incorrect Email or password";
            }
        }
        else if (driverOptional.isPresent()) {
            DriverInfo driver = driverOptional.get();
            if (driver.getPassword().equals(loginInfo.getPassword())) {
                // Successful login, return admin information
                return driver;

            } else {
                return "Email or password is incorrect.";
            }
        }
        else if (userOptional.isPresent()) {
            UserInfo user = userOptional.get();
            if (user.getPassword().equals(loginInfo.getPassword())) {
                // Successful login, return user information
                return user;
            } else {
                return "Email or password is incorrect.";
            }
        } else {
            return "You are not registered. First register, then log in here.";
        }
    }
}
