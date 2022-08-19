package com.project.cloneproject.service;


import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.project.cloneproject.controller.request.PostRequestDto;
import com.project.cloneproject.controller.response.PostResponseDto;
import com.project.cloneproject.controller.response.ResponseDto;
import com.project.cloneproject.domain.Post;
import com.project.cloneproject.domain.UserDetailsImpl;
import com.project.cloneproject.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.util.Base64;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final AmazonS3 amazonS3;
    @Value("${cloud.aws.s3.bucket.url}")
    private String defaultEndpointUrl;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;


    public ResponseDto<PostResponseDto> createPost(PostRequestDto postRequestDto, UserDetailsImpl userDetails) {
        String fileUrl = getSavedS3ImageUrl(postRequestDto);
        postRequestDto.setImageUrl(fileUrl);

        Post post = new Post(postRequestDto, userDetails.getMember());
        postRepository.save(post);

        PostResponseDto postResponseDto = new PostResponseDto(post);
        return ResponseDto.success(postResponseDto);
    }

    @Transactional
    public ResponseEntity<?> updatePost(Long postId, PostRequestDto postRequestDto, UserDetailsImpl userDetails) {
        if(postRepository.findById(postId).isEmpty()){
            log.error("요청하신 게시글은 존재하지 않습니다.");
            return new ResponseEntity<>(ResponseDto.fail("NOT_FOUND", "찾으시는 게시글이 없습니다."), HttpStatus.NOT_FOUND);
        }

        Post findPost = postRepository.findById(postId).get();

        if(findPost.getMember().getUsername().equals(userDetails.getUsername())) {
            String deleteUrl = findPost.getImageUrl();
            deleteImage(deleteUrl);
            String updateImageUrl = getSavedS3ImageUrl(postRequestDto);
            postRequestDto.setImageUrl(updateImageUrl);
            findPost.update(postRequestDto);
            return new ResponseEntity<>(ResponseDto.success(new PostResponseDto(findPost)),HttpStatus.OK);
        }
        return new ResponseEntity<>(ResponseDto.fail("BAD_REQUEST", "작성자가 아니므로 수정권한이 없습니다."),
                    HttpStatus.BAD_REQUEST);
    }

    @Transactional
    public ResponseEntity<?> removePost(Long postId, UserDetailsImpl userDetails) {
        if(postRepository.findById(postId).isEmpty()){
            log.error("요청하신 게시글은 존재하지 않습니다.");
            return new ResponseEntity<>(ResponseDto.fail("NOT_FOUND", "찾으시는 게시글이 없습니다."), HttpStatus.NOT_FOUND);
        }

        Post findPost = postRepository.findById(postId).get();

        if(findPost.getMember().getUsername().equals(userDetails.getUsername())) {
            deleteImage(findPost.getImageUrl());
            postRepository.delete(findPost);

            return new ResponseEntity<>(ResponseDto.success("게시글 삭제가 완료되었습니다."), HttpStatus.OK);
        }

        return new ResponseEntity<>(ResponseDto.fail("BAD_REQUEST", "작성자가 아니므로 수정권한이 없습니다."),
                HttpStatus.BAD_REQUEST);
    }



    //--------------------------file upload method----------------------------------
    private void uploadFileToS3Bucket(String fileName, File file) {
        amazonS3.putObject(new PutObjectRequest(bucket, fileName, file).
                withCannedAcl(CannedAccessControlList.PublicRead));
    }


    public static File getImageFromBase64(String base64String, String fileName) {
        String[] strings = base64String.split(",");

        String extension = switch (strings[0]) {
            case "data:image/jpeg;base64" -> ".jpeg";
            case "data:image/png;base64" -> ".png";
            default -> ".jpg";
        };

        byte[] data = Base64.getDecoder().decode(strings[1]);
        File file = new File(fileName + extension);

        try(OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file))) {
            outputStream.write(data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return file;
    }

    private String getSavedS3ImageUrl(PostRequestDto postRequestDto) {
        String fileName = UUID.randomUUID().toString();
        String fileUrl;

        try {
            File file = getImageFromBase64(postRequestDto.getImageUrl(),fileName);
            fileUrl = defaultEndpointUrl + "/" + fileName;

            uploadFileToS3Bucket(fileName, file);
            file.delete();
            log.info("File uploaded to S3 successfully");
        } catch (Exception e) {
            log.error("Error while uploading the file to S3" + e);
            throw e;
        }
        return fileUrl;
    }

    private void deleteImage(String deleteUrl) {
        String deleteFileName = deleteUrl.substring(defaultEndpointUrl.length() + 1);
        amazonS3.deleteObject(new DeleteObjectRequest(bucket,deleteFileName));
    }
}
