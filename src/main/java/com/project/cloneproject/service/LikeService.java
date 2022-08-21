package com.project.cloneproject.service;

import com.project.cloneproject.controller.response.ResponseDto;
import com.project.cloneproject.domain.Likes;
import com.project.cloneproject.domain.Member;
import com.project.cloneproject.domain.Post;
import com.project.cloneproject.repository.LikeRepository;
import com.project.cloneproject.repository.PostRepository;
import com.project.cloneproject.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;

    private final PostRepository postRepository;

    public ResponseDto<?> addAndDeleteLike(Long postId ,UserDetailsImpl userDetails) {

        Member member = userDetails.getMember();

//        // 멤버를 가지고 오기
//        if (null == request.getHeader("Refresh-Token")) {
//            return ResponseDto.fail("MEMBER_NOT_FOUND",
//                    "로그인이 필요합니다.");
//        }
//
//        if (null == request.getHeader("Authorization")) {
//            return ResponseDto.fail("MEMBER_NOT_FOUND",
//                    "로그인이 필요합니다.");
//        }

        Post post = isPresentPost(postId);

        Likes likes = likeRepository.findByMemberAndPost(member, post).orElse(null);
        if (null != likes) {
            likeRepository.delete(likes);
            return ResponseDto.success(false);
        }

        likeRepository.save(likes);
        return ResponseDto.success(true);
    }


    @Transactional(readOnly = true)
    public Post isPresentPost(Long id) {
        Optional<Post> optionalPost = postRepository.findById(id);
        return optionalPost.orElse(null);
    }
}
