package reviewme.be.feedback.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reviewme.be.feedback.dto.request.CreateFeedbackCommentRequest;
import reviewme.be.feedback.dto.request.CreateFeedbackRequest;
import reviewme.be.feedback.dto.request.UpdateFeedbackCheckRequest;
import reviewme.be.feedback.dto.request.UpdateFeedbackContentRequest;
import reviewme.be.feedback.entity.Feedback;
import reviewme.be.feedback.exception.NonExistFeedbackException;
import reviewme.be.feedback.repository.FeedbackEmojiRepository;
import reviewme.be.feedback.repository.FeedbackRepository;
import reviewme.be.resume.entity.Resume;
import reviewme.be.resume.service.ResumeService;
import reviewme.be.user.entity.User;
import reviewme.be.util.entity.Label;
import reviewme.be.util.service.UtilService;

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final UtilService utilService;
    private final FeedbackRepository feedbackRepository;
    private final FeedbackEmojiRepository feedbackEmojiRepository;
    private final ResumeService resumeService;

    @Transactional
    public void saveFeedback(CreateFeedbackRequest request, User commenter, long resumeId) {

        // 이력서, 피드백, 라벨 존재 여부 확인
        Resume resume = resumeService.findById(resumeId);
        Label label = null;

        if (request.getLabelId() != null) {
            label = utilService.findFeedbackLabelById(request.getLabelId());
        }

        feedbackRepository.save(Feedback.createdFeedback(
            commenter,
            resume,
            label,
            request.getContent(),
            request.getResumePage()));
    }

    @Transactional
    public void saveFeedbackComment(CreateFeedbackCommentRequest request, User commenter,
        long resumeId, long parentId) {

        // 이력서, 피드백 존재 여부 확인
        Resume resume = resumeService.findById(resumeId);
        Feedback parentFeedback = findById(parentId);

        if (!parentFeedback.isParentFeedback()) {
            throw new NonExistFeedbackException("해당 피드백에는 대댓글을 추가할 수 없습니다.");
        }

        feedbackRepository.save(Feedback.createFeedbackComment(
            commenter,
            resume,
            parentFeedback,
            request.getContent()));

        parentFeedback.plusChildCnt();
    }

    // TODO: 목록 조회, 삭제 여부 확인을 통해 응답값 변경 (child 존재 여부 등)

    @Transactional
    public void deleteFeedback(User user, long resumeId, long feedbackId) {

        // 이력서 존재 여부 확인
        resumeService.findById(resumeId);

        // 피드백 존재 여부 확인 및 유저 검증
        Feedback feedback = findById(feedbackId);
        feedback.validateUser(user);
        feedback.softDelete();
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
        Feedback feedback = validateFeedbackByResumeId(feedbackId, resumeId);
        feedback.updateChecked(request.isChecked());
    }

    private Feedback findById(long feedbackId) {

        return feedbackRepository.findByIdAndDeletedAtIsNull(feedbackId)
            .orElseThrow(() -> new NonExistFeedbackException("존재하지 않는 피드백입니다."));
    }

    private Feedback validateFeedbackByResumeId(long feedbackId, long resumeId) {

        return feedbackRepository.findByIdAndResumeIdAndDeletedAtIsNull(feedbackId, resumeId)
            .orElseThrow(() -> new NonExistFeedbackException("존재하지 않는 피드백입니다."));
    }
}
