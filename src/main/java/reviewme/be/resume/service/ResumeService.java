package reviewme.be.resume.service;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import reviewme.be.friend.service.FriendService;
import reviewme.be.resume.dto.ResumeSearchCondition;
import reviewme.be.resume.dto.request.UpdateResumeRequest;
import reviewme.be.resume.dto.response.ResumeDetailResponse;
import reviewme.be.resume.dto.response.ResumeResponse;
import reviewme.be.resume.entity.Resume;
import reviewme.be.resume.exception.BadFileExtensionException;
import reviewme.be.resume.exception.NonExistResumeException;
import reviewme.be.resume.exception.NotYourResumeException;
import reviewme.be.resume.repository.ResumeRepository;
import reviewme.be.resume.dto.request.UploadResumeRequest;
import reviewme.be.user.service.UserService;
import reviewme.be.util.entity.Occupation;
import reviewme.be.util.entity.Scope;
import reviewme.be.user.entity.User;
import reviewme.be.util.service.UtilService;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ResumeService {

    private final S3Client s3Client;
    private final ResumeRepository resumeRepository;

    private final FriendService friendService;
    private final UserService userService;
    private final UtilService utilService;

    @Value("${AWS_S3_BUCKET_NAME}")
    private String bucketName;

    @Value("${BUCKET_URL}")
    private String bucketUrl;

    @Transactional
    public long saveResume(UploadResumeRequest resumeRequest, long userId) {

        String resumeFileName = uploadResumeFile(resumeRequest.getPdf());

        // TODO: 로그인 기능 구현 전까지 userId가 1인 user로 사용
        User user = userService.getUserById(userId);
        Scope scope = utilService.getScopeById(resumeRequest.getScopeId());
        Occupation occupation = utilService.getOccupationById(resumeRequest.getOccupationId());

        Resume createdResume = resumeRepository.save(
                Resume.ofCreated(resumeRequest, user, scope, occupation, resumeFileName)
        );

        return createdResume.getId();
    }

    @Transactional(readOnly = true)
    public Page<ResumeResponse> getResumes(ResumeSearchCondition searchCondition, Pageable pageable) {

        // TODO: 친구 여부 검증 로직 필요

        return resumeRepository.findAllByDeletedAtIsNull(searchCondition, pageable);
    }

    @Transactional(readOnly = true)
    public ResumeDetailResponse getResumeDetail(long resumeId, long userId) {

        Resume resume = resumeRepository.findByIdAndDeletedAtIsNull(resumeId)
                .orElseThrow(() -> new NonExistResumeException("해당 이력서가 존재하지 않습니다."));

        String scope = resume.getScope().getScope();
        long resumeOwnerId = resume.getUser().getId();

        if (scope.equals("private") && resumeOwnerId != userId) {
            throw new NonExistResumeException("해당 이력서가 존재하지 않습니다.");
        }

        if (scope.equals("friend") && !(friendService.isFriend(userId, resumeOwnerId))) {
            throw new NonExistResumeException("해당 이력서가 존재하지 않습니다.");
        }

        return ResumeDetailResponse.fromResume(resume);
    }

    @Transactional
    public void deleteResume(long resumeId, long userId) {

        Resume resume = resumeRepository.findByIdAndDeletedAtIsNull(resumeId)
                .orElseThrow(() -> new NonExistResumeException("해당 이력서가 존재하지 않습니다."));

        User owner = resume.getUser();

        if (owner.getId() != userId) {
            throw new NotYourResumeException("이력서를 삭제할 권한이 없습니다.");
        }

        resume.softDelete();
    }

    @Transactional
    public void updateResume(UpdateResumeRequest request, long resumeId, long userId) {

        Resume resume = resumeRepository.findByIdAndDeletedAtIsNull(resumeId)
                .orElseThrow(() -> new NonExistResumeException("해당 이력서가 존재하지 않습니다."));

        User owner = resume.getUser();

        if (owner.getId() != userId) {
            throw new NotYourResumeException("이력서를 수정할 권한이 없습니다.");
        }

        Scope modifiedScope = utilService.getScopeById(request.getScopeId());
        Occupation modifiedOccupation = utilService.getOccupationById(request.getOccupationId());

        resume.update(request, modifiedScope, modifiedOccupation);
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

        String fileName =  createFileNameWithUUID(resumeFile.getOriginalFilename());
        String contentTypeOfResumeFile = resumeFile.getContentType();

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .contentDisposition("inline")
                .contentType(contentTypeOfResumeFile)
                .build();

        try {
            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(resumeFile.getInputStream(), resumeFile.getSize()));

            return fileName;
        } catch (Exception e) {
            throw new IllegalStateException("Failed to upload file", e);
        }
    }

    private String createFileNameWithUUID(String fileName) {

        StringBuilder sb = new StringBuilder();

        String newFileName = sb.append(UUID.randomUUID().toString(), 0, 8)
                .append(fileName)
                .toString();

        resumeRepository.findByUrlAndDeletedAtIsNull(newFileName).ifPresent(
                resumeByUrl -> createFileNameWithUUID(fileName)
        );

        return newFileName;
    }

    private void validateFileExtension(MultipartFile resumeFile) {

        String extension = StringUtils.getFilenameExtension(resumeFile.getOriginalFilename());

        if (extension == null || !extension.equals("pdf")) {
            throw new BadFileExtensionException("pdf 파일만 업로드 가능합니다.");
        }
    }
}
