package domain.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class TicketSeat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TICKET_SEAT_ID")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SEAT_ID")
    private Seat seat;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TICKET_ID")
    private Ticket ticket;

    public void setSeat(Seat seat)
    {
        if (this.seat != null)
            this.seat.getTicketSeats().remove(this);
        if (seat != null)
            seat.getTicketSeats().add(this);
        this.seat = seat;
    }

    public void setTicket(Ticket ticket)
    {
        if (this.ticket != null)
            this.ticket.getTicketSeats().remove(this);
        if (ticket != null)
            ticket.getTicketSeats().add(this);
        this.ticket = ticket;
    }
}
