package com.ivis.duary.data;

import com.ivis.duary.auth.Role;
import com.ivis.duary.model.Member;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberRes {

    private final Long id;
    private final String username;
    private final Role role;

    public MemberRes(Long id, String username, Role role) {
        this.id = id;
        this.username = username;
        this.role = role;
    }

    public static MemberRes of(Member member) {
        return new MemberRes(member.getId(), member.getUsername(), member.getRole());
    }

}
