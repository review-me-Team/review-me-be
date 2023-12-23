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
    private long id;

    @ManyToOne
    @JoinColumn(name = "resume_id")
    private Resume resume;

    private String content;
}
