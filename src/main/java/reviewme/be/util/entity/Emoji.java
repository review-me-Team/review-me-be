package reviewme.be.util.entity;

import lombok.AccessLevel;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Emoji {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String emoji;
}
