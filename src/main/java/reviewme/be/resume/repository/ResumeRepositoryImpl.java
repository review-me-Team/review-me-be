package reviewme.be.resume.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import reviewme.be.resume.dto.ResumeSearchCondition;
import reviewme.be.resume.dto.response.QResumeResponse;
import reviewme.be.resume.dto.response.ResumeResponse;

import java.util.List;

import static reviewme.be.resume.entity.QResume.resume;

@RequiredArgsConstructor
public class ResumeRepositoryImpl implements ResumeRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<ResumeResponse> findAllByDeletedAtIsNull(ResumeSearchCondition searchCondition, Pageable pageable) {

        QueryResults<ResumeResponse> results = queryFactory
                .select(new QResumeResponse(
                        resume.id,
                        resume.title,
                        resume.user.id,
                        resume.user.name,
                        resume.user.profileUrl,
                        resume.createdAt,
                        resume.scope.scope,
                        resume.occupation.occupation,
                        resume.year
                ))
                .from(resume)
                .leftJoin(resume.user)
                .leftJoin(resume.scope)
                .leftJoin(resume.occupation)
                .where(
                        scopeIdEq(searchCondition.getScope()),
                        occupationEq(searchCondition.getOccupation()),
                        yearEq(searchCondition.getYear()))
                .orderBy(resume.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<ResumeResponse> content = results.getResults();
        long total = results.getTotal();

        return new PageImpl<>(content, pageable, total);
    }

    private BooleanExpression scopeIdEq(Integer scopeId) {

        return scopeId != null ? resume.scope.id.loe(scopeId) : null;
    }

    private BooleanExpression occupationEq(Integer occupationId) {

        return occupationId != null ? resume.occupation.id.eq(Long.valueOf(occupationId)) : null;
    }

    private BooleanExpression yearEq(Integer year) {

        return year != null ? resume.year.eq(year) : null;
    }
}