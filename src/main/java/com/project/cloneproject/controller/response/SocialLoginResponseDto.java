package com.project.cloneproject.controller.response;

import com.project.cloneproject.domain.Member;
import lombok.Getter;

@Getter
public class SocialLoginResponseDto {
    //소셜로그인 시 body로 내려가는 사용자 정보

    private Long userId;

    private String nickname;

    private boolean login;

    private String profileImage;

    //소셜 로그인할 때 프론트에 내려주는 값
    public SocialLoginResponseDto(Member member, boolean login) {
        this.userId = member.getId();
        this.nickname = member.getNickname();
        this.login = login;  //login true/ false 상황
        this.profileImage = member.getProfileImg();
    }

}
