package reviewme.be.util.vo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reviewme.be.util.entity.Label;
import reviewme.be.util.repository.LabelRepository;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Component
@RequiredArgsConstructor
public class FeedbackLabelsVO {

    private final LabelRepository labelRepository;

    private Map<Long, Label> feedbackLabels;
    private List<Label> feedbackLabelList;

    @PostConstruct
    public void init() {

        feedbackLabelList = labelRepository.findByResumeIsNull();

        feedbackLabels = feedbackLabelList.stream()
                .collect(
                        HashMap::new,
                        (map, label) -> map.put(label.getId(), label),
                        HashMap::putAll
                );
    }
}
