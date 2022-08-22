package com.project.cloneproject.controller.response;

import com.project.cloneproject.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostResponseTempDto {

    private Post posts;
    private int LikeNum = 0;
    private int commentNum = 0;
}
