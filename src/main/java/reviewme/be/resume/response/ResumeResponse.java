package reviewme.be.resume.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ResumeResponse {

    private long id;
    private String title;
    private String writer;
    private LocalDateTime createdAt;
    private String scope;
}
