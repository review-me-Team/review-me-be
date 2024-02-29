package reviewme.be.resume.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import reviewme.be.resume.dto.ResumeSearchCondition;
import reviewme.be.resume.dto.response.MyResumeResponse;
import reviewme.be.resume.dto.response.ResumeResponse;

public interface ResumeRepositoryCustom {

    Page<ResumeResponse> findResumes(ResumeSearchCondition searchCondition, Pageable pageable);

    Page<MyResumeResponse> findResumesByWriterId(Pageable pageable, long userId);
}
