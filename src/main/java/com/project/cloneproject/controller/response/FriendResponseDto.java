package com.project.cloneproject.controller.response;

import com.project.cloneproject.domain.Member;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class FriendResponseDto {

    List<Member> friends = new ArrayList<>();
}
