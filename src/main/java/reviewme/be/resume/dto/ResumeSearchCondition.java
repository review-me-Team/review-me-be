package reviewme.be.resume.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import reviewme.be.util.entity.Occupation;
import reviewme.be.util.entity.Scope;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ResumeSearchCondition {

    private List<Scope> scopes;
    private Occupation occupation;
    private String year;
}
