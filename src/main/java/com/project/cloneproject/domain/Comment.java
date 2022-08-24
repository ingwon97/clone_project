package com.project.cloneproject.domain;

import com.project.cloneproject.controller.request.CommentRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Builder @Getter
@NoArgsConstructor
@AllArgsConstructor
public class Comment extends Timestamped{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String comment;

    @JoinColumn(name = "post_id", nullable = false )
    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;

    @JoinColumn(name = "member_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;






    public void update(CommentRequestDto commentRequestDto) {
        this.comment = commentRequestDto.getComment();

    }

}
