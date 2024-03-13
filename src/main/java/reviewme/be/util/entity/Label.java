package reviewme.be.util.entity;

import lombok.*;
import reviewme.be.resume.entity.Resume;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Label {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    public static Label ofCreated(String content) {

        return Label.builder()
            .content(content)
            .build();
    }
}
