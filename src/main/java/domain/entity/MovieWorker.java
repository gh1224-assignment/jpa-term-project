package domain.entity;

import domain.RoleType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class MovieWorker {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MOVIE_WORKER_ID")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MOVIE_ID")
    private Movie movie;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "WORKER_ID")
    private Worker worker;
    @Enumerated(EnumType.STRING)
    private RoleType roleType;

    public void setMovie(Movie movie)
    {
        if (this.movie != null)
            this.movie.getMovieWorkers().remove(this);
        if (movie != null)
            movie.getMovieWorkers().add(this);
        this.movie = movie;
    }

    public void setWorker(Worker worker)
    {
        if (this.worker != null)
            this.worker.getMovieWorkers().remove(this);
        if (worker != null)
            worker.getMovieWorkers().add(this);
        this.worker = worker;
    }

    public MovieWorker(RoleType roleType) {
        this.roleType = roleType;
    }

    @Override
    public String toString() {
        return "{name=" + worker.getName() +
                ", roleType=" + roleType +
                '}';
    }
}
