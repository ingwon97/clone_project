package com.project.cloneproject.repository;

import com.project.cloneproject.domain.Likes;
import com.project.cloneproject.domain.Member;
import com.project.cloneproject.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Likes, Long> {

    Optional<Likes> findByMemberAndPost(Member member, Post post);

    int countByPost(Post post);
}
