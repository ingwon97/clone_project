package com.project.cloneproject.controller.response;

import com.project.cloneproject.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SocialUserInfoDto {
    private String socialId;
    private String nickname;
    private String email;
    private String profileImage;

    public SocialUserInfoDto(Member member) {
        this.socialId = member.getSocialId();
        this.nickname = member.getNickname();
        this.email = member.getUsername();
        this.profileImage = member.getProfileImg();
    }
}
