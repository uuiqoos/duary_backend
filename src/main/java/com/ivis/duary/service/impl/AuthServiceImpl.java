package com.ivis.duary.service.impl;

import com.ivis.duary.auth.AuthorizationTokenProvider;
import com.ivis.duary.config.exception.CustomException;
import com.ivis.duary.config.response.ResponseCode;
import com.ivis.duary.data.*;
import com.ivis.duary.model.Member;
import com.ivis.duary.model.Token;
import com.ivis.duary.repository.MemberRepository;
import com.ivis.duary.repository.TokenRepository;
import com.ivis.duary.service.AuthService;
import com.ivis.duary.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final MemberRepository memberRepository;
    private final TokenRepository tokenRepository;
    private final AuthorizationTokenProvider tokenProvider;
    private final AuthUtil authUtil;

    @Override
    public AuthorizationTokenData signIn(SignInReq req) {
        Member member = memberRepository.findByUsernameAndPassword(req.getUsername(), req.getPassword()).orElseThrow(() -> {
           throw new CustomException(ResponseCode.NO_USER_FOUND);
        });

        AuthorizationTokenData tokenData = tokenProvider.generateAuthorizationTokenData(member);
        tokenRepository.save(Token.of(tokenData, member));

        return tokenData;
    }

    @Override
    public AuthorizationTokenData signUp(SignUpReq req) {
        List<Member> check = memberRepository.findByUsername(req.getUsername());

        if (!check.isEmpty()) {
            throw new CustomException(ResponseCode.DUPLICATE_USERNAME);
        }
        Member member = Member.of(req);
        memberRepository.save(member);

        AuthorizationTokenData tokenData = tokenProvider.generateAuthorizationTokenData(member);
        tokenRepository.save(Token.of(tokenData, member));

        return tokenData;
    }

    @Override
    public AuthorizationTokenData reissue(TokenReq req) {
        Member member = memberRepository.findById(tokenProvider.getSubject(req.getAccessToken())).orElseThrow();

        AuthorizationTokenData tokenData = tokenProvider.generateAuthorizationTokenData(member);
        tokenRepository.save(Token.of(tokenData, member));

        return tokenData;
    }

    @Override
    public MemberRes me() {
        Member member = memberRepository.findById(authUtil.getLoginMemberId()).orElseThrow(() -> {
            throw new CustomException(ResponseCode.FORBIDDEN);
        });

        return MemberRes.of(member);
    }
}
