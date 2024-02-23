package reviewme.be.feedback.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import reviewme.be.feedback.entity.Feedback;
import reviewme.be.util.dto.EmojiCount;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
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
    private boolean checked;

    @Schema(description = "이모지 정보")
    private List<EmojiCount> emojis;

    @Schema(description = "내가 선택한 이모지", example = "1")
    private Integer myEmojiId;

    public static FeedbackResponse fromFeedbackOfOwnResume(Feedback feedback,
        List<EmojiCount> emojis, Integer myEmojiId) {

        return FeedbackResponse.builder()
            .id(feedback.getId())
            .content(feedback.getContent())
            .commenterId(feedback.getCommenter().getId())
            .commenterName(feedback.getCommenter().getName())
            .commenterProfileUrl(feedback.getCommenter().getProfileUrl())
            .labelContent(feedback.getLabel().getContent())
            .createdAt(feedback.getCreatedAt())
            .countOfReplies(feedback.getChildCnt())
            .checked(feedback.isChecked())
            .emojis(emojis)
            .myEmojiId(myEmojiId)
            .build();
    }
}
