// src/components/HomePage.js
import React, { useState } from "react";
import { FaTrain, FaFacebookF, FaTwitter, FaInstagram } from "react-icons/fa";
import { useNavigate } from "react-router-dom";
import "./HomePage.css";

function HomePage() {
  const [fromStation, setFromStation] = useState("");
  const [toStation, setToStation] = useState("");
  const [travelDate, setTravelDate] = useState("");
  const navigate = useNavigate();

  const handleSearch = () => {
    if (!fromStation || !toStation || !travelDate) {
      alert("Please fill all fields");
      return;
    }
    navigate(
      `/trains?source=${fromStation}&destination=${toStation}&date=${travelDate}`
    );
  };

  return (
    <div className="homepage">
      {/* ===== Train Search Section ===== */}
      <section className="search-section">
        <h1 className="title">Train Ticket Booking</h1>
        <p className="subtitle">Easy IRCTC Login</p>

        <div className="search-card">
          <input
            type="text"
            placeholder="From (e.g., NDLS - Delhi)"
            value={fromStation}
            onChange={(e) => setFromStation(e.target.value)}
          />
          <input
            type="text"
            placeholder="To (e.g., MMCT - Mumbai)"
            value={toStation}
            onChange={(e) => setToStation(e.target.value)}
          />
          <input
            type="date"
            value={travelDate}
            onChange={(e) => setTravelDate(e.target.value)}
          />
          <button onClick={handleSearch} className="search-btn">
            Search
          </button>
        </div>
      </section>

      {/* ===== Banner Area ===== */}
      <div className="banner-area"></div>

      {/* ===== Footer ===== */}
      <footer className="footer">
        <div className="footer-container">
          <div className="footer-section about">
            <h3>RailConfirm</h3>
            <p>
              Your trusted train booking platform. Book tickets easily and
              travel safely.
            </p>
          </div>

          <div className="footer-section links">
            <h4>Quick Links</h4>
            <ul>
              <li><a href="/">Home</a></li>
              <li><a href="/explore">Explore</a></li>
              <li><a href="/my-ticket">My Tickets</a></li>
              <li><a href="/profile">Profile</a></li>
            </ul>
          </div>

          <div className="footer-section contact">
            <h4>Contact Us</h4>
            <p>Email: support@railconfirm.com</p>
            <p>Phone: +91 123 456 7890</p>
          </div>

          <div className="footer-section social">
            <h4>Follow Us</h4>
            <div className="social-icons">
              <a href="#"><FaFacebookF /></a>
              <a href="#"><FaTwitter /></a>
              <a href="#"><FaInstagram /></a>
            </div>
          </div>
        </div>

        <div className="footer-bottom">
          &copy; {new Date().getFullYear()} RailConfirm. All rights reserved.
        </div>
      </footer>
    </div>
  );
}

export default HomePage;
