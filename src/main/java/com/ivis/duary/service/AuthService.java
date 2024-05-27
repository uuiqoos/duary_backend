package com.ivis.duary.service;

import com.ivis.duary.data.*;

public interface AuthService {

    AuthorizationTokenData signIn(SignInReq req);

    AuthorizationTokenData signUp(SignUpReq req);

    AuthorizationTokenData reissue(TokenReq req);

    MemberRes me();
}
