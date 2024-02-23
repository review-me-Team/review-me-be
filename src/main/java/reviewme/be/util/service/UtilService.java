package reviewme.be.util.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reviewme.be.util.dto.EmojiCount;
import reviewme.be.util.dto.MyEmoji;
import reviewme.be.util.entity.Label;
import reviewme.be.util.entity.Occupation;
import reviewme.be.util.entity.Scope;
import reviewme.be.util.exception.NonExistLabelException;
import reviewme.be.util.exception.NonExistOccupationException;
import reviewme.be.util.exception.NonExistScopeException;
import reviewme.be.util.repository.LabelRepository;
import reviewme.be.util.vo.EmojisVO;
import reviewme.be.util.vo.FeedbackLabelsVO;
import reviewme.be.util.vo.OccupationsVO;
import reviewme.be.util.vo.ScopesVO;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class UtilService {

    private final FeedbackLabelsVO feedbackLabelsVO;
    private final OccupationsVO occupationsVO;
    private final ScopesVO scopesVO;
    private final EmojisVO emojisVO;
    private final LabelRepository labelRepository;

    public Label findFeedbackLabelById(long id) {

        Map<Long, Label> feedbackLabels = feedbackLabelsVO.getFeedbackLabels();

        if (!feedbackLabels.containsKey(id)) {
            throw new NonExistLabelException("[ERROR] 존재하지 않는 라벨입니다.");
        }

        return feedbackLabels.get(id);
    }

    public Occupation findOccupationById(int id) {

        Map<Integer, Occupation> occupations = occupationsVO.getOccupations();

        if (!occupations.containsKey(id)) {
            throw new NonExistOccupationException("[ERROR] 존재하지 않는 직군입니다.");
        }

        return occupations.get(id);
    }

    public Scope findScopeById(int id) {

        Map<Integer, Scope> scopes = scopesVO.getScopes();

        if (!scopes.containsKey(id)) {
            throw new NonExistScopeException("[ERROR] 존재하지 않는 공개범위입니다.");
        }

        return scopes.get(id);
    }

    @Transactional(readOnly = true)
    public Label findById(long id) {

        return labelRepository.findById(id)
                .orElseThrow(() -> new NonExistLabelException("[ERROR] 존재하지 않는 라벨입니다."));
    }

    public List<List<EmojiCount>> collectEmojiCounts(List<EmojiCount> emojiCounts) {

        List<List<EmojiCount>> groupedEmojiCounts = new ArrayList<>();

        // 선택할 수 있는 이모지의 총 개수
        int emojisSize = emojisVO.getEmojisSize();

        for (int emojiCount = 0; emojiCount < emojiCounts.size(); ) {

            List<EmojiCount> commentEmojiCount = new ArrayList<>();

            for (int commentEmoji = 0; commentEmoji < emojisSize; commentEmoji++) {
                commentEmojiCount.add(emojiCounts.get(emojiCount));
                emojiCount++;
            }
            groupedEmojiCounts.add(commentEmojiCount);
        }

        return groupedEmojiCounts;
    }

    /**
     * 사용자가 선택한 이모지 id 리스트 추출
     */
    public List<Integer> getMyEmojiIds(List<MyEmoji> myEmojis) {

        List<Integer> myEmojiIds = myEmojis
            .stream()
            .map(MyEmoji::getEmojiId)
            .collect(Collectors.toList());

        return myEmojiIds;
    }
}
