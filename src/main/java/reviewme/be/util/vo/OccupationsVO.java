package reviewme.be.util.vo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reviewme.be.util.entity.Occupation;
import reviewme.be.util.repository.OccupationRepository;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Component
@RequiredArgsConstructor
public class OccupationsVO {

    private final OccupationRepository occupationRepository;

    private Map<Integer, Occupation> occupations;
    private List<Occupation> occupationList;

    @PostConstruct
    public void init() {

        occupationList = occupationRepository.findAll();

        occupations = occupationList.stream()
                .collect(
                        HashMap::new,
                        (map, occupation) -> map.put(occupation.getId(), occupation),
                        HashMap::putAll
                );
    }
}
