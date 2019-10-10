package com.chess.chessapi.viewmodels;

import com.chess.chessapi.constants.AuthProvider;
import com.chess.chessapi.entities.Certificate;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class UserUpdateViewModel {
    @NotNull(message = "User id is required not null")
    private long userId;

    @NotNull(message = "Full Name is required not null")
    @Length(min = 6,max = 255, message = "Full name should be in range 6~255 characters")
    @Column(name = "full_name")
    private String fullName;

    @Length(max = 255, message = "Link avatar shouldn't larger than 255 characters")
    private String avatar;

    @Column(name = "role_id")
    private long roleId;


    @Length(max = 255, message = "Achievement shouldn't larger than 255 characters")
    private String achievement;

    @Valid
    private List<CertificateUpdateViewModel> certificates;


    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    public String getAchievement() {
        return achievement;
    }

    public void setAchievement(String achievement) {
        this.achievement = achievement;
    }

    public List<CertificateUpdateViewModel> getCertificates() {
        return certificates;
    }

    public void setCertificates(List<CertificateUpdateViewModel> certificates) {
        this.certificates = certificates;
    }
}
