package domain.dto;

import java.time.LocalDateTime;
import java.util.List;

public class ScreenInfoDTO {
    private String movieName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private List<SeatDTO> seats;

    public void setSeats(List<SeatDTO> seats) {
        this.seats = seats;
    }

    @Override
    public String toString() {
        String str = "ScreenInfoDTO{" +
                "movieName='" + movieName + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", totalSeats=" + seats +
                ", availableSeats=[";

        for (SeatDTO seat: seats) {
            if (seat.getCanTicketing())
                str += seat + ", ";
        }

        if (str.charAt(str.length() - 2) == ',') {
            str = str.substring(0, str.length() - 2);
        }

        return  str + "]}";
    }
}
