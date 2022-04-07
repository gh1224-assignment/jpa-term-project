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
@DiscriminatorValue("D")
public class Director extends Worker {
    private String birthPlace;

    public Director(String name, LocalDate birthday, String birthPlace) {
        super(name, birthday);
        this.birthPlace = birthPlace;
    }
}
