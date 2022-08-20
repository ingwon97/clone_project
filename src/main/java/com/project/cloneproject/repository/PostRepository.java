package com.project.cloneproject.repository;

import com.project.cloneproject.domain.Member;
import com.project.cloneproject.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByMember(Member member);
}
