package reviewme.be.resume.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import reviewme.be.resume.dto.ResumeSearchCondition;
import reviewme.be.resume.dto.response.MyResumeResponse;
import reviewme.be.resume.dto.response.QMyResumeResponse;
import reviewme.be.resume.dto.response.QResumeResponse;
import reviewme.be.resume.dto.response.ResumeResponse;

import java.util.List;

import static reviewme.be.resume.entity.QResume.resume;

@RequiredArgsConstructor
public class ResumeRepositoryImpl implements ResumeRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<ResumeResponse> findResumes(ResumeSearchCondition searchCondition, Pageable pageable) {

        QueryResults<ResumeResponse> results = queryFactory
                .select(new QResumeResponse(
                        resume.id,
                        resume.title,
                        resume.writer.id,
                        resume.writer.name,
                        resume.writer.profileUrl,
                        resume.createdAt,
                        resume.scope.scope,
                        resume.occupation.occupation,
                        resume.year
                ))
                .from(resume)
                .leftJoin(resume.writer)
                .leftJoin(resume.scope)
                .leftJoin(resume.occupation)
                .where(
                        scopeIdEq(searchCondition.getScope()),
                        occupationEq(searchCondition.getOccupation()),
                        yearGoe(searchCondition.getStartYear()),
                        yearLoe(searchCondition.getEndYear()),
                        resume.deletedAt.isNull()
                )
                .orderBy(resume.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<ResumeResponse> content = results.getResults();
        long total = results.getTotal();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<MyResumeResponse> findResumesByWriterId(Pageable pageable, long userId) {

        QueryResults<MyResumeResponse> results = queryFactory
                .select(new QMyResumeResponse(
                        resume.id,
                        resume.title,
                        resume.createdAt,
                        resume.scope.scope,
                        resume.occupation.occupation,
                        resume.year
                ))
                .from(resume)
                .leftJoin(resume.writer)
                .leftJoin(resume.scope)
                .leftJoin(resume.occupation)
                .where(
                        resume.writer.id.eq(userId),
                        resume.deletedAt.isNull()
                )
                .orderBy(resume.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<MyResumeResponse> content = results.getResults();
        long total = results.getTotal();

        return new PageImpl<>(content, pageable, total);
    }

    private BooleanExpression scopeIdEq(Integer scopeId) {

        return scopeId != null ? resume.scope.id.loe(scopeId) : null;
    }

    private BooleanExpression occupationEq(Integer occupationId) {

        return occupationId != null ? resume.occupation.id.eq(occupationId) : null;
    }

    private BooleanExpression yearGoe(Integer startYear) {

        return startYear != null ? resume.year.goe(startYear) : null;
    }

    private BooleanExpression yearLoe(Integer endYear) {

        return endYear != null ? resume.year.loe(endYear) : null;
    }
}
