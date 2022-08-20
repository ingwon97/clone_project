package com.project.cloneproject.controller;


import com.project.cloneproject.controller.request.PostRequestDto;
import com.project.cloneproject.controller.request.SignupImgRequestDto;
import com.project.cloneproject.controller.request.SignupRequestDto;
import com.project.cloneproject.security.UserDetailsImpl;
import com.project.cloneproject.service.AwsS3Service;
import com.project.cloneproject.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@RestController
@RequiredArgsConstructor
public class MemberController {

  private final MemberService memberService;
  private final AwsS3Service s3Service;

  //회원가입
  @PostMapping("/member/signup")
  public ResponseEntity signupUser(@RequestPart("signup") SignupRequestDto requestDto,
                                   @RequestPart("profileImage") MultipartFile profileImages) throws IOException {
    String defaultImg = "https://buckitforimg.s3.ap-northeast-2.amazonaws.com/default_profile.png"; // 기본이미지
    String image = "";
    // 이미지를 안 넣으면 기본이미지 주기
    if (profileImages.isEmpty()) { // 이미지가 안들어오면 true
      image = defaultImg;
    } else {  // profileImages에 유저가 등록한 이미지가 들어올 때
      String content = new String(profileImages.getBytes());

      PostRequestDto dto = new PostRequestDto(content,"");
      image = s3Service.getSavedS3ImageUrl(dto);


    }

    return memberService.signupUser(SignupRequestDto.builder()
            .username(requestDto.getUsername())
            .nickname(requestDto.getNickname())
            .password(requestDto.getPassword())
            .build(), image);
  }

  //회원가입에 이미지가 null이 들어올 때
  @PostMapping("/api/member/signup")
  public ResponseEntity signupNullUser(@RequestBody SignupImgRequestDto requestDto) {
    return memberService.signupNullUser(requestDto);
  }

  //username 중복체크
  @PostMapping("/api/member/signup/checkID")
  public ResponseEntity checkUsername(@RequestBody SignupRequestDto requestDto) {
    return memberService.checkUsername(requestDto);
  }

  //nickname 중복체크
  @PostMapping("/api/member/signup/nickID")
  public ResponseEntity checkNickname(@RequestBody SignupRequestDto requestDto) {
    return memberService.checkNickname(requestDto);
  }

  //로그인 후 관리자 권한 얻을 수 있는 API
//  @PutMapping("/api/signup/admin")
//  public ResponseEntity adminAuthorization(@RequestBody AdminRequestDto requestDto,
//                                           @AuthenticationPrincipal UserDetailsImpl userDetails) {
//    return memberService.adminAuthorization(requestDto, userDetails);
//  }

  //소셜로그인 사용자 정보 조회
  @GetMapping("/social/member/islogin")
  public ResponseEntity socialUserInfo(@AuthenticationPrincipal UserDetailsImpl userDetails) {
    return memberService.socialUserInfo(userDetails);
  }

}
