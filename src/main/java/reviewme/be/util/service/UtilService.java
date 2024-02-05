package reviewme.be.util.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reviewme.be.util.entity.Occupation;
import reviewme.be.util.entity.Scope;
import reviewme.be.util.exception.NonExistOccupationException;
import reviewme.be.util.exception.NonExistScopeException;
import reviewme.be.util.repository.OccupationRepository;
import reviewme.be.util.repository.ScopeRepository;
import reviewme.be.util.vo.OccupationsVO;
import reviewme.be.util.vo.ScopesVO;

@Service
@RequiredArgsConstructor
public class UtilService {

    private final OccupationsVO occupationsVO;
    private final ScopesVO scopesVO;

    public Occupation getOccupationById(int id) {

        if (!occupationsVO.getOccupations().containsKey(id)) {
            throw new NonExistOccupationException("[ERROR] 존재하지 않는 직군입니다.");
        }

        return occupationsVO.getOccupations().get(id);
    }

    public Scope getScopeById(int id) {

        if (!scopesVO.getScopes().containsKey(id)) {
            throw new NonExistScopeException("[ERROR] 존재하지 않는 공개범위입니다.");
        }

        return scopesVO.getScopes().get(id);
    }

}
