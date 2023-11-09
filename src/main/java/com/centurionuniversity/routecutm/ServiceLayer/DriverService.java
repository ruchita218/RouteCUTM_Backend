package com.centurionuniversity.routecutm.ServiceLayer;

import com.centurionuniversity.routecutm.AdminInfo;
import com.centurionuniversity.routecutm.DriverInfo;
import com.centurionuniversity.routecutm.UserInfo;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface DriverService {
    String loginDriver(DriverInfo driverInfo);
    String updateDriverPassword(Long id, String oldPass, String newPass);
    //List<UserInfo> getUsersByDriverId(Long driverId);
    Object getUsersByDriverId(Long id);
    Object getRegisteredUsersInDriverBus(Long driverId);
}
