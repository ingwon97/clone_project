package com.project.cloneproject.security.provider;

import com.project.cloneproject.domain.Member;
import com.project.cloneproject.exceptionHandler.CustomException;
import com.project.cloneproject.exceptionHandler.ErrorCode;
import com.project.cloneproject.repository.MemberRepository;
import com.project.cloneproject.security.UserDetailsImpl;
import com.project.cloneproject.security.jwt.JwtDecoder;
import com.project.cloneproject.security.jwt.JwtPreProcessingToken;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JWTAuthProvider implements AuthenticationProvider {

    private final JwtDecoder jwtDecoder;
    private final MemberRepository memberRepository;

    @Override
    public Authentication authenticate(Authentication authentication)
            throws AuthenticationException {
        String token = (String) authentication.getPrincipal();
        String username = jwtDecoder.decodeUsername(token);

        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        UserDetailsImpl userDetails = new UserDetailsImpl(member);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtPreProcessingToken.class.isAssignableFrom(authentication);
    }
}
