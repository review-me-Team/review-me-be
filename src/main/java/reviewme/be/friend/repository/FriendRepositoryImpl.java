package reviewme.be.friend.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import reviewme.be.friend.entity.Friend;
import reviewme.be.user.dto.response.QUserResponse;
import reviewme.be.user.dto.response.UserResponse;

import java.util.List;

import static reviewme.be.friend.entity.QFriend.friend;
import static reviewme.be.user.entity.QUser.user;

@RequiredArgsConstructor
public class FriendRepositoryImpl implements FriendRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<UserResponse> findFriendsByUserId(long userId, String start,
        boolean accpeted,
        Pageable pageable) {

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
                startNameEqInFriends(start),
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

    @Override
    public Page<UserResponse> findSentFriendRequests(long followerId, String start, Pageable pageable) {

        QueryResults<UserResponse> results = queryFactory.select(new QUserResponse(
                friend.followingUser.id,
                friend.followingUser.name,
                friend.followingUser.profileUrl
            ))
            .from(friend)
            .leftJoin(friend.followerUser, user)
            .where(
                followerUserEq(followerId),
                startNameEqInSentFriendRequests(start),
                friend.accepted.eq(false)
            )
            .orderBy(friend.followingUser.name.asc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetchResults();

        List<UserResponse> content = results.getResults();
        long total = results.getTotal();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public List<Friend> findFriendRelation(long followerUserId, long followingUserId) {

        return queryFactory
            .selectFrom(friend)
            .where(
                (friend.followerUser.id.eq(followerUserId)
                    .and(friend.followingUser.id.eq(followingUserId)))
                    .or(friend.followerUser.id.eq(followingUserId)
                        .and(friend.followingUser.id.eq(followerUserId)))
            )
            .fetch();
    }

    private BooleanExpression followingUserEq(Long userId) {

        return userId != null ? friend.followingUser.id.eq(userId) : null;
    }

    private BooleanExpression followerUserEq(Long userId) {

        return userId != null ? friend.followerUser.id.eq(userId) : null;
    }

    private BooleanExpression startNameEqInFriends(String start) {

        return start != null ? friend.followerUser.name.startsWith(start) : null;
    }

    private BooleanExpression startNameEqInSentFriendRequests(String start) {

        return start != null ? friend.followingUser.name.startsWith(start) : null;
    }
}
