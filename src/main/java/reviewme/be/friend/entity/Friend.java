package reviewme.be.friend.entity;

import lombok.*;
import reviewme.be.util.entity.User;

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
}
