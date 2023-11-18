package reviewme.be.resume;

import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(summary = "main", description = "전체 공개 이력서 목록을 조회합니다.")
    @GetMapping
    public ResponseEntity<CustomResponse<ResumePageResponse>> showAllResumes() {

        List<ResumeResponse> sampleResponse = List.of(
                ResumeResponse.builder()
                        .id(1L)
                        .title("이력서 제목")
                        .writer("이름")
                        .createdAt(LocalDateTime.now())
                        .scope("전체 공개")
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
