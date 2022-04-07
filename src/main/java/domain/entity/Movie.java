package domain.entity;

import domain.Genre;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Movie extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MOVIE_ID")
    private Long id;
    private String name;
    private LocalDate openingDate;
    @Enumerated(EnumType.STRING)
    private Genre genre;
    private Integer runningTime;
    @OneToMany(mappedBy = "movie")
    private List<MovieWorker> movieWorkers = new ArrayList<MovieWorker>();
    @OneToMany(mappedBy = "movie")
    private List<Screen> screens = new ArrayList<Screen>();

    public void addMovieWorker(MovieWorker movieWorker) {
        if (movieWorker != null && movieWorker.getMovie() != this)
            movieWorker.setMovie(this);
    }

    public void addScreen(Screen screen) {
        if (screen != null && screen.getMovie() != this)
            screen.setMovie(this);
    }

    public Movie(String name, LocalDate openingDate, Genre genre, Integer runningTime) {
        this.name = name;
        this.openingDate = openingDate;
        this.genre = genre;
        this.runningTime = runningTime;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "name='" + name + '\'' +
                ", openingDate=" + openingDate +
                ", genre=" + genre +
                ", runningTime=" + runningTime +
                ", movieWorkers=" + movieWorkers +
                '}';
    }
}
