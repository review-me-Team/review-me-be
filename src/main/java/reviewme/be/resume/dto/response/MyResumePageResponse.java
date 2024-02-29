package reviewme.be.resume.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
@Builder
@Schema(description = "이력서 목록 응답")
public class MyResumePageResponse {

    @Schema(description = "이력서 목록")
    private List<MyResumeResponse> resumes;

    @Schema(description = "현재 페이지", example = "1")
    private int pageNumber;

    @Schema(description = "페이징 적용 시 전체 페이지 수", example = "1")
    private int lastPage;

    @Schema(description = "페이징 적용 시 한 번에 받아오는 데이터 개수", example = "1")
    private int pageSize;

    public static MyResumePageResponse fromMyResumePageable(Page<MyResumeResponse> resumePage) {

        return MyResumePageResponse.builder()
                .resumes(resumePage.getContent())
                .pageNumber(resumePage.getNumber())
                .lastPage(resumePage.getTotalPages() - 1)
                .pageSize(resumePage.getSize())
                .build();
    }
}
