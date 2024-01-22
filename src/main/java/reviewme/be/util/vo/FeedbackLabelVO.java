package reviewme.be.util.vo;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reviewme.be.util.repository.LabelRepository;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class FeedbackLabelVO {

    private final LabelRepository labelRepository;

    private Map<Long, String> labels;

    @PostConstruct
    public void init() {
        labels = labelRepository.findByResumeIsNull()
                .stream()
                .collect(
                        HashMap::new,
                        (map, label) -> map.put(label.getId(), label.getContent()),
                        HashMap::putAll
                );
    }
}
