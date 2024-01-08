package reviewme.be.util.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reviewme.be.util.entity.Scope;
import reviewme.be.util.exception.NonExistScopeException;
import reviewme.be.util.repository.ScopeRepository;

@Service
@RequiredArgsConstructor
public class UtilService {

    private final ScopeRepository scopeRepository;

    public Scope getScopeById(long id) {

        return scopeRepository.findById(id)
            .orElseThrow(() -> new NonExistScopeException("[ERROR] 존재하지 않는 공개범위입니다."));
    }
}
