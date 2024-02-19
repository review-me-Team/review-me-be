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

import java.time.LocalDateTime;
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

    @Transactional
    public long saveResume(UploadResumeRequest resumeRequest, User writer) {

        String resumeFileName = uploadResumeFile(resumeRequest.getPdf());

        Scope scope = utilService.findScopeById(resumeRequest.getScopeId());
        Occupation occupation = utilService.findOccupationById(resumeRequest.getOccupationId());

        Resume createdResume = resumeRepository.save(
                Resume.ofCreated(resumeRequest, writer, scope, occupation, resumeFileName)
        );

        return createdResume.getId();
    }

    @Transactional(readOnly = true)
    public Page<ResumeResponse> getResumes(ResumeSearchCondition searchCondition, Pageable pageable) {

        // TODO: 친구 여부 검증 로직 필요

        return resumeRepository.findAllByDeletedAtIsNull(searchCondition, pageable);
    }

    @Transactional(readOnly = true)
    public ResumeDetailResponse getResumeDetail(long resumeId, User user) {

        Resume resume = findById(resumeId);

        if (user.isAnonymous() && resume.isPublic()) {
            return ResumeDetailResponse.fromResume(resume);
        }

        validateAccessRights(resume, user);

        return ResumeDetailResponse.fromResume(resume);
    }

    @Transactional
    public void deleteResume(long resumeId, User user) {

        // 이력서 존재 여부 및 삭제 권한 확인
        Resume resume = findById(resumeId);
        resume.validateUser(user);

        LocalDateTime deletedAt = LocalDateTime.now();
        resume.softDelete(deletedAt);
    }

    @Transactional
    public void updateResume(UpdateResumeRequest request, long resumeId, User user) {

        Resume resume = findById(resumeId);
        resume.validateUser(user);

        Scope modifiedScope = utilService.findScopeById(request.getScopeId());
        Occupation modifiedOccupation = utilService.findOccupationById(request.getOccupationId());

        resume.update(request, modifiedScope, modifiedOccupation);
    }

    public Resume findById(long resumeId) {

        return resumeRepository.findByIdAndDeletedAtIsNull(resumeId)
                .orElseThrow(() -> new NonExistResumeException("해당 이력서가 존재하지 않습니다."));
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

        resumeRepository.findByUrlAndDeletedAtIsNull(newFileName)
                .ifPresent(resumeByUrl -> createFileNameWithUUID(fileName));

        return newFileName;
    }

    private void validateFileExtension(MultipartFile resumeFile) {

        String extension = StringUtils.getFilenameExtension(resumeFile.getOriginalFilename());

        if (extension == null || !extension.equals("pdf")) {
            throw new BadFileExtensionException("pdf 파일만 업로드 가능합니다.");
        }
    }

    private void validateAccessRights(Resume resume, User user) {

        if (resume.getScope().getId() == 2 && !friendService.isFriend(user.getId(), resume.getWriter().getId())) {
            throw new NonExistResumeException("해당 이력서에 접근할 수 없습니다.");
        }

        if (resume.getScope().getId() == 3) {
            resume.validateUser(user);
        }
    }
}
