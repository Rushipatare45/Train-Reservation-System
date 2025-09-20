// src/components/NavBar.js
import React from "react";
import { Link } from "react-router-dom";
import { FaTrain } from "react-icons/fa";
import "./NavBar.css";

function Navbar({ user, setUser }) {
  const handleLogout = () => {
    if (setUser) setUser(null);
  };

  return (
    <nav className="navbar">
      {/* Logo on the left */}
      <div className="logo">
        <FaTrain className="logo-icon" />
        <span className="logo-text">RailConfirm</span>
      </div>

      {/* Nav links on the right */}
      <div className="nav-links">
        {user ? (
          <>
            <Link to="/">Home</Link>
            <Link to="/explore">Explore</Link>
            <Link to="/my-ticket">My Tickets</Link>
            <Link to="/profile">Profile</Link>
           
          </>
        ) : (
          <>
            <Link to="/login">Login</Link>
            <Link to="/register">Register</Link>
          </>
        )}
      </div>
    </nav>
  );
}

export default Navbar;
