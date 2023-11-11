package com.centurionuniversity.routecutm.ServiceLayer;

import com.centurionuniversity.routecutm.BusInfo;
import com.centurionuniversity.routecutm.DriverInfo;
import com.centurionuniversity.routecutm.UserInfo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


public interface UserService {
    Optional<UserInfo> getUserInfo(Long id);
    String registerUser(UserInfo userInfo);
    //String loginUser(UserInfo userInfo);
    Object getBusDetailsByUserId(Long userId);
    Object getdriverDetailsByUserId(Long userId);
    String updateUserPassword(Long id, String oldPass, String newPass);

}
