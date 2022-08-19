package com.project.cloneproject.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Friend {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 자신의 정보
    @JoinColumn(name = "member_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    /*// 친구가 등록한 것
    @JoinColumn(name = "to_member_id", nullable = true)
    @ManyToOne(fetch = FetchType.LAZY)
    private Member toMember;*/
    // 내가 등록한 것
    @JoinColumn(name = "from_member_id", nullable = true)
    @ManyToOne(fetch = FetchType.LAZY)
    private Member fromMember;

}
