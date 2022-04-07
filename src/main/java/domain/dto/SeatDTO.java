package domain.dto;

public class SeatDTO {
    private Character seatRow;
    private Integer seatCol;
    private Boolean canTicketing;

    @Override
    public String toString() {
        return "SeatDTO{" + seatRow + seatCol + '}';
    }

    public Boolean getCanTicketing() {
        return canTicketing;
    }
}
