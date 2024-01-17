package reviewme.be.friend.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import reviewme.be.user.dto.QUserResponse;
import reviewme.be.user.dto.UserResponse;

import java.util.List;

import static reviewme.be.friend.entity.QFriend.friend;
import static reviewme.be.resume.entity.QResume.resume;
import static reviewme.be.user.entity.QUser.user;

@RequiredArgsConstructor
public class FriendRepositoryImpl implements FriendRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<UserResponse> findFriendsByUserId(long userId, boolean accpeted, Pageable pageable) {

        // order by name asc
        QueryResults<UserResponse> results = queryFactory
                .select(new QUserResponse(
                        friend.followerUser.id,
                        friend.followerUser.name,
                        friend.followerUser.profileUrl
                ))
                .from(friend)
                .leftJoin(friend.followingUser, user)
                .where(
                        followingUserEq(userId),
                        friend.accepted.eq(accpeted)
                )
                .orderBy(friend.followerUser.name.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<UserResponse> content = results.getResults();
        long total = results.getTotal();

        return new PageImpl<>(content, pageable, total);
    }

    private BooleanExpression followingUserEq(Long userId) {

        return userId != null ? friend.followingUser.id.eq(userId) : null;
    }
}
