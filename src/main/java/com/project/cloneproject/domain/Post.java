package com.project.cloneproject.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.cloneproject.controller.request.PostRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Post extends Timestamped{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "member_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    private String imageUrl;

    @Column(nullable = false)
    @Lob
    private String content;

    @JsonIgnore
    @OneToMany(mappedBy = "post",cascade = CascadeType.REMOVE, orphanRemoval = true)
    private final List<Comment> commentList = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private final List<Likes> likes = new ArrayList<>();


    public Post(PostRequestDto postRequestDto, Member member) {
        this.imageUrl = postRequestDto.getImageUrl();
        this.content = postRequestDto.getContent();
        this.member = member;
    }

    public void update(PostRequestDto postRequestDto) {
        this.imageUrl = postRequestDto.getImageUrl();
        this.content = postRequestDto.getContent();
    }
}
