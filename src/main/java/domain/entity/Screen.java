package domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Screen {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SCREEN_ID")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "THEATER_ID")
    private Theater theater;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MOVIE_ID")
    private Movie movie;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    @OneToMany(mappedBy = "screen")
    private List<Ticket> tickets = new ArrayList<Ticket>();

    public void setTheater(Theater theater)
    {
        if (this.theater != null)
            this.theater.getScreens().remove(this);
        if (theater != null)
            theater.getScreens().add(this);
        this.theater = theater;
    }

    public void setMovie(Movie movie)
    {
        if (this.movie != null)
            this.movie.getScreens().remove(this);
        if (movie == null) {
            this.movie = null;
            return;
        }
        movie.getScreens().add(this);
        this.movie = movie;

        endTime = startTime.plusMinutes(movie.getRunningTime());
    }

    public void addTicket(Ticket ticket) {
        if (ticket != null && ticket.getScreen() != this)
            ticket.setScreen(this);
    }

    public Screen(LocalDateTime startTime) {
        this.startTime = startTime;
    }
}
