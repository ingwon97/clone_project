package com.project.cloneproject.controller.response;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
@Builder
public class SignpResponseDto {
    private String username;

    private String nickname;

    private String password;

    private boolean admin;
}
