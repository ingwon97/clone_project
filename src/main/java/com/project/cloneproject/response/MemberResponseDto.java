package com.project.cloneproject.response;


import com.example.carrot.model.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
@AllArgsConstructor
public class MemberResponseDto {

    private Long id;
    private String username;
    private String nickname;
    private String profile;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public MemberResponseDto(Member member){
        this.username = member.getUsername();
        this.nickname = member.getNickname();
        this.profile = member.getProfile();
        this.createdAt = member.getCreatedAt();
        this.modifiedAt = member.getModifiedAt();
    }

    public MemberResponseDto(String nickname, String username) {
        this.nickname = nickname;
        this.username = username;
    }
}
