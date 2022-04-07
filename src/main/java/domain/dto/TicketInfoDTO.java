package domain.dto;

import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@ToString
public class TicketInfoDTO {
    private String movieName;
    private String theaterName;
    private LocalDateTime startTime;
    private List<SeatDTO> seats;
    private Boolean isCanceled;

    public void setSeats(List<SeatDTO> seats) {
        this.seats = seats;
    }
}
