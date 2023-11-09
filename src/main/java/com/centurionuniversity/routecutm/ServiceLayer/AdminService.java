package com.centurionuniversity.routecutm.ServiceLayer;

import com.centurionuniversity.routecutm.*;

import java.util.List;
import java.util.Optional;

public interface AdminService {

    Optional<AdminInfo> getAdminInfo();
    Object loginAdmin(AdminInfo adminInfo);
    String updateAdminPassword(Long id, String oldPass, String newPass);
    List<UserInfo> findAllUsers();
    List<DriverInfo> getAllDrivers();
    List<BusInfo> getAllBuses();
    //List<AttendanceInfo> getAttendanceDetails(String date, String busNo);
    Object getAttendanceDetails(String date, String busNo);
    Object addUser(UserInfo userInfo);
    String addDriver(DriverInfo driverInfo);
    String addBus(BusInfo busInfo);
    String deleteUserById(Long userId);
    String deleteDriverById(Long driverId);
    String updateBusById(Long id,String email);
    String deleteDriverNameById(Long busId);
    String getUser(Long userId);




    String deleteBusByBusId(Long busId);
}
