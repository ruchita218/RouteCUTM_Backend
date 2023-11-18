package com.centurionuniversity.routecutm.ServiceLayer;

import com.centurionuniversity.routecutm.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    private UserInfoRepository userInfoRepository;
    private BusInfoRepository busInfoRepository;
    private DriverInfoRepository driverInfoRepository;
    private AttendanceInfoRepository attendanceInfoRepository;

    @Autowired
    public UserServiceImpl(UserInfoRepository userInfoRepository,BusInfoRepository busInfoRepository,DriverInfoRepository driverInfoRepository,AttendanceInfoRepository attendanceInfoRepository) {
        this.userInfoRepository = userInfoRepository;
        this.busInfoRepository = busInfoRepository;
        this.driverInfoRepository=driverInfoRepository;
        this.attendanceInfoRepository=attendanceInfoRepository;
    }

    @Override
    public Optional<UserInfo> getUserInfo(Long id) {
        return userInfoRepository.findById(id);
    }

    @Override
    public String registerUser(UserInfo userInfo) {
        if (userInfo.getName() == null || userInfo.getEmail() == null || userInfo.getPassword() == null || userInfo.getLocation() == null || userInfo.getRegistrationNo()==null) {
            return "Cannot registered. Please enter all fields i.e name,email,password,location,registrationNo";
        }

        if (userInfoRepository.existsByEmail(userInfo.getEmail())) {
            return "Cannot register again. User with this Email is already registered.";
        }

        if (userInfo.getPassword().length() < 4) {
            return "Password should be at least 4 characters.";
        }

        String truncatedBusPassNo = generateShortUUID(); // Generate shorter UUID
        userInfo.setBusPassNo(truncatedBusPassNo);

        Optional<BusInfo> busOptional = busInfoRepository.findByLocation(userInfo.getLocation());
        if(busOptional.isPresent()){
            BusInfo busInfo = busOptional.get();
            userInfo.setBusInfo(busInfo);

            Optional<DriverInfo> driverInfoOptional=driverInfoRepository.findByBusInfo(busInfo);  //checks if this bus is assigned to any driver
            if(driverInfoOptional.isPresent()){
                DriverInfo driverInfo = driverInfoOptional.get();
                userInfo.setDriverInfo(driverInfo);
            }

            Long registeredUsers = busInfo.getRegisteredUsers() + 1;
            busInfo.setRegisteredUsers(registeredUsers);
            busInfoRepository.save(busInfo);
        }
        else{
            return "Sry,you cannot be registered because bus for your location is not available in our campus";
        }

        UserInfo savedUserInfo = userInfoRepository.save(userInfo);

        if (savedUserInfo != null) {
            return "Success";
        } else {
            return "User registration failed.";
        }
    }

    private String generateShortUUID() {
        UUID uuid = UUID.randomUUID();
        String shortUUID = Long.toHexString(uuid.getMostSignificantBits()).toUpperCase().substring(0, 6);
        return  shortUUID;
    }

//    @Override
//    public String loginUser(UserInfo userInfo) {
//        if (userInfo.getEmail() == null || userInfo.getPassword() == null) {
//            return "Cannot log in. Please enter both email and password.";
//        }
//
//        Optional<UserInfo> userOptional = userInfoRepository.findByEmail(userInfo.getEmail());
//        if (userOptional.isPresent()) {
//            UserInfo user = userOptional.get();
//            if (user.getPassword().equals(userInfo.getPassword())) {
//                // Successful login, return user information
//                return "Logged in successfully";
//            } else {
//                return "Email or password is incorrect.";
//            }
//        } else {
//            return "User is not registered. First register, then log in here.";
//        }
//    }

    @Override
    public Object getBusDetailsByUserId(Long userId) {
        Optional<UserInfo> userOptional = userInfoRepository.findById(userId);

        if (!userOptional.isPresent()) {
            // Driver with this ID does not exist
            return new HashMap<String, String>() {{
                put("Error", "You are not registered.First register yourself");
            }};
        }

        UserInfo userInfo = userOptional.get();
        if (userInfo.getBusInfo() == null) {
            // Driver is not assigned a bus
            return new HashMap<String, String>() {{
                put("message", "Currently , You are not assigned a bus");
            }};
        }

        BusInfo busInfo = userInfo.getBusInfo();

        return new HashMap<String, Object>() {{
            put("response", busInfo);
        }};
    }

    @Override
    public Object getdriverDetailsByUserId(Long userId) {
        Optional<UserInfo> userOptional = userInfoRepository.findById(userId);

        if (!userOptional.isPresent()) {
            // Driver with this ID does not exist
            return new HashMap<String, String>() {{
                put("Error", "You are not registered.First register yourself");
            }};
        }

        UserInfo userInfo = userOptional.get();
        if (userInfo.getDriverInfo() == null) {
            // Driver is not assigned a bus
            return new HashMap<String, String>() {{
                put("message", "Currently , there is no driver assigned for your bus");
            }};
        }

        DriverInfo driverInfo = userInfo.getDriverInfo();

        return new HashMap<String, Object>() {{
            put("response", driverInfo);
        }};
    }

    @Override
    public String updateUserPassword(Long id, String oldPass, String newPass){
        if (oldPass == null || newPass == null) {
            return "Please enter both oldpass and newpass.";
        }
        Optional<UserInfo> userOptional = userInfoRepository.findById(id);
        if (userOptional.isPresent()) {
            UserInfo user = userOptional.get();
            if (user.getPassword().equals(oldPass)) {
                user.setPassword(newPass);
                userInfoRepository.save(user);
                return "Password changed successfully.";
            } else {
                return "Incorrect old password.";
            }
        } else {
            return "Invalid User ID.";
        }
    }
    @Override
    public Object changeLocation(Long id, String newLocation) {
        if (newLocation == null || newLocation.isEmpty()) {
            return "Please enter your new location";
        }

        Optional<UserInfo> userOptional = userInfoRepository.findById(id);
        if (!userOptional.isPresent()) {
            return "Sorry, this ID is not present";
        }
        System.out.println(id);
        System.out.println(newLocation);

        UserInfo userInfo = userOptional.get();

        // Set busInfo and driverInfo to null
        userInfo.setBusInfo(null);
        userInfo.setDriverInfo(null);

        String oldLocation= userInfo.getLocation();
        Optional<BusInfo> busInfoOptional1=busInfoRepository.findByLocation(oldLocation);
        if(busInfoOptional1.isPresent()){
            BusInfo oldBusInfo=busInfoOptional1.get();
            Long registeredUsers=oldBusInfo.getRegisteredUsers()-1;
            oldBusInfo.setRegisteredUsers(registeredUsers);
            busInfoRepository.save(oldBusInfo);
        }


        // Retrieve data from busInfo table
        Optional<BusInfo> busInfoOptional = busInfoRepository.findByLocation(newLocation);

        if (!busInfoOptional.isPresent()) {
            return "Sorry, you cannot change your location because bus for your location is not available in our campus";
        }

        BusInfo newBusInfo = busInfoOptional.get();
//        System.out.println("Driver Email: " + newBusInfo.getDriverEmail());
//        System.out.println("Bus No: " + newBusInfo.getBusNo());
        Long registeredUsers = newBusInfo.getRegisteredUsers() + 1;
        newBusInfo.setRegisteredUsers(registeredUsers);
        busInfoRepository.save(newBusInfo);

        // Set location in userInfo table
        userInfo.setLocation(newLocation);

        // Set busInfo in userInfo table
        userInfo.setBusInfo(newBusInfo);

        // Check if driver email exists in driverInfo table
        if (newBusInfo.getDriverEmail() != null) {

            Optional<DriverInfo> driverInfoOptional = driverInfoRepository.findByEmail(newBusInfo.getDriverEmail());
            if (driverInfoOptional.isPresent()) {
                DriverInfo driverInfo = driverInfoOptional.get();
                userInfo.setDriverInfo(driverInfo);
            }
        }

        userInfoRepository.save(userInfo);

        return "Location changed successfully.";
    }
    @Override
    public List<BusInfo> getAllBuses() {
        return busInfoRepository.findAll();
    }

    @Override
    public Object checkAttendance(String userEmail, String date) {
        java.sql.Date sqlDate = java.sql.Date.valueOf(date);
        if (userEmail=="" || date=="" || userEmail == null || date == null) {
            return "Please fill all the fields";
        }
        AttendanceInfo attendanceInfo = attendanceInfoRepository.findByUserEmailAndDate(userEmail, sqlDate);
        if (attendanceInfo != null){
            return attendanceInfo;
        }
        else{
            return "No attendance is there for this date";
        }
//        return "Email id is invalid";
    }
}
