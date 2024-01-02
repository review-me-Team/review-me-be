package reviewme.be.resume.service;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reviewme.be.resume.repository.ResumeRepository;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ResumeService {

    private final S3Client s3Client;
    private final ResumeRepository resumeRepository;

    @Value("${AWS_S3_BUCKET_NAME}")
    private String bucketName;

    /**
     * Take MultiFile data, create a url, and upload to S3
     * Recursively recreate URLs if they are duplicates
     * @param resumeFile
     * @return url
     */
    public String uploadResumeFile(MultipartFile resumeFile) {

        StringBuilder sb = new StringBuilder();
        String fileName =  createFileNameWithUUID(resumeFile.getOriginalFilename());
        String contentTypeOfResumeFile = resumeFile.getContentType();

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .contentDisposition("inline")
                .contentType(contentTypeOfResumeFile)
                .build();

        String url = sb.append("https://")
                .append(bucketName)
                .append(".s3.ap-northeast-2.amazonaws.com/")
                .append(fileName)
                .toString();

        try {
            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(resumeFile.getInputStream(), resumeFile.getSize()));
            return url;
        } catch (Exception e) {
            throw new IllegalStateException("Failed to upload file", e);
        }
    }

    private String createFileNameWithUUID(String fileName) {

        StringBuilder sb = new StringBuilder();

        String newFileName = sb.append(UUID.randomUUID().toString(), 0, 8)
                .append(fileName)
                .toString();

        resumeRepository.findByUrl(newFileName).ifPresent(
                resumeByUrl -> createFileNameWithUUID(fileName)
        );

        return newFileName;
    }
}
