package reviewme.be.feedback.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reviewme.be.feedback.dto.FeedbackCommentInfo;
import reviewme.be.feedback.dto.FeedbackInfo;
import reviewme.be.feedback.dto.request.CreateFeedbackCommentRequest;
import reviewme.be.feedback.dto.request.CreateFeedbackRequest;
import reviewme.be.feedback.dto.request.UpdateFeedbackCheckRequest;
import reviewme.be.feedback.dto.request.UpdateFeedbackContentRequest;
import reviewme.be.feedback.dto.response.FeedbackCommentPageResponse;
import reviewme.be.feedback.dto.response.FeedbackCommentResponse;
import reviewme.be.feedback.dto.response.FeedbackPageResponse;
import reviewme.be.feedback.dto.response.FeedbackResponse;
import reviewme.be.feedback.entity.Feedback;
import reviewme.be.feedback.entity.FeedbackEmoji;
import reviewme.be.feedback.exception.NonExistFeedbackException;
import reviewme.be.feedback.repository.FeedbackEmojiRepository;
import reviewme.be.feedback.repository.FeedbackRepository;
import reviewme.be.resume.entity.Resume;
import reviewme.be.resume.service.ResumeService;
import reviewme.be.user.entity.User;
import reviewme.be.util.dto.EmojiCount;
import reviewme.be.util.entity.Label;
import reviewme.be.util.service.UtilService;
import reviewme.be.util.vo.EmojisVO;

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final UtilService utilService;
    private final FeedbackRepository feedbackRepository;
    private final FeedbackEmojiRepository feedbackEmojiRepository;
    private final ResumeService resumeService;
    private final EmojisVO emojisVO;

    @Transactional
    public void saveFeedback(CreateFeedbackRequest request, User commenter, long resumeId) {

        // 이력서, 피드백, 라벨 존재 여부 확인
        Resume resume = resumeService.findById(resumeId);
        Label label = null;

        // TODO: 이력서 공개 범위에 따라 요청한 사용자가 작성할 수 있는지 검증

        if (request.getLabelId() != null) {
            label = utilService.findFeedbackLabelById(request.getLabelId());
        }

        Feedback savedFeedback = feedbackRepository.save(Feedback.createdFeedback(
            commenter,
            resume,
            label,
            request.getContent(),
            request.getResumePage()));

        // Default Feedback Emojis 생성
        saveDefaultEmojis(savedFeedback);
    }

    @Transactional
    public void saveFeedbackComment(CreateFeedbackCommentRequest request, long resumeId,
        User commenter, long parentId) {

        // 이력서, 피드백 존재 여부 확인
        Resume resume = resumeService.findById(resumeId);
        Feedback parentFeedback = findByIdAndResumeId(parentId, resumeId);

        // TODO: 이력서 공개 범위에 따라 요청한 사용자가 작성할 수 있는지 검증

        if (!parentFeedback.isParentFeedback()) {
            throw new NonExistFeedbackException("해당 피드백에는 대댓글을 추가할 수 없습니다.");
        }

        Feedback savedFeedback = feedbackRepository.save(Feedback.createFeedbackComment(
            commenter,
            resume,
            parentFeedback,
            request.getContent()));

        parentFeedback.plusChildCnt();

        // Default Feedback Emojis 생성
        saveDefaultEmojis(savedFeedback);
    }

    @Transactional(readOnly = true)
    public FeedbackPageResponse getFeedbacks(long resumeId, int resumePage,
        User user,
        Pageable pageable) {

        resumeService.findById(resumeId);

        // 피드백 목록 조회 후 id 목록 추출
        Page<FeedbackInfo> feedbackPage = feedbackRepository.findFeedbacksByResumeIdAndResumePage(
            resumeId, resumePage, pageable);
        List<FeedbackInfo> feedbacks = feedbackPage.getContent();
        List<Long> feedbackIds = extractFeedbackIds(feedbacks);

        List<List<EmojiCount>> emojiCounts = utilService.collectEmojiCounts(
            feedbackEmojiRepository.findEmojiCountByFeedbackIds(feedbackIds));

        List<FeedbackResponse> feedbacksResponse = collectToFeedbacksResponse(feedbackIds,
            feedbacks, emojiCounts, user);

        return FeedbackPageResponse.builder()
            .feedbacks(feedbacksResponse)
            .pageNumber(feedbackPage.getNumber())
            .lastPage(feedbackPage.getTotalPages() - 1)
            .pageSize(feedbackPage.getSize())
            .build();
    }

    @Transactional(readOnly = true)
    public FeedbackCommentPageResponse getFeedbackComments(long resumeId, long parentFeedbackId,
        User user,
        Pageable pageable) {

        // 이력서, 부모 피드백 존재 여부 확인
        resumeService.findById(resumeId);
        findParentFeedbackById(parentFeedbackId);

        // 피드백 대댓글 목록 조회 후 id 목록 추출
        Page<FeedbackCommentInfo> feedbackPage = feedbackRepository.findFeedbackCommentsByFeedbackId(
            parentFeedbackId, pageable);
        List<FeedbackCommentInfo> feedbackComments = feedbackPage.getContent();
        List<Long> feedbackCommentIds = extractFeedbackCommentIds(feedbackComments);

        List<List<EmojiCount>> emojiCounts = utilService.collectEmojiCounts(
            feedbackEmojiRepository.findEmojiCountByFeedbackIds(feedbackCommentIds));

        List<FeedbackCommentResponse> feedbackCommentsResponse = collectToFeedbackCommentsResponse(
            feedbackCommentIds,
            feedbackComments,
            emojiCounts, user);

        return FeedbackCommentPageResponse.builder()
            .feedbackComments(feedbackCommentsResponse)
            .pageNumber(feedbackPage.getNumber())
            .lastPage(feedbackPage.getTotalPages() - 1)
            .pageSize(feedbackPage.getSize())
            .build();
    }

    @Transactional
    public void deleteFeedback(User user, long resumeId, long feedbackId) {

        // 이력서 존재 여부 확인
        resumeService.findById(resumeId);

        // 피드백 존재 여부 확인 및 유저 검증
        Feedback feedback = findById(feedbackId);
        feedback.validateUser(user);
        LocalDateTime deletedAt = LocalDateTime.now();
        feedback.softDelete(deletedAt);
        feedbackEmojiRepository.deleteAllByFeedbackId(feedbackId);

        if (feedback.getParentFeedback() != null) {
            feedback.getParentFeedback().minusChildCnt();
        }
    }

    @Transactional
    public void updateFeedbackContent(UpdateFeedbackContentRequest request, User user,
        long resumeId, long feedbackId) {

        // 이력서 존재 여부 확인
        resumeService.findById(resumeId);

        // 피드백 존재 여부 확인 및 유저 검증
        Feedback feedback = findById(feedbackId);
        feedback.validateUser(user);
        feedback.updateContent(request.getContent());
    }

    @Transactional
    public void updateFeedbackCheck(UpdateFeedbackCheckRequest request, User user, long resumeId,
        long feedbackId) {

        // 이력서 존재 여부 확인 및 유저 검증
        Resume resume = resumeService.findById(resumeId);
        resume.validateUser(user);

        // 피드백 존재 여부 확인
        Feedback feedback = findByIdAndResumeId(feedbackId, resumeId);
        feedback.updateChecked(request.isChecked());
    }

    private Feedback findById(long feedbackId) {

        return feedbackRepository.findByIdAndDeletedAtIsNull(feedbackId)
            .orElseThrow(() -> new NonExistFeedbackException("존재하지 않는 피드백입니다."));
    }

    private Feedback findByIdAndResumeId(long feedbackId, long resumeId) {

        return feedbackRepository.findByIdAndResumeIdAndDeletedAtIsNull(feedbackId, resumeId)
            .orElseThrow(() -> new NonExistFeedbackException("존재하지 않는 피드백입니다."));
    }

    private Feedback findParentFeedbackById(long feedbackId) {

        return feedbackRepository.findFeedbackById(feedbackId)
            .orElseThrow(() -> new NonExistFeedbackException("존재하지 않는 피드백입니다."));
    }

    private void saveDefaultEmojis(Feedback savedFeedback) {
        feedbackEmojiRepository.saveAll(
            FeedbackEmoji.createDefaultFeedbackEmojis(
                savedFeedback,
                emojisVO.getEmojis())
        );
    }

    /***************
     * 아래는 피드백 목록 조회 시 사용되는 메서드입니다.
     ***************/
    private List<Long> extractFeedbackIds(List<FeedbackInfo> feedbacks) {

        return feedbacks.stream()
            .map(FeedbackInfo::getId)
            .collect(Collectors.toList());
    }

    private List<FeedbackResponse> collectToFeedbacksResponse(List<Long> feedbackIds,
        List<FeedbackInfo> feedbacks,
        List<List<EmojiCount>> emojiCounts, User user) {

        List<FeedbackResponse> feedbacksResponse = new ArrayList<>();

        for (int feedbackIdx = 0; feedbackIdx < feedbackIds.size(); feedbackIdx++) {

            FeedbackInfo feedback = feedbacks.get(feedbackIdx);
            List<EmojiCount> emojis = emojiCounts.get(feedbackIdx);
            Integer myEmojiId = findMyEmojiIdByFeedbackId(feedbackIds.get(feedbackIdx), user);

            FeedbackResponse feedbackResponse = FeedbackResponse.fromFeedbackOfResume(feedback,
                emojis, myEmojiId);

            feedbacksResponse.add(feedbackResponse);
        }

        return feedbacksResponse;
    }

    private List<Long> extractFeedbackCommentIds(List<FeedbackCommentInfo> feedbackComments) {

        return feedbackComments.stream()
            .map(FeedbackCommentInfo::getId)
            .collect(Collectors.toList());
    }

    private List<FeedbackCommentResponse> collectToFeedbackCommentsResponse(List<Long> feedbackIds,
        List<FeedbackCommentInfo> feedbackComments,
        List<List<EmojiCount>> emojiCounts, User user) {

        List<FeedbackCommentResponse> feedbackCommentsResponse = new ArrayList<>();

        for (int feedbackCommentIdx = 0; feedbackCommentIdx < feedbackIds.size();
            feedbackCommentIdx++) {

            FeedbackCommentInfo feedbackComment = feedbackComments.get(feedbackCommentIdx);
            List<EmojiCount> emojis = emojiCounts.get(feedbackCommentIdx);
            Integer myEmojiId = findMyEmojiIdByFeedbackId(feedbackIds.get(feedbackCommentIdx),
                user);

            feedbackCommentsResponse.add(
                FeedbackCommentResponse.fromFeedbackComment(feedbackComment, emojis, myEmojiId)
            );
        }

        return feedbackCommentsResponse;
    }

    private Integer findMyEmojiIdByFeedbackId(Long feedbackId, User user) {

        return feedbackEmojiRepository.findByFeedbackIdAndUserId(
                feedbackId, user.getId())
            .map(el -> el.getEmoji().getId())
            .orElse(null);
    }
}
