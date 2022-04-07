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
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TICKET_ID")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SCREEN_ID")
    private Screen screen;
    private Boolean isCanceled = false;
    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL)
    private List<TicketSeat> ticketSeats = new ArrayList<TicketSeat>();

    public void setUser(User user)
    {
        if (this.user != null)
            this.user.getTickets().remove(this);
        if (user != null)
            user.getTickets().add(this);
        this.user = user;
    }

    public void setScreen(Screen screen)
    {
        if (this.screen != null)
            this.screen.getTickets().remove(this);
        if (screen != null)
            screen.getTickets().add(this);
        this.screen = screen;
    }

    public void addTicketSeat(TicketSeat ticketSeat) {
        if (ticketSeat != null && ticketSeat.getTicket() != this)
            ticketSeat.setTicket(this);
    }
}
