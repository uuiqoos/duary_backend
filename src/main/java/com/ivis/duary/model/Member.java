package com.ivis.duary.model;

import com.ivis.duary.base.BaseEntity;
import com.ivis.duary.data.SignUpReq;
import com.ivis.duary.auth.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Member extends BaseEntity {

    @Column
    private String username;

    @Column
    private String password;

    @Column
    private Role role;


    public static Member of(SignUpReq req) {
        return new Member(req.getUsername(), req.getPassword(), Role.USER);
    }
}
