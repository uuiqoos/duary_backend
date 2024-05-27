package com.ivis.duary.api;

import com.ivis.duary.config.response.Response;
import com.ivis.duary.data.*;
import com.ivis.duary.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthApi {

    private final AuthService authService;

    @PostMapping("/signIn")
    public Response<AuthorizationTokenData> signIn(@RequestBody SignInReq req) {
        return new Response<>(authService.signIn(req));
    }

    @PostMapping("/signUp")
    public Response<AuthorizationTokenData> signUp(@RequestBody SignUpReq req) {
        return new Response<>(authService.signUp(req));
    }

    @PostMapping("/token")
    public Response<AuthorizationTokenData> token(@RequestBody TokenReq req) {
        return new Response<>(authService.reissue(req));
    }

    @GetMapping("/me")
    public Response<MemberRes> me() {
        return new Response<>(authService.me());
    }
}
