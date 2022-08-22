package com.project.cloneproject.service;


import com.project.cloneproject.controller.request.PostRequestDto;
import com.project.cloneproject.controller.response.PostResponseDto;
import com.project.cloneproject.controller.response.PostResponseTempDto;
import com.project.cloneproject.controller.response.ResponseDto;
import com.project.cloneproject.domain.Friend;
import com.project.cloneproject.domain.Member;
import com.project.cloneproject.domain.Post;
import com.project.cloneproject.domain.Timestamped;
import com.project.cloneproject.repository.CommentRepository;
import com.project.cloneproject.repository.LikeRepository;
import com.project.cloneproject.security.UserDetailsImpl;
import com.project.cloneproject.repository.FriendRepository;
import com.project.cloneproject.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final AwsS3Service awsS3Service;
    private final FriendRepository friendRepository;
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;


    public ResponseDto<PostResponseDto> createPost(PostRequestDto postRequestDto, UserDetailsImpl userDetails) throws IOException {
        String fileUrl = awsS3Service.getSavedS3ImageUrl(postRequestDto);
        postRequestDto.setImageUrl(fileUrl);

        Post post = new Post(postRequestDto, userDetails.getMember());
        postRepository.save(post);

        PostResponseDto postResponseDto = new PostResponseDto(post);
        return ResponseDto.success(postResponseDto);
    }

    @Transactional
    public ResponseEntity<?> updatePost(Long postId, PostRequestDto postRequestDto, UserDetailsImpl userDetails) throws IOException {
        if(postRepository.findById(postId).isEmpty()){
            log.error("요청하신 게시글은 존재하지 않습니다.");
            return new ResponseEntity<>(ResponseDto.fail("NOT_FOUND", "찾으시는 게시글이 없습니다."), HttpStatus.NOT_FOUND);
        }

        Post findPost = postRepository.findById(postId).get();

        if(findPost.getMember().getUsername().equals(userDetails.getUsername())) {
            String deleteUrl = findPost.getImageUrl();
            awsS3Service.deleteImage(deleteUrl);
            String updateImageUrl = awsS3Service.getSavedS3ImageUrl(postRequestDto);
            postRequestDto.setImageUrl(updateImageUrl);
            findPost.update(postRequestDto);
            return new ResponseEntity<>(ResponseDto.success(new PostResponseDto(findPost)),HttpStatus.OK);
        }
        return new ResponseEntity<>(ResponseDto.fail("BAD_REQUEST", "작성자가 아니므로 수정권한이 없습니다."),
                HttpStatus.BAD_REQUEST);
    }

    @Transactional
    public ResponseEntity<?> removePost(Long postId, UserDetailsImpl userDetails) {
        if(postRepository.findById(postId).isEmpty()){
            log.error("요청하신 게시글은 존재하지 않습니다.");
            return new ResponseEntity<>(ResponseDto.fail("NOT_FOUND", "찾으시는 게시글이 없습니다."), HttpStatus.NOT_FOUND);
        }

        Post findPost = postRepository.findById(postId).get();

        if(findPost.getMember().getUsername().equals(userDetails.getUsername())) {
            awsS3Service.deleteImage(findPost.getImageUrl());
            postRepository.delete(findPost);

            return new ResponseEntity<>(ResponseDto.success("게시글 삭제가 완료되었습니다."), HttpStatus.OK);
        }

        return new ResponseEntity<>(ResponseDto.fail("BAD_REQUEST", "작성자가 아니므로 수정권한이 없습니다."),
                HttpStatus.BAD_REQUEST);
    }

    public ResponseDto<?> getPostsByFriends(UserDetailsImpl userDetails) {

        // 자신의 identity
        Member member = userDetails.getMember();

        // 자신이 등록한 친구들을 모두 가져옴
        List<Friend> friendsByMember = friendRepository.findAllByMember(member);

        // post들을 저장할 list배열선언
        List<Post> posts = new ArrayList<>();

        List<Post> allByMember = postRepository.findAllByMemberOrderByCreatedAtDesc(member);
        for (Post post : allByMember) {
            posts.add(post);

        }

        // friends들을 모두 돌려줄 예정정
        // 1. 친구로 등록된 멤버들의 게시글들을 List형태로 저장
        // 2. posts.add => 해당 게시글들을 List<Post> posts 데이터 안에 add
        for (Friend friend : friendsByMember) {
            List<Post> postsByMember = postRepository.findAllByMemberOrderByCreatedAtDesc(friend.getFromMember());
            for (Post post : postsByMember) {
                posts.add(post);
            }
            // 친구의 post데이터가 list형태로 반환 -> list<Post> posts에 넣어주려고 함
        }

        Collections.sort(posts, new Comparator<Post>() {
            @Override
            public int compare(Post o1, Post o2) {
                return o2.getCreatedAt().compareTo(o1.getCreatedAt());
            }
        });

        // 안에다가 commentList랑, like의 개수를 반환해줘야디는데
        List<PostResponseTempDto> postResponseTempDtos = new ArrayList<>();

        for (Post post : posts) {
            postResponseTempDtos.add(PostResponseTempDto.builder()
                    .posts(post)
                    .LikeNum(post.getLikes().size())
                    .commentNum(post.getComments().size())
                    .build());
        }

        // 위에서 추가해준 데이터들을 return해준다
        return ResponseDto.success(postResponseTempDtos);
    }
}
