package reviewme.be.resume.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ResumeSearchCondition {

    private Integer scope;
    private Integer occupation;
    private Integer year;
}
