package com.example.Reservation_System.controller;

import com.example.Reservation_System.model.Booking;
import com.example.Reservation_System.model.Coach;
import com.example.Reservation_System.model.Train;
import com.example.Reservation_System.model.User;
import com.example.Reservation_System.repository.BookingRepository;
import com.example.Reservation_System.repository.TrainRepository;
import com.example.Reservation_System.repository.UserRepository;
import com.example.Reservation_System.utils.FareCalculator;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.io.ByteArrayOutputStream;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;



@RestController
@RequestMapping("/booking")
@CrossOrigin(origins = "http://localhost:3000")
public class BookingController {

    private final TrainRepository trainRepository;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;

    public BookingController(TrainRepository trainRepository,
                             BookingRepository bookingRepository,
                             UserRepository userRepository) {
        this.trainRepository = trainRepository;
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
    }

    // ---------------- Create Booking ----------------
    @PostMapping("/create")
    @Transactional
    public Booking createBooking(@RequestParam Long trainId,
                                 @RequestParam Long userId,
                                 @RequestParam String passengerName,
                                 @RequestParam String fromStation,
                                 @RequestParam String toStation,
                                 @RequestParam String date,
                                 @RequestParam String time) {

        // Validate date
        LocalDate travelDate;
        try {
            travelDate = LocalDate.parse(date);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format. Use YYYY-MM-DD");
        }

        // Validate time
        LocalTime travelTime;
        try {
            travelTime = LocalTime.parse(time);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid time format. Use HH:mm");
        }

        // Fetch train
        Train train = trainRepository.findById(trainId)
                .orElseThrow(() -> new RuntimeException("Train not found"));

        // Fetch user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Validate stations
        List<String> stops = train.getStops();
        int fromIndex = stops.indexOf(fromStation);
        int toIndex = stops.indexOf(toStation);
        if (fromIndex == -1 || toIndex == -1 || fromIndex >= toIndex) {
            throw new RuntimeException("Invalid station selection!");
        }

        // Mark existing booked segments
        List<Booking> existingBookings = bookingRepository.findByTrainIdAndDate(trainId, date);
        for (Booking b : existingBookings) {
            Coach coach = train.getCoaches().stream()
                    .filter(c -> c.getName().equals(b.getCoachName()))
                    .findFirst().orElse(null);
            if (coach != null) {
                coach.initializeSeatsIfNeeded();
                String key = "S" + b.getSeatNumber();
                List<Coach.Segment> bookedSegments = coach.getSeatSegments()
                        .computeIfAbsent(key, k -> new ArrayList<>());
                bookedSegments.add(new Coach.Segment(b.getFromStation(), b.getToStation()));
            }
        }

        // Find first available seat
        for (Coach coach : train.getCoaches()) {
            coach.initializeSeatsIfNeeded();
            int totalSeats = coach.getSeats().size();

            for (int seatNumber = 1; seatNumber <= totalSeats; seatNumber++) {
                String seatKey = "S" + seatNumber;
                boolean isAvailable = true;
                List<Coach.Segment> bookedSegments = coach.getSeatSegments()
                        .computeIfAbsent(seatKey, k -> new ArrayList<>());

                for (Coach.Segment segment : bookedSegments) {
                    int bookedFrom = stops.indexOf(segment.getFromStation());
                    int bookedTo = stops.indexOf(segment.getToStation());
                    if (!(toIndex <= bookedFrom || fromIndex >= bookedTo)) {
                        isAvailable = false;
                        break;
                    }
                }

                if (isAvailable) {
                    int fare = FareCalculator.calculateFare(stops, fromStation, toStation);
                    int travelMinutes = (toIndex - fromIndex) * 45;
                    LocalTime arrivalTime = travelTime.plusMinutes(travelMinutes);

                    Booking booking = new Booking();
                    booking.setTrain(train);
                    booking.setPassengerName(passengerName);
                    booking.setSeatNumber(seatNumber);
                    booking.setCoachName(coach.getName());
                    booking.setFromStation(fromStation);
                    booking.setToStation(toStation);
                    booking.setDate(date);
                    booking.setTime(time);
                    booking.setFare(fare);
                    booking.setArrivalTime(arrivalTime.toString());
                    booking.setPnr(generatePNR(train.getId(), seatNumber));
                    booking.setUser(user);

                    Booking savedBooking = bookingRepository.save(booking);

                    bookedSegments.add(new Coach.Segment(fromStation, toStation));
                    coach.getSeatSegments().put(seatKey, bookedSegments);
                    trainRepository.save(train);

                    return savedBooking;
                }
            }
        }

        throw new RuntimeException("No seats available!");
    }

    // ---------------- User-specific bookings ----------------
    @GetMapping("/my")
    public List<Booking> getMyBookings(@RequestParam Long userId) {
        return bookingRepository.findByUserId(userId);
    }

    // ---------------- Download Ticket by ID ----------------
    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> downloadTicket(@PathVariable Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found with ID: " + id));

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            Document document = new Document();
            PdfWriter.getInstance(document, out);
            document.open();

            document.add(new Paragraph("----- Train Ticket -----"));
            document.add(new Paragraph("PNR: " + booking.getPnr()));
            document.add(new Paragraph("Passenger: " + booking.getPassengerName()));
            document.add(new Paragraph("Train: " + booking.getTrain().getName()));
            document.add(new Paragraph("Coach: " + booking.getCoachName()));
            document.add(new Paragraph("Seat: " + booking.getSeatNumber()));
            document.add(new Paragraph("From: " + booking.getFromStation()));
            document.add(new Paragraph("To: " + booking.getToStation()));
            document.add(new Paragraph("Date: " + booking.getDate()));
            document.add(new Paragraph("Departure: " + booking.getTime()));
            document.add(new Paragraph("Arrival: " + booking.getArrivalTime()));
            document.add(new Paragraph("Fare: ₹" + booking.getFare()));

            document.close();

            byte[] pdfBytes = out.toByteArray();

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=ticket_" + id + ".pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdfBytes);

        } catch (DocumentException e) {
            throw new RuntimeException("Error generating ticket PDF", e);
        }
    }

    // ---------------- PNR Generator ----------------
    private String generatePNR(Long trainId, int seatNumber) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return "T" + trainId + "S" + seatNumber + sb.toString();
    }
}
