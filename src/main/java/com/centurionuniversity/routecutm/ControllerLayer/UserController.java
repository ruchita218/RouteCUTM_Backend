package com.centurionuniversity.routecutm.ControllerLayer;

import com.centurionuniversity.routecutm.BusInfo;
import com.centurionuniversity.routecutm.DriverInfo;
import com.centurionuniversity.routecutm.ServiceLayer.UserService;
import com.centurionuniversity.routecutm.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.centurionuniversity.routecutm.RqstRes.PasswordUpdateRequest;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/users")
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/getUser/{id}")
    public ResponseEntity<UserInfo> getDriver(@PathVariable Long id) {
        Optional<UserInfo> driverOptional = userService.getUserInfo(id);
        if (driverOptional.isPresent()) {
            return new ResponseEntity<>(driverOptional.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/registerUser")
    public ResponseEntity<String> registerUser(@RequestBody UserInfo userInfo) {
        String registrationStatus = userService.registerUser(userInfo);
        if (registrationStatus.equals("Success")) {
            return new ResponseEntity<>("Registered successfully. " +"His/her Credentials are "+"Name= "+ userInfo.getName() + ", " +"Email= "+ userInfo.getEmail() + ", " +"Location= "+ userInfo.getLocation()+" Registration No= "+userInfo.getRegistrationNo()+" Bus Pass No= "+userInfo.getBusPassNo(), HttpStatus.CREATED);
        }else {
            return new ResponseEntity<>(registrationStatus, HttpStatus.BAD_REQUEST);
        }
    }

//    @PostMapping("/login")
//    public ResponseEntity<String> loginUser(@RequestBody UserInfo userInfo) {
//        String loginStatus = userService.loginUser(userInfo);
//        if (loginStatus.equals("Logged in successfully")) {
//            return new ResponseEntity<>(loginStatus, HttpStatus.OK);
//        } else {
//            return new ResponseEntity<>(loginStatus, HttpStatus.BAD_REQUEST);
//        }
//    }

    @GetMapping("/busDetails/{userId}")
    public ResponseEntity<Object> getBusDetailsByUserId(@PathVariable Long userId) {
        Object result = userService.getBusDetailsByUserId(userId);

        if (result instanceof Map) {
            // Check if it's a message, then return it with the appropriate HTTP status
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            // Handle other cases if needed
            return new ResponseEntity<>("An error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/driverDetails/{userId}")
    public ResponseEntity<Object> getdriverDetailsByUserId(@PathVariable Long userId) {
        Object result = userService.getdriverDetailsByUserId(userId);

        if (result instanceof Map) {
            // Check if it's a message, then return it with the appropriate HTTP status
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            // Handle other cases if needed
            return new ResponseEntity<>("An error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<String> updateUserPassword(@PathVariable Long id, @RequestBody PasswordUpdateRequest passwordUpdateRequest) {
        String oldPass = passwordUpdateRequest.getOldPass();
        String newPass = passwordUpdateRequest.getNewPass();
        String updateStatus = userService.updateUserPassword(id, oldPass, newPass);
        if (updateStatus.equals("Password changed successfully.")) {
            return new ResponseEntity<>(updateStatus, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(updateStatus, HttpStatus.BAD_REQUEST);
        }
    }
    @PutMapping("/changeLocation/{id}")
    public ResponseEntity<Object> changeLocation(@PathVariable Long id, @RequestBody  Map<String, String> requestBody) {
        String newLocation=requestBody.get("newLocation");
        Object result = userService.changeLocation(id, newLocation);
        //return new ResponseEntity<>(result, HttpStatus.OK);
        if(result instanceof String){
            return new ResponseEntity<>(result,HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(result,HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/findBuses")
    public ResponseEntity<List<BusInfo>> getAllBuses() {
        List<BusInfo> buses = userService.getAllBuses();
        if (!buses.isEmpty()) {
            return new ResponseEntity<>(buses, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @PostMapping("/checkAttendance")
    public ResponseEntity<Object> checkAttendance(@RequestBody Map<String, String> requestBody) {
        String userEmail = requestBody.get("userEmail");
        String date = requestBody.get("date");
        Object result= userService.checkAttendance(userEmail,date);
        if(result instanceof String){
            return new ResponseEntity<>(result,HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(result,HttpStatus.OK);
        }
    }
}
