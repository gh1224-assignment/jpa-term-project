package domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Theater {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "THEATER_ID")
    private Long id;
    private String name;
    private Integer floor;
    @OneToMany(mappedBy = "theater")
    private List<Screen> screens = new ArrayList<Screen>();
    @OneToMany(mappedBy = "theater")
    private List<Seat> seats = new ArrayList<Seat>();

    public void addScreen(Screen screen) {
        if (screen != null && screen.getTheater() != this)
            screen.setTheater(this);
    }

    public void addSeat(Seat seat) {
        if (seat != null && seat.getTheater() != this)
            seat.setTheater(this);
    }

    public Theater(String name, Integer floor) {
        this.name = name;
        this.floor = floor;
    }
}
