package domain.entity;

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
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn
public abstract class Worker {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "WORKER_ID")
    private Long id;
    private String name;
    private LocalDate birthday;
    @OneToMany(mappedBy = "worker")
    private List<MovieWorker> movieWorkers = new ArrayList<MovieWorker>();

    public void addMovieWorker(MovieWorker movieWorker) {
        if (movieWorker != null && movieWorker.getWorker() != this)
            movieWorker.setWorker(this);
    }

    public Worker(String name, LocalDate birthday) {
        this.name = name;
        this.birthday = birthday;
    }
}
