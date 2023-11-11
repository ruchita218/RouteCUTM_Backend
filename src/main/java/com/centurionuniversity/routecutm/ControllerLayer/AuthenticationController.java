package com.centurionuniversity.routecutm.ControllerLayer;

import com.centurionuniversity.routecutm.AdminInfo;
import com.centurionuniversity.routecutm.DriverInfo;
import com.centurionuniversity.routecutm.RqstRes.LoginCredentials;
import com.centurionuniversity.routecutm.ServiceLayer.AdminService;
import com.centurionuniversity.routecutm.ServiceLayer.AuthenticationService;
import com.centurionuniversity.routecutm.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api")
public class AuthenticationController {
    private AuthenticationService authenticationService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }
    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginCredentials loginInfo) {
        Object loginStatus = authenticationService.login(loginInfo);
//        if (loginStatus instanceof AdminInfo) {
//            return new ResponseEntity<>(loginStatus, HttpStatus.OK);
//        }
//        else if (loginStatus instanceof UserInfo) {
//            return new ResponseEntity<>(loginStatus, HttpStatus.OK);
//        }
//        else if (loginStatus instanceof DriverInfo) {
//            return new ResponseEntity<>(loginStatus, HttpStatus.OK);
//        }
//        else if(loginStatus instanceof String){
//            return new ResponseEntity<>(loginStatus, HttpStatus.OK);
//        }
//        else {
//            return new ResponseEntity<>(loginStatus, HttpStatus.NOT_FOUND);
//        }

        if (loginStatus instanceof String) {
            return new ResponseEntity<>(loginStatus, HttpStatus.OK);
        }
        else if (loginStatus instanceof Object) {
            return new ResponseEntity<>(loginStatus, HttpStatus.OK);
       }
        else{
            return new ResponseEntity<>(loginStatus, HttpStatus.NOT_FOUND);
        }

    }
}
