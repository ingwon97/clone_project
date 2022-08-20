package com.project.cloneproject.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.project.cloneproject.controller.request.PostRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AwsS3Service {

    private final AmazonS3 amazonS3;
    @Value("${cloud.aws.s3.bucket.url}")
    private String defaultEndpointUrl;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    /*public static File getImageFromBase64(String base64String, String fileName) {
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
    }*/

    @Transactional
    public String getSavedS3ImageUrl(PostRequestDto postRequestDto) throws IOException {
        String fileName = UUID.randomUUID().toString();
        String fileUrl;

        try {
            File file = convert(postRequestDto.getImageUrl()).get();
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

    private void uploadFileToS3Bucket(String fileName, File file) {
        amazonS3.putObject(new PutObjectRequest(bucket, fileName, file).
                withCannedAcl(CannedAccessControlList.PublicRead));
    }

    @Transactional
    public void deleteImage(String deleteUrl) {
        String deleteFileName = deleteUrl.substring(defaultEndpointUrl.length() + 1);
        amazonS3.deleteObject(new DeleteObjectRequest(bucket,deleteFileName));
    }


    // 로컬에 파일 업로드 하기
    private Optional<File> convert(String stringImage) throws IOException {
        byte[] bytes = decodeBase64(stringImage);
        File convertFile = new File(System.getProperty("user.dir") + "/" + "tempFile");
        if (convertFile.createNewFile()) { // 바로 위에서 지정한 경로에 File이 생성됨 (경로가 잘못되었다면 생성 불가능)
            try (FileOutputStream fos = new FileOutputStream(convertFile)) { // FileOutputStream 데이터를 파일에 바이트 스트림으로 저장하기 위함
                fos.write(bytes);
            }
            return Optional.of(convertFile);
        }

        return Optional.empty();
    }

    public byte[] decodeBase64(String encodedFile) {
        String substring = encodedFile.substring(encodedFile.indexOf(",") + 1);
        Base64.Decoder decoder = Base64.getDecoder();
        return decoder.decode(substring);
    }
}
