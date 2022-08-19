package com.project.cloneproject.controller;

import com.project.cloneproject.controller.response.ResponseDto;
import com.project.cloneproject.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    @PostMapping("/api/likes/{postId}")
    public ResponseDto<?> addAndDeleteLike(@PathVariable Long postId, HttpServletRequest request) {
        return likeService.addAndDeleteLike(postId, request);
    }
}
