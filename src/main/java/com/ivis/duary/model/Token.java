package com.ivis.duary.model;

import com.ivis.duary.base.BaseEntity;
import com.ivis.duary.data.AuthorizationTokenData;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Token extends BaseEntity {

    @Column
    private String accessToken;

    @Column
    private String refreshToken;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private Member member;


    public static Token of(AuthorizationTokenData tokenData, Member member) {
        return new Token(tokenData.getAccessToken(), tokenData.getRefreshToken(), member);
    }

}
