package reviewme.be.resume.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ResumeSearchConditionParam {
    
    private Integer occupation;
    private Integer startYear;
    private Integer endYear;
}
