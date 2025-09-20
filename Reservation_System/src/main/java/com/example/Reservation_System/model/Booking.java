package com.example.Reservation_System.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonIgnoreProperties({"coaches"})
    private Train train;

    private String passengerName;
    private int seatNumber;
    private String coachName;
    private String fromStation;
    private String toStation;
    private String date;  // yyyy-MM-dd
    private String time;  // HH:mm
    private int fare;
    private String pnr;
    private String arrivalTime;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    public Booking() {}

    public Booking(Train train, String passengerName, int seatNumber, String coachName,
                   String fromStation, String toStation, User user) {
        this.train = train;
        this.passengerName = passengerName;
        this.seatNumber = seatNumber;
        this.coachName = coachName;
        this.fromStation = fromStation;
        this.toStation = toStation;
        this.user = user;
    }

    // --- Getters & Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Train getTrain() { return train; }
    public void setTrain(Train train) { this.train = train; }

    public String getPassengerName() { return passengerName; }
    public void setPassengerName(String passengerName) { this.passengerName = passengerName; }

    public int getSeatNumber() { return seatNumber; }
    public void setSeatNumber(int seatNumber) { this.seatNumber = seatNumber; }

    public String getCoachName() { return coachName; }
    public void setCoachName(String coachName) { this.coachName = coachName; }

    public String getFromStation() { return fromStation; }
    public void setFromStation(String fromStation) { this.fromStation = fromStation; }

    public String getToStation() { return toStation; }
    public void setToStation(String toStation) { this.toStation = toStation; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }

    public int getFare() { return fare; }
    public void setFare(int fare) { this.fare = fare; }

    public String getPnr() { return pnr; }
    public void setPnr(String pnr) { this.pnr = pnr; }

    public String getArrivalTime() { return arrivalTime; }
    public void setArrivalTime(String arrivalTime) { this.arrivalTime = arrivalTime; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    @Override
    public String toString() {
        return "Booking{" +
                "id=" + id +
                ", train=" + (train != null ? train.getName() : "N/A") +
                ", passengerName='" + passengerName + '\'' +
                ", seatNumber=" + seatNumber +
                ", coachName='" + coachName + '\'' +
                ", fromStation='" + fromStation + '\'' +
                ", toStation='" + toStation + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", fare=" + fare +
                ", pnr='" + pnr + '\'' +
                ", arrivalTime='" + arrivalTime + '\'' +
                ", user=" + (user != null ? user.getName() : "N/A") +
                '}';
    }
}
