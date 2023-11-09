package com.centurionuniversity.routecutm.RqstRes;

import lombok.Data;

@Data
public class PasswordUpdateRequest {
    private String oldPass;
    private String newPass;
}
