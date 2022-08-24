package com.project.cloneproject.controller;

import com.project.cloneproject.controller.request.AddFriendRequestDto;
import com.project.cloneproject.controller.response.FriendResponseDto;
import com.project.cloneproject.controller.response.ResponseDto;
import com.project.cloneproject.security.UserDetailsImpl;
import com.project.cloneproject.service.FriendService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
public class FriendController {

    private final FriendService friendService;

    @Autowired
    public FriendController(FriendService friendService) {
        this.friendService = friendService;
    }

    @PostMapping("/api/friends/add")
    public ResponseDto<?> addFriend(@RequestBody AddFriendRequestDto requestDto,
                                    @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return friendService.addFriend(requestDto, userDetails);
    }

    @GetMapping("/api/friends")
    public FriendResponseDto getFriends(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return friendService.getFriends(userDetails);
    }

    // member데이터지만, 임시로 friend에 넣었음
    @PostMapping("/api/search/members")
    public ResponseDto<?> searchFriend(@RequestBody String nickname) {
        return friendService.searchFriend(nickname);
    }

}
