package reviewme.be.resume.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reviewme.be.resume.repository.ResumeRepository;
import reviewme.be.resume.request.UpdateResumeRequest;
import reviewme.be.resume.request.UploadResumeRequest;
import reviewme.be.resume.response.ResumeDetailResponse;
import reviewme.be.resume.response.ResumePageResponse;
import reviewme.be.resume.response.ResumeResponse;
import reviewme.be.resume.response.UploadResumeResponse;
import reviewme.be.util.CustomResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "resume", description = "이력서(resume) API")
@RequestMapping("/resume")
@RestController
@RequiredArgsConstructor
public class ResumeController {

    private final ResumeRepository resumeRepository;

    @Operation(summary = "이력서 업로드", description = "이력서를 업로드합니다.")
    @PostMapping
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "이력서 업로드 성공"),
            @ApiResponse(responseCode = "400", description = "이력서 업로드 실패")
    })
    public ResponseEntity<CustomResponse<UploadResumeResponse>> uploadResume(
            @Parameter(description = "PDF 파일", content = @Content(mediaType = "multipart/form-data", schema = @Schema(type = "file", format = "binary")))
            @RequestPart MultipartFile pdf,
            @RequestPart UploadResumeRequest uploadResumeRequest
    ) {

        UploadResumeResponse createdResumeId = UploadResumeResponse.builder()
                .id(1L)
                .build();

        return ResponseEntity
                .ok()
                .body(new CustomResponse<>(
                        "success",
                        200,
                        "이력서 업로드에 성공했습니다.",
                        createdResumeId
                ));
    }

    @Operation(summary = "이력서 목록 조회", description = "이력서 목록을 조회합니다.")
    @GetMapping
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "이력서 목록 조회 성공"),
            @ApiResponse(responseCode = "400", description = "이력서 목록 조회 실패")
    })
    public ResponseEntity<CustomResponse<ResumePageResponse>> showResumes(@PageableDefault(size=20) Pageable pageable) {


        // 내 이력서 목록 조회
        // TODO: pageable 적용

        List<ResumeResponse> resumeResponse = resumeRepository.findByUserId(1L)
                .stream()
                .map(ResumeResponse::fromResume)
                .collect(Collectors.toList());

        return ResponseEntity
                .ok()
                .body(new CustomResponse<>(
                        "success",
                        200,
                        "전체 공개 이력서 목록 조회에 성공했습니다.",
                        ResumePageResponse.builder()
                                .resumes(resumeResponse)
                                .build()
                ));
    }

    @Operation(summary = "이력서 상세 조회", description = "이력서 상세 내용을 조회합니다.")
    @GetMapping("/{resumeId}")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "이력서 상세 내용 조회 성공"),
            @ApiResponse(responseCode = "400", description = "이력서 상세 내용 조회 실패")
    })
    public ResponseEntity<CustomResponse<ResumeDetailResponse>> showResumeDetail(@PathVariable Long resumeId) {

        // TODO: S3 연결 후, sample resume 세팅
        // TODO: findByResumeId -> get resumeUrl
        // TODO: pdf url 암호화 필요할 수 있음 (회의 후 결정)

        ResumeDetailResponse sampleResponse = ResumeDetailResponse.builder()
                .resumeUrl("https://review-me-resume.s3.ap-northeast-2.amazonaws.com/resume/7562857e-130f-4f9a-9ca1-441908180b31_%E1%84%8B%E1%85%B5%E1%84%85%E1%85%A7%E1%86%A8%E1%84%89%E1%85%A5_%E1%84%89%E1%85%A2%E1%86%B7%E1%84%91%E1%85%B3%E1%86%AF.pdf")
                .build();

        return ResponseEntity
                .ok()
                .body(new CustomResponse<>(
                        "success",
                        200,
                        "이력서 상세 내용 조회에 성공했습니다.",
                        sampleResponse
                ));
    }

    @Operation(summary = "이력서 삭제", description = "이력서를 삭제합니다.")
    @DeleteMapping("/{resumeId}")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "이력서 삭제 성공"),
            @ApiResponse(responseCode = "400", description = "이력서 삭제 실패")
    })
    public ResponseEntity<CustomResponse> deleteResume(@PathVariable Long resumeId) {

        return ResponseEntity
                .ok()
                .body(new CustomResponse<>(
                        "success",
                        200,
                        "이력서 삭제에 성공했습니다."
                ));
    }

    @Operation(summary = "이력서 수정", description = "이력서를 수정합니다.")
    @PatchMapping("/{resumeId}")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "이력서 수정 성공"),
            @ApiResponse(responseCode = "400", description = "이력서 수정 실패")
    })
    public ResponseEntity<CustomResponse<ResumeResponse>> updateResume(@PathVariable Long resumeId, @RequestBody UpdateResumeRequest updateResumeRequest) {

        ResumeResponse sampleUpdatedResumeResponse = ResumeResponse.builder()
                .id(1L)
                .title("네이버 신입 대비") // TODO: updateResumeRequest Title()
                .writer("aken-you")
                .createdAt(LocalDateTime.now())
                .scopeId(1L)    // TODO: updateResumeRequest ScopeId()
                .occupationId(1)
                .year(0L)
                .build();

        return ResponseEntity
                .ok()
                .body(new CustomResponse<>(
                        "success",
                        200,
                        "이력서 수정에 성공했습니다.",
                        sampleUpdatedResumeResponse
                ));
    }
}
