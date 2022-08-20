package com.project.cloneproject.service;

import com.project.cloneproject.controller.request.CommentRequestDto;
import com.project.cloneproject.controller.response.CommentResponseDto;
import com.project.cloneproject.controller.response.CommentsResponseDto;
import com.project.cloneproject.controller.response.ResponseDto;
import com.project.cloneproject.domain.Comment;
import com.project.cloneproject.domain.Member;
import com.project.cloneproject.domain.Post;
import com.project.cloneproject.repository.CommentRepository;
import com.project.cloneproject.repository.MemberRepository;
import com.project.cloneproject.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;


    // 댓글 작성
    @Transactional
    public ResponseEntity createComment(Long postId, CommentRequestDto commentRequestDto) {

        // 해당 아이디의 게시글이 존재하는지 여부
        Optional<Post> postIsPresent = postRepository.findById(postId); // postRepository 가 없어서 내가 임의로 빨간줄 상태로 뒀음.
        if(postIsPresent.isEmpty()) {
            return new ResponseEntity(ResponseDto.fail("404", "게시물이 존재하지 않습니다."), HttpStatus.NOT_FOUND);
        }

        // CommentRequestDto의 값을 DB에 저장
        Post post = postRepository.findById(postId).orElseThrow();
        String loginUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberRepository.findByUsername(loginUsername).orElseThrow();
        Comment comment = Comment.builder()
                .post(post)
                .member(member)
                .comment(commentRequestDto.getComment())
                .build();
        commentRepository.save(comment);

        // CommentResponseDto 작성
        CommentResponseDto commentResponseDto = CommentResponseDto.builder()
                .id(comment.getId())
                .comment(comment.getComment())
                .nickname(comment.getMember().getNickname())
                .createdAt(comment.getCreatedAt())
                .modifiedAt(comment.getModifiedAt())
                .build();

        // 리턴
        return new ResponseEntity(ResponseDto.success(commentResponseDto),HttpStatus.OK);

    }



    // 댓글 수정
    @Transactional
    public ResponseEntity updateComment(Long postId, Long commentId, CommentRequestDto commentRequestDto) {

        // 해당 댓글이 존재하는지 여부
        if(commentExist(commentId) == 1) {
            return new ResponseEntity(ResponseDto.fail("404", "댓글이 존재하지 않습니다."), HttpStatus.NOT_FOUND);
        }
        Comment comment = commentRepository.findById(commentId).orElseThrow();

        // 수정 권한 여부
        if(!comment.getMember().getUsername().equals(SecurityContextHolder.getContext().getAuthentication().getName())) {
            return new ResponseEntity(ResponseDto.fail("401", "댓글 작성자만 수정할 수 있습니다."), HttpStatus.UNAUTHORIZED);
        }

        // CommentRequestDto의 값을 DB에 저장함으로써 업데이트
        comment.update(commentRequestDto);

        // CommentResponseDto 작성
        CommentResponseDto commentResponseDto = CommentResponseDto.builder()
                .id(comment.getId())
                .comment(comment.getComment())
                .nickname(comment.getMember().getNickname())
                .createdAt(comment.getCreatedAt())
                .modifiedAt(comment.getModifiedAt())
                .build();

        // 리턴
        return new ResponseEntity(ResponseDto.success(commentResponseDto),HttpStatus.OK);


    }



    // 댓글 삭제
    @Transactional
    public ResponseEntity deleteComment(Long postId, Long commentId) {

        // 해당 댓글이 존재하는지 여부
        if(commentExist(commentId) == 1) {
            return new ResponseEntity(ResponseDto.fail("404", "false"), HttpStatus.NOT_FOUND);
//            return new ResponseEntity(ResponseDto.fail("404", "댓글이 존재하지 않습니다."), HttpStatus.NOT_FOUND);
        }
        Comment comment = commentRepository.findById(commentId).orElseThrow();

        // 삭제 권한 여부
        if(!comment.getMember().getUsername().equals(SecurityContextHolder.getContext().getAuthentication().getName())) {
            return new ResponseEntity(ResponseDto.fail("401", "false"), HttpStatus.UNAUTHORIZED);
//            return new ResponseEntity(ResponseDto.fail("401", "댓글 작성자만 삭제할 수 있습니다."), HttpStatus.UNAUTHORIZED);
        }

        // 삭제 후 리턴
        commentRepository.deleteById(commentId);
        return new ResponseEntity(ResponseDto.success("true"), HttpStatus.OK);

    }



    // 댓글 조회
    public ResponseEntity readComment() {

        // 무한 참조 루프를 막기위해서 Comment 클래스를 그냥 쓰지않고 Dto 를 만들어서 그것을 보여주자
        List<Comment> commentList = commentRepository.findAll();
        List<CommentsResponseDto> commentsResponseDtoList = new ArrayList<>();
        for(Comment comment : commentList) {
            commentsResponseDtoList.add(CommentsResponseDto.builder()
                    .id(comment.getId())
                    .comment(comment.getComment())
                    .nickname(comment.getMember().getNickname())
                    .createdAt(comment.getCreatedAt())
                    .modifiedAt(comment.getModifiedAt())
                    .build()
            );
        }

        // 리턴
        return new ResponseEntity(ResponseDto.success(commentsResponseDtoList), HttpStatus.OK);



    }




    // 댓글 존재여부 판단
    public int commentExist(Long commentId) {
        Optional<Comment> commentIsPresent = commentRepository.findById(commentId);
        if(commentIsPresent.isEmpty()) {
            return 1; // 댓글 존재하지 않음.
        }
        return 2; // 댓글 존재함

    }



}
