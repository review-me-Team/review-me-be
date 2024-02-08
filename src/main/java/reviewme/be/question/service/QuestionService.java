package reviewme.be.question.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reviewme.be.question.repository.QuestionRepository;
import reviewme.be.util.entity.Label;
import reviewme.be.util.repository.LabelRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final LabelRepository labelRepository;

    public List<Label> findQuestionLabels(long resumeId) {

        return labelRepository.findByResumeId(resumeId);
    }
}
