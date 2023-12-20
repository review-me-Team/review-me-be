package reviewme.be.util.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Scope {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String scope;
}
