package domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@NoArgsConstructor
@DiscriminatorValue("A")
public class Actor extends Worker {
    private Double height;
    private String instagramId;

    public Actor(String name, LocalDate birthday, Double height, String instagramId) {
        super(name, birthday);
        this.height = height;
        this.instagramId = instagramId;
    }
}
