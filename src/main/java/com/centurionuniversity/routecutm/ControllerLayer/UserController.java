package com.centurionuniversity.routecutm.ControllerLayer;

import com.centurionuniversity.routecutm.BusInfo;
import com.centurionuniversity.routecutm.ServiceLayer.UserService;
import com.centurionuniversity.routecutm.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.centurionuniversity.routecutm.RqstRes.PasswordUpdateRequest;

import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/users")
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
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

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody UserInfo userInfo) {
        String loginStatus = userService.loginUser(userInfo);
        if (loginStatus.equals("Logged in successfully")) {
            return new ResponseEntity<>(loginStatus, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(loginStatus, HttpStatus.BAD_REQUEST);
        }
    }

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
}
