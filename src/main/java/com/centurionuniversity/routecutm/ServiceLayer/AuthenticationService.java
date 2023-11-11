package com.centurionuniversity.routecutm.ServiceLayer;

import com.centurionuniversity.routecutm.AdminInfo;
import com.centurionuniversity.routecutm.RqstRes.LoginCredentials;

public interface AuthenticationService {
    Object login(LoginCredentials loginInfo);
}
