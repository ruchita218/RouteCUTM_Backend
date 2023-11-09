package com.centurionuniversity.routecutm.ControllerLayer;


import com.centurionuniversity.routecutm.AdminInfo;
import com.centurionuniversity.routecutm.DriverInfo;
import com.centurionuniversity.routecutm.RqstRes.PasswordUpdateRequest;
import com.centurionuniversity.routecutm.ServiceLayer.AdminService;
import com.centurionuniversity.routecutm.ServiceLayer.DriverService;
import com.centurionuniversity.routecutm.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/driver")
public class DriverController {
    private DriverService driverService;

    @Autowired
    public DriverController(DriverService driverService) {
        this.driverService = driverService;
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginDriver(@RequestBody DriverInfo driverInfo) {
        String loginStatus = driverService.loginDriver(driverInfo);
        if (loginStatus.equals("Logged in successfully")) {
            return new ResponseEntity<>(loginStatus, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(loginStatus, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<String> updateDriverPassword(@PathVariable Long id, @RequestBody PasswordUpdateRequest passwordUpdateRequest) {
        String oldPass = passwordUpdateRequest.getOldPass();
        String newPass = passwordUpdateRequest.getNewPass();
        String updateStatus = driverService.updateDriverPassword(id, oldPass, newPass);
        if (updateStatus.equals("Password changed successfully.")) {
            return new ResponseEntity<>(updateStatus, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(updateStatus, HttpStatus.BAD_REQUEST);
        }
    }

    //get all the users in the driverbus
//    @GetMapping("/getUsersByDriverId/{driverId}")
//    public ResponseEntity<List<UserInfo>> getUsersByDriverId(@PathVariable Long driverId) {
//        List<UserInfo> users = driverService.getUsersByDriverId(driverId);
//        if (users != null) {
//            return new ResponseEntity<>(users, HttpStatus.OK);
//        } else {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//    }

    @GetMapping("/getUsersByDriver/{id}")
    public ResponseEntity<Object> getUsersByDriverId(@PathVariable Long id) {
        Object result = driverService.getUsersByDriverId(id);

        if (result instanceof List) {
            // Users found, return the list
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else if (result instanceof Map) {
            // Check if it's a message, then return it with the appropriate HTTP status
            return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
        }

        // Handle other cases if needed
        return new ResponseEntity<>("An error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/getRegisteredUsersInDriverBus/{driverId}")
    public ResponseEntity<Object> getRegisteredUsersInDriverBus(@PathVariable Long driverId) {
        Object result = driverService.getRegisteredUsersInDriverBus(driverId);
        if (result instanceof Map) {
            // Check if it's a message, then return it with the appropriate HTTP status
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            // Handle other cases if needed
            return new ResponseEntity<>("An error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
