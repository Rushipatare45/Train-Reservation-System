import React, { useEffect, useState } from "react";
import axios from "axios";
import "./MyTicketPage.css";

function MyTicketPage() {
  const [bookings, setBookings] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  // ✅ Get logged-in user ID from localStorage
  const userId = localStorage.getItem("userId");

  useEffect(() => {
    const fetchBookings = async () => {
      if (!userId) {
        setError("❌ User not logged in");
        setLoading(false);
        return;
      }
      try {
        const res = await axios.get(`http://localhost:8080/booking/my?userId=${userId}`);
        setBookings(res.data);
      } catch (err) {
        setError("❌ Failed to fetch your bookings");
      } finally {
        setLoading(false);
      }
    };
    fetchBookings();
  }, [userId]);

  // Format date as "14 Sep 2025"
  const formatDate = (dateString) => {
    const date = new Date(dateString);
    return new Intl.DateTimeFormat("en-GB", {
      day: "2-digit",
      month: "short",
      year: "numeric"
    }).format(date);
  };

  if (loading) return <p>Loading your bookings...</p>;
  if (error) return <p>{error}</p>;

  return (
    <div className="ticket-wrapper">
      {bookings.length === 0 ? (
        <p>No bookings yet.</p>
      ) : (
        bookings.map((b) => (
          <div key={b.id} className="ticket-card">
            <div className="ticket-header">
              <h3>{b.train.name}</h3>
              <p>PNR: {b.pnr}</p>
              <p className="ticket-date">{formatDate(b.date)}</p>
            </div>

            <div className="ticket-journey">
              <div className="station">
                <h4>{b.fromStation}</h4>
                <p>Departure: {b.time}</p>
              </div>
              <div className="duration">→</div>
              <div className="station">
                <h4>{b.toStation}</h4>
                <p>Arrival: {b.arrivalTime}</p>
              </div>
            </div>

            <div className="ticket-passengers">
              <div className="passenger-header">
                <div>Passenger</div>
                <div>Coach</div>
                <div>Seat</div>
                <div>Fare</div>
              </div>
              <div className="passenger-info">
                <div>{b.passengerName}</div>
                <div>{b.coachName}</div>
                <div>{b.seatNumber}</div>
                <div>₹{b.fare}</div>
              </div>
            </div>

            <div className="ticket-action">
              <button>Download Ticket</button>
            </div>
          </div>
        ))
      )}
    </div>
  );
}

export default MyTicketPage;
