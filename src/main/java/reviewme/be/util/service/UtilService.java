package reviewme.be.util.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reviewme.be.util.entity.Label;
import reviewme.be.util.entity.Occupation;
import reviewme.be.util.entity.Scope;
import reviewme.be.util.exception.NonExistOccupationException;
import reviewme.be.util.exception.NonExistScopeException;
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

    public Label findFeedbackLabelById(long id) {

        Map<Long, Label> feedbackLabels = feedbackLabelsVO.getFeedbackLabels();

        if (!feedbackLabels.containsKey(id)) {
            throw new NonExistOccupationException("[ERROR] 존재하지 않는 라벨입니다.");
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

}
