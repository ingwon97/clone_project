package com.project.cloneproject.repository;

import com.project.cloneproject.domain.Friend;
import com.project.cloneproject.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FriendRepository extends JpaRepository<Friend, Long> {
    Optional<Friend> findByMemberAndId(Member member, Long Id);

    List<Friend> findAllByMember(Member member);
}