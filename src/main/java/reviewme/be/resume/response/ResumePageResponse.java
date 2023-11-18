package reviewme.be.resume.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ResumePageResponse {

    private List<ResumeResponse> resumePage;
}
