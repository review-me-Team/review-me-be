package reviewme.be.util.vo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reviewme.be.util.entity.Label;
import reviewme.be.util.entity.Scope;
import reviewme.be.util.repository.LabelRepository;
import reviewme.be.util.repository.ScopeRepository;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Component
@RequiredArgsConstructor
public class ScopesVO {

    private final ScopeRepository scopeRepository;

    private Map<Integer, String> scopes;
    private List<Scope> scopeList;

    @PostConstruct
    public void init() {

        scopeList = scopeRepository.findAll();

        scopes = scopeList.stream()
                .collect(
                        HashMap::new,
                        (map, scope) -> map.put(scope.getId(), scope.getScope()),
                        HashMap::putAll
                );
    }
}
