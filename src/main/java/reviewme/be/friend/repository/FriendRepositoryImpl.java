package reviewme.be.friend.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FriendRepositoryImpl {

    private final JPAQueryFactory queryFactory;
}
