package reviewme.be.resume.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reviewme.be.resume.response.ResumePageResponse;
import reviewme.be.resume.response.ResumeResponse;
import reviewme.be.util.CustomResponse;

import java.time.LocalDateTime;
import java.util.List;

@Tag(name = "resume", description = "이력서(resume) API")
@RequestMapping("/resume")
@RestController
@RequiredArgsConstructor
public class ResumeController {

    @Operation(summary = "resume", description = "이력서 목록을 조회합니다.")
    @GetMapping
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "이력서 목록 조회 성공"),
            @ApiResponse(responseCode = "400", description = "이력서 목록 조회 실패")
    })
    public ResponseEntity<CustomResponse<ResumePageResponse>> showResumes() {

        List<ResumeResponse> sampleResponse = List.of(
                ResumeResponse.builder()
                        .id(1L)
                        .title("네이버 신입 대비")
                        .writer("aken-you")
                        .createdAt(LocalDateTime.now())
                        .scope("public")
                        .build());

        return ResponseEntity
                .ok()
                .body(new CustomResponse<>(
                        "success",
                        200,
                        "전체 공개 이력서 목록 조회에 성공했습니다.",
                        ResumePageResponse.builder()
                                .resumePage(sampleResponse)
                                .build()
                ));
    }
}
