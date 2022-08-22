package com.project.cloneproject.controller;

import com.project.cloneproject.controller.request.CommentRequestDto;
import com.project.cloneproject.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    // 댓글 작성
    @PostMapping("/api/comments/{postId}")
    public ResponseEntity<?> createComment(@PathVariable Long postId, @RequestBody CommentRequestDto commentRequestDto) {
        return commentService.createComment(postId, commentRequestDto);

    }

    // 댓글 수정
    @PutMapping("/api/comments/{postId}/{commentId}")
    public ResponseEntity<?> updateComment(@PathVariable Long postId, @PathVariable Long commentId, @RequestBody CommentRequestDto commentRequestDto) {
        return commentService.updateComment(postId, commentId, commentRequestDto);

    }

    // 댓글 삭제
    @DeleteMapping("/api/comments/{postId}/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Long postId, @PathVariable Long commentId) {
        return commentService.deleteComment(postId, commentId);

    }

    // 선택 게시글 댓글 조회
    @GetMapping("/api/comments/{postId}")
    public ResponseEntity<?> readComment(@PathVariable Long postId) {
        return commentService.readComment(postId);

    }
}
