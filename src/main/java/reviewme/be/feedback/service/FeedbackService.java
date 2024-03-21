package reviewme.be.feedback.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reviewme.be.feedback.dto.request.CreateFeedbackCommentRequest;
import reviewme.be.feedback.dto.request.CreateFeedbackRequest;
import reviewme.be.feedback.dto.request.UpdateFeedbackCheckRequest;
import reviewme.be.feedback.dto.request.UpdateFeedbackContentRequest;
import reviewme.be.feedback.dto.request.UpdateFeedbackEmojiRequest;
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
import reviewme.be.util.entity.Emoji;
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
        Feedback parentFeedback = findParentFeedbackByIdAndResumeId(parentId, resumeId);

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

        // 피드백 목록 조회
        Page<FeedbackResponse> feedbackPage = feedbackRepository.findFeedbacksByResumeIdAndResumePage(
            resumeId, user.getId(), resumePage, pageable);

        List<FeedbackResponse> feedbacks = feedbackPage.getContent();

        feedbacks.forEach(feedback -> {
            List<EmojiCount> emojiCounts = feedbackEmojiRepository.findFeedbackEmojiCountByFeedbackId(
                feedback.getId());
            feedback.setEmojis(emojiCounts);
        });

        return FeedbackPageResponse.builder()
            .feedbacks(feedbacks)
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

        // 피드백 대댓글 목록 조회 후 id 오름차순으로 재정렬
        Page<FeedbackCommentResponse> feedbackCommentPage = feedbackRepository.findFeedbackCommentsByParentId(
            parentFeedbackId, user.getId(), pageable);

        List<FeedbackCommentResponse> feedbackComments = feedbackCommentPage.getContent();

        feedbackComments.forEach(feedbackComment -> {
            List<EmojiCount> emojiCounts = feedbackEmojiRepository.findFeedbackEmojiCountByFeedbackId(
                feedbackComment.getId());
            feedbackComment.setEmojis(emojiCounts);
        });

        feedbackComments = sortFeedbackCommentsByIdAsc(feedbackComments);

        return FeedbackCommentPageResponse.builder()
            .feedbackComments(feedbackComments)
            .pageNumber(feedbackCommentPage.getNumber())
            .lastPage(feedbackCommentPage.getTotalPages() - 1)
            .pageSize(feedbackCommentPage.getSize())
            .build();
    }

    @Transactional
    public void deleteFeedback(long resumeId, long feedbackId, User user) {

        // 이력서 존재 여부 확인
        resumeService.findById(resumeId);

        // 피드백 존재 여부 확인 및 유저 검증
        Feedback feedback = findById(feedbackId);
        feedback.validateUser(user);
        LocalDateTime deletedAt = LocalDateTime.now();
        feedback.softDelete(deletedAt);
        feedbackEmojiRepository.deleteAllByFeedbackIdAndUserIdIsNotNull(feedbackId);

        if (feedback.getParentFeedback() != null) {
            feedback.getParentFeedback().minusChildCnt();
        }
    }

    @Transactional
    public void updateFeedbackContent(UpdateFeedbackContentRequest request,
        long resumeId, long feedbackId, User user) {

        // 이력서 존재 여부 확인
        resumeService.findById(resumeId);

        // 피드백 존재 여부 확인 및 유저 검증
        Feedback feedback = findById(feedbackId);
        feedback.validateUser(user);

        Label label = null;

        if (request.getLabelId() != null) {
            label = utilService.findFeedbackLabelById(request.getLabelId());
        }

        feedback.updateContent(label, request.getContent());
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

    @Transactional
    public void updateFeedbackEmoji(UpdateFeedbackEmojiRequest request, long resumeId,
        long feedbackId, User user) {

        // 이력서로 피드백 존재 여부 확인
        Feedback feedback = findByIdAndResumeId(feedbackId, resumeId);

        // 기존 이모지 삭제
        Optional<FeedbackEmoji> feedbackEmoji = feedbackEmojiRepository.findByFeedbackIdAndUserId(
            feedbackId, user.getId());

        Emoji emoji = emojisVO.findEmojiById(request.getId());

        if (feedbackEmoji.isPresent()) {
            feedbackEmoji.get().updateEmoji(emojisVO.findEmojiById(request.getId()));
            return;
        }

        feedbackEmojiRepository.save(
            new FeedbackEmoji(user, feedback, emoji)
        );
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

        return feedbackRepository.findParentFeedbackById(feedbackId)
            .orElseThrow(() -> new NonExistFeedbackException("존재하지 않는 피드백입니다."));
    }

    // 대댓글 추가 시, 삭제된 피드백에도 추가할 수 있다.
    private Feedback findParentFeedbackByIdAndResumeId(long feedbackId, long resumeId) {

        return feedbackRepository.findParentFeedbackByIdAndResumeId(feedbackId, resumeId)
            .orElseThrow(() -> new NonExistFeedbackException("존재하지 않는 피드백입니다."));
    }

    private void saveDefaultEmojis(Feedback savedFeedback) {
        feedbackEmojiRepository.saveAll(
            FeedbackEmoji.createDefaultFeedbackEmojis(
                savedFeedback,
                emojisVO.getEmojis())
        );
    }

    /**
     * 대댓글 조회 시 id 오름차순으로 재정렬
     */
    private List<FeedbackCommentResponse> sortFeedbackCommentsByIdAsc(
        List<FeedbackCommentResponse> feedbackCommentInfos) {

        return feedbackCommentInfos.stream()
            .sorted(Comparator.comparingLong(FeedbackCommentResponse::getId))
            .collect(Collectors.toList());
    }
}
