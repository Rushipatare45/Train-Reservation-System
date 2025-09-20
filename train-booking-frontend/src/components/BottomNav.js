import React from "react";
import { FaHome, FaSearch, FaTicketAlt, FaUser } from "react-icons/fa";
import "./BottomNav.css";

const BottomNav = () => {
  return (
    <div className="bottom-nav">
      <div className="nav-item">
        <FaHome size={24} />
        <span>Home</span>
      </div>
      <div className="nav-item center-icon">
        <FaSearch size={32} />
      </div>
      <div className="nav-item">
        <FaTicketAlt size={24} />
        <span>My Ticket</span>
      </div>
      <div className="nav-item">
        <FaUser size={24} />
        <span>Profile</span>
      </div>
    </div>
  );
};

export default BottomNav;
