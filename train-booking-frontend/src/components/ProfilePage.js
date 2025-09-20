// src/components/ProfilePage.js
import React from "react";
import { FaUserCircle } from "react-icons/fa";
import { useNavigate } from "react-router-dom"; // ✅ import navigation hook
import "./ProfilePage.css";

function ProfilePage({ user, onLogout }) {
  const navigate = useNavigate();

  const handleLogout = () => {
    if (onLogout) {
      onLogout(); // ✅ clear user data (from parent App.js or context)
    }
    navigate("/login"); // ✅ redirect to login page
  };

  return (
    <div className="profile-container">
      <div className="profile-card">
        <FaUserCircle className="profile-icon" />

        {user ? (
          <>
            <h2 className="profile-name">{user.name}</h2>
            <p className="profile-email">{user.email}</p>
            <p className="profile-text">
              ✅ Welcome {user.name}! Manage your account details and preferences.
            </p>
            <blockquote className="profile-quote">
              “Great journeys start with a single step. Let’s make your travel seamless.”
            </blockquote>

            {/* ✅ Logout Button */}
            <button className="logout-btn" onClick={handleLogout}>
              Logout
            </button>
          </>
        ) : (
          <>
            <h2 className="profile-name">Guest</h2>
            <p className="profile-text">
              Manage your account details and preferences.
            </p>
            <blockquote className="profile-quote">
              “Travel is the only thing you buy that makes you richer.”
            </blockquote>
          </>
        )}
      </div>
    </div>
  );
}

export default ProfilePage;
