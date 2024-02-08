package reviewme.be.question.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reviewme.be.question.repository.QuestionRepository;
import reviewme.be.resume.service.ResumeService;
import reviewme.be.util.entity.Label;
import reviewme.be.util.repository.LabelRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final ResumeService resumeService;
    private final LabelRepository labelRepository;

    public List<Label> findQuestionLabels(long resumeId) {

        // 이력서 존재 여부 확인
        resumeService.findById(resumeId);

        return labelRepository.findByResumeId(resumeId);
    }
}
