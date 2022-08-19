package com.project.cloneproject.repository;


import com.project.cloneproject.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByUsername(String user);

    Optional<Member> findById(Long id);

    Optional<Member> findByNickname(String nickname);


}
