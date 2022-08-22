package com.project.cloneproject.repository;

import com.project.cloneproject.domain.Comment;
import com.project.cloneproject.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Long countByPost(Post post);
}