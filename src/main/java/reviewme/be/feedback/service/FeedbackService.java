package reviewme.be.feedback.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reviewme.be.feedback.dto.request.CreateFeedbackRequest;
import reviewme.be.feedback.dto.request.UpdateFeedbackCheckRequest;
import reviewme.be.feedback.dto.request.UpdateFeedbackContentRequest;
import reviewme.be.feedback.entity.Feedback;
import reviewme.be.feedback.exception.NonExistFeedbackException;
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
    private final ResumeService resumeService;

    @Transactional
    public void saveFeedback(CreateFeedbackRequest request, User commenter, long resumeId) {

        // 이력서, 피드백, 라벨 존재 여부 확인
        Resume resume = resumeService.findById(resumeId);
        Feedback feedback = null;
        Label label = null;

        if (request.getLabelId() != null) {
            label = utilService.findFeedbackLabelById(request.getLabelId());
        }

        // 대댓글이라면 부모 피드백 존재 여부 확인 및 부모 피드백의 댓글 수 증가
        if (request.getFeedbackId() != null) {
            feedback = findById(request.getFeedbackId());
            feedback.plusChildCnt();
            // TODO: 부모 피드백의 parentFeedback이 null이 아닌 경우 예외 처리
        }

        feedbackRepository.save(Feedback.ofCreated(
                commenter,
                resume,
                feedback,
                label,
                request.getContent(),
                request.getResumePage()));
    }

    @Transactional
    public void deleteFeedback(User user, long resumeId, long feedbackId) {

        // 이력서 존재 여부 확인
        resumeService.findById(resumeId);

        // 피드백 존재 여부 확인 및 유저 검증
        Feedback feedback = findById(feedbackId);
        feedback.validateUser(user);
        feedback.softDelete();
    }

    @Transactional
    public void updateFeedbackContent(UpdateFeedbackContentRequest request, User user, long resumeId, long feedbackId) {

        // 이력서 존재 여부 확인
        resumeService.findById(resumeId);

        // 피드백 존재 여부 확인 및 유저 검증
        Feedback feedback = findById(feedbackId);
        feedback.validateUser(user);
        feedback.updateContent(request.getContent());
    }

    @Transactional
    public void updateFeedbackCheck(UpdateFeedbackCheckRequest request, User user, long resumeId, long feedbackId) {

        // 이력서 존재 여부 확인 및 유저 검증
        Resume resume = resumeService.findById(resumeId);
        resume.validateUser(user);

        // 피드백 존재 여부 확인
        Feedback feedback = findById(feedbackId);
        feedback.updateChecked(request.isChecked());
    }

    private Feedback findById(long feedbackId) {

        return feedbackRepository.findById(feedbackId)
            .orElseThrow(() -> new NonExistFeedbackException("존재하지 않는 피드백입니다."));
    }
}
