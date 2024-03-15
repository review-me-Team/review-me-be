package reviewme.be.user.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import reviewme.be.user.dto.response.QUserResponse;
import reviewme.be.user.dto.response.UserResponse;

import java.util.List;

import static reviewme.be.friend.entity.QFriend.friend;
import static reviewme.be.user.entity.QUser.user;

@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<UserResponse> findUsersByStartName(String name, Pageable pageable) {

        JPAQuery<Long> followingIds = queryFactory
            .select(friend.followerUser.id)
            .from(friend)
            .where(friend.followingUser.id.eq(user.id));

        JPAQuery<Long> follwerIds = queryFactory
            .select(friend.followingUser.id)
            .from(friend)
            .where(friend.followerUser.id.eq(user.id));

        QueryResults<UserResponse> results = queryFactory
            .select(new QUserResponse(
                user.id,
                user.name,
                user.profileUrl
            ))
            .from(user)
            .where(
                user.name.startsWith(name)
                    .and(user.id.notIn(followingIds))
                    .and(user.id.notIn(follwerIds))
            )
            .orderBy(user.name.asc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetchResults();

        List<UserResponse> content = results.getResults();
        long total = results.getTotal();

        return new PageImpl<>(content, pageable, total);
    }
}
