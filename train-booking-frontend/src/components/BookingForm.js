// src/components/BookingForm.js

import React, { useState, useEffect } from "react";
import { useLocation, useParams } from "react-router-dom";
import axios from "axios";
import { BASE_URL } from "../config";
import "./BookingForm.css";

function loadRazorpayScript() {
  return new Promise((resolve) => {
    const script = document.createElement("script");
    script.src = "https://checkout.razorpay.com/v1/checkout.js";
    script.onload = () => resolve(true);
    script.onerror = () => resolve(false);
    document.body.appendChild(script);
  });
}

// Helper function to format date as YYYY-MM-DD
const formatDate = (dateString) => {
  const date = new Date(dateString);
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, "0");
  const day = String(date.getDate()).padStart(2, "0");
  return `${year}-${month}-${day}`;
};

function BookingForm() {
  const location = useLocation();
  const { id } = useParams();

  const selectedTrain = location.state?.train || null;
  const fromStation = location.state?.fromStation || null;
  const toStation = location.state?.toStation || null;

  const [train, setTrain] = useState(selectedTrain);
  const [loading, setLoading] = useState(false);
  const [fetchingTrain, setFetchingTrain] = useState(!selectedTrain);

  const [formData, setFormData] = useState({
    passengerName: "",
    date: ""
  });

  // Fetch train details if not passed via state
  useEffect(() => {
    if (!selectedTrain) {
      axios
        .get(`${BASE_URL}/trains/${id}`)
        .then((res) => setTrain(res.data))
        .catch((err) => {
          console.error("Failed to fetch train:", err);
          alert("Train data not found!");
        })
        .finally(() => setFetchingTrain(false));
    } else {
      setFetchingTrain(false);
    }
  }, [id, selectedTrain]);

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handlePayment = async () => {
    if (!formData.passengerName || !formData.date) {
      alert("Please fill passenger name and date before payment!");
      return;
    }

    const loggedInUserId = localStorage.getItem("userId"); // Get logged-in user ID
    if (!loggedInUserId) {
      alert("❌ Please login to book a ticket!");
      return;
    }

    const res = await loadRazorpayScript();
    if (!res) {
      alert("Razorpay SDK failed to load. Are you online?");
      return;
    }

    const options = {
      key: "rzp_test_RGJCrSCstDxkaH", // Replace with your Razorpay key
      amount: 50000, // ₹500 in paise
      currency: "INR",
      name: "Train Reservation",
      description: `Ticket for ${train.name}`,
      handler: async function (response) {
        alert("Payment Successful! Payment ID: " + response.razorpay_payment_id);
        await handleBooking(response.razorpay_payment_id);
      },
      prefill: {
        name: formData.passengerName,
        email: "user@example.com",
        contact: "9999999999",
      },
      theme: { color: "#3399cc" },
    };

    const paymentObject = new window.Razorpay(options);
    paymentObject.open();
  };

  const handleBooking = async (paymentId) => {
    if (!train) return;

    setLoading(true);
    try {
      const loggedInUserId = localStorage.getItem("userId");
      if (!loggedInUserId) {
        alert("❌ User not logged in!");
        setLoading(false);
        return;
      }

      const query = new URLSearchParams({
        trainId: train.id,
        userId: loggedInUserId, // ✅ send logged-in user ID
        passengerName: formData.passengerName,
        fromStation: fromStation || train.stops[0],
        toStation: toStation || train.stops[train.stops.length - 1],
        date: formatDate(formData.date),
        time: train.departureTime || "10:00",
      }).toString();

      const url = `${BASE_URL}/booking/create?${query}`;
      const response = await axios.post(url);

      alert(
        `✅ Booking Successful!\n` +
        `PNR: ${response.data.pnr || "N/A"}\n` +
        `Seat: ${response.data.seatNumber || "N/A"}\n` +
        `Coach: ${response.data.coachName || "N/A"}`
      );

      setFormData({ passengerName: "", date: "" });
    } catch (error) {
      console.error("Booking failed:", error.response?.data || error.message);
      alert("❌ Booking Failed. Please check console for details or try again later.");
    } finally {
      setLoading(false);
    }
  };

  if (fetchingTrain) return <p>Loading train details...</p>;
  if (!train) return <p>Train not found!</p>;

  return (
    <div className="booking-container">
      <h2>Book Ticket for {train.name}</h2>
      <form className="booking-form" onSubmit={(e) => e.preventDefault()}>
        <input
          type="text"
          name="passengerName"
          placeholder="Passenger Name"
          value={formData.passengerName}
          onChange={handleChange}
          required
        />
        <input
          type="date"
          name="date"
          value={formData.date}
          onChange={handleChange}
          required
        />
        <button type="button" disabled={loading} onClick={handlePayment}>
          {loading ? "Processing..." : "Pay & Book"}
        </button>
      </form>

      <div className="train-info">
        <p><strong>Train ID:</strong> {train.id}</p>
        <p><strong>From:</strong> {fromStation || train.stops[0]}</p>
        <p><strong>To:</strong> {toStation || train.stops[train.stops.length - 1]}</p>
        <p><strong>Time:</strong> {train.departureTime || "10:00"}</p>
      </div>
    </div>
  );
}

export default BookingForm;
