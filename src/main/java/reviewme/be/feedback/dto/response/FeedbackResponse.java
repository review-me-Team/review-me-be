package reviewme.be.feedback.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import reviewme.be.util.dto.EmojiCount;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Schema(description = "피드백 정보 응답")
public class FeedbackResponse {

    @Schema(description = "피드백 ID", example = "1")
    private Long id;

    @Schema(description = "피드백 내용", example = "뭔가 이력서에 문제 해결과 관련된 내용이 부족한 것같아요.")
    private String content;

    @Schema(description = "피드백을 남긴 사용자 ID", example = "1")
    private long commenterId;

    @Schema(description = "피드백을 남긴 사용자 이름", example = "aken-you")
    private String commenterName;

    @Schema(description = "피드백을 남긴 사용자 프로필 사진", example = "https://avatars.githubusercontent.com/u/96980857?v=4")
    private String commenterProfileUrl;

    @Schema(description = "피드백 라벨", example = "프로젝트")
    private String labelContent;

    @Schema(description = "피드백 작성 시간", example = "2024-01-02 01:32")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;

    @Schema(description = "댓글 개수", example = "10")
    private long countOfReplies;

    @Schema(description = "체크 여부", example = "true")
    private Boolean checked;

    @Schema(description = "이모지 정보")
    private List<EmojiCount> emojis;

    @Schema(description = "내가 선택한 이모지", example = "1")
    private Integer myEmojiId;

    @QueryProjection
    public FeedbackResponse(Long id, String content, long commenterId, String commenterName,
        String commenterProfileUrl, String labelContent, LocalDateTime createdAt, long countOfReplies,
        Boolean checked, Integer myEmojiId) {

        this.id = id;
        this.content = content;
        this.commenterId = commenterId;
        this.commenterName = commenterName;
        this.commenterProfileUrl = commenterProfileUrl;
        this.labelContent = labelContent;
        this.createdAt = createdAt;
        this.countOfReplies = countOfReplies;
        this.checked = checked;
        this.myEmojiId = myEmojiId;
    }
}
