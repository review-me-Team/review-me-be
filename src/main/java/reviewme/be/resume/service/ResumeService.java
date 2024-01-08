package reviewme.be.resume.service;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import reviewme.be.resume.exception.BadFileExtensionException;
import reviewme.be.resume.repository.ResumeRepository;
import reviewme.be.resume.dto.request.UploadResumeRequest;
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

    @Value("${BUCKET_URL}")
    private String bucketUrl;

    public long saveResume(UploadResumeRequest resumeRequest) {

        String resumeFileName = uploadResumeFile(resumeRequest.getPdf());

        // TODO: save newResume Entity

        // TODO: 로그인 기능 구현 전까지 userId가 1인 user로 사용

        long savedResumeId = 1L;

        return savedResumeId;
    }

    /**
     * Take MultiFile data, create a url, and upload to S3
     * Recursively recreate URLs if they are duplicates
     * delete the bucket URL and return it
     *
     * @param resumeFile
     * @return String with the bucket URL deleted
     */
    private String uploadResumeFile(MultipartFile resumeFile) {

        validateFileExtension(resumeFile);

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

            sb.setLength(0);

            String modifiedFileName = sb.append(url)
                    .delete(0, bucketUrl.length())
                    .toString();

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

    private void validateFileExtension(MultipartFile resumeFile) {

        String extension = StringUtils.getFilenameExtension(resumeFile.getOriginalFilename());

        System.out.println(extension);

        if (extension == null || !extension.equals("pdf")) {
            throw new BadFileExtensionException("pdf 파일만 업로드 가능합니다.");
        }
    }
}
