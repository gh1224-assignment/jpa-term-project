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
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SEAT_ID")
    private Long id;
    @Column(name = "SEAT_ROW")
    private Character row;
    @Column(name = "SEAT_COL")
    private Integer column;
    private Boolean status;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "THEATER_ID")
    private Theater theater;
    @OneToMany(mappedBy = "seat")
    private List<TicketSeat> ticketSeats = new ArrayList<TicketSeat>();

    public void setTheater(Theater theater)
    {
        if (this.theater != null)
            this.theater.getSeats().remove(this);
        if (theater != null)
            theater.getSeats().add(this);
        this.theater = theater;
    }

    public void addTicketSeat(TicketSeat ticketSeat) {
        if (ticketSeat != null && ticketSeat.getSeat() != this)
            ticketSeat.setSeat(this);
    }

    public Seat(Character row, Integer column, Boolean status) {
        this.row = row;
        this.column = column;
        this.status = status;
    }
}