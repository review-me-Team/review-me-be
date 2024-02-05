package reviewme.be.feedback.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reviewme.be.feedback.dto.request.UpdateFeedbackCheckRequest;
import reviewme.be.feedback.dto.request.UpdateFeedbackContentRequest;
import reviewme.be.feedback.entity.Feedback;
import reviewme.be.feedback.exception.NonExistFeedbackException;
import reviewme.be.feedback.repository.FeedbackRepository;
import reviewme.be.resume.entity.Resume;
import reviewme.be.resume.service.ResumeService;
import reviewme.be.user.entity.User;

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final ResumeService resumeService;

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
