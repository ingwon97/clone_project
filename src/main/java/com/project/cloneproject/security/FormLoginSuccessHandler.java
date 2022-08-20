package com.project.cloneproject.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.cloneproject.controller.response.LoginResponseDto;
import com.project.cloneproject.domain.Member;
import com.project.cloneproject.security.jwt.JwtTokenUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class FormLoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    public static final String AUTH_HEADER = "Authorization";
    public static final String TOKEN_TYPE = "BEARER";
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void onAuthenticationSuccess(final HttpServletRequest request, final HttpServletResponse response,
                                        final Authentication authentication) throws IOException {
        final UserDetailsImpl userDetails = ((UserDetailsImpl) authentication.getPrincipal());
        // Token 생성
        final String token = JwtTokenUtils.generateJwtToken(userDetails);
        System.out.println(userDetails.getUsername() + "'s token : " + TOKEN_TYPE + " " + token);
        response.addHeader(AUTH_HEADER, TOKEN_TYPE + " " + token);
        System.out.println("LOGIN SUCCESS!");

        //Member nicakname 내려주기 - 동관 천재님꺼 참고
        response.setContentType("application/json; charset=utf-8");
        Member member = userDetails.getMember();
        LoginResponseDto loginResponseDto = new LoginResponseDto(member.getId(), member.getNickname(), true, token, member.getProfileImg());
        String result = mapper.writeValueAsString(loginResponseDto);
        response.getWriter().write(result);
    }
}
