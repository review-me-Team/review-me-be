package reviewme.be.friend.entity;

import lombok.*;
import reviewme.be.user.entity.User;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Friend {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "follower_id")
    private User followerUser;

    @OneToOne
    @JoinColumn(name = "following_id")
    private User followingUser;

    private Boolean accepted;

    public static Friend newRequest(User followerUser, User followingUser) {

        return Friend.builder()
                .followerUser(followerUser)
                .followingUser(followingUser)
                .accepted(false)
                .build();
    }

    public static Friend newRelation(User followerUser, User followingUser) {

        return Friend.builder()
                .followerUser(followerUser)
                .followingUser(followingUser)
                .accepted(true)
                .build();
    }

    public void accept() {

        this.accepted = true;
    }

}
