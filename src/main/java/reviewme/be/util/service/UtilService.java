package reviewme.be.util.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reviewme.be.util.entity.Occupation;
import reviewme.be.util.entity.Scope;
import reviewme.be.util.exception.NonExistOccupationException;
import reviewme.be.util.exception.NonExistScopeException;
import reviewme.be.util.repository.OccupationRepository;
import reviewme.be.util.repository.ScopeRepository;

@Service
@RequiredArgsConstructor
public class UtilService {

    private final OccupationRepository occupationRepository;
    private final ScopeRepository scopeRepository;

    public Occupation getOccupationById(long id) {

        return occupationRepository.findById(id)
                .orElseThrow(() -> new NonExistOccupationException("[ERROR] 존재하지 않는 직군입니다."));
    }

    public Scope getScopeById(long id) {

        return scopeRepository.findById(id)
            .orElseThrow(() -> new NonExistScopeException("[ERROR] 존재하지 않는 공개범위입니다."));
    }

}
