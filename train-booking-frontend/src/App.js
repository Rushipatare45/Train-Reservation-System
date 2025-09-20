// src/App.js
import React, { useState } from "react";
import { Routes, Route, Navigate } from "react-router-dom";

import Navbar from "./components/NavBar";
import HomePage from "./components/HomePage";
import ExplorePage from "./components/ExplorePage";
import MyTicketPage from "./components/MyTicketPage";
import ProfilePage from "./components/ProfilePage";
import TrainList from "./components/TrainList";
import BookingForm from "./components/BookingForm";
import LoginPage from "./pages/LoginPage";
import RegisterPage from "./pages/RegisterPage";

function App() {
  const [user, setUser] = useState(null); // Logged-in user info

  return (
    <div>
      {/* ✅ Single navbar at the top */}
      <Navbar user={user} />

      <Routes>
        {/* Protected routes */}
        <Route
          path="/"
          element={user ? <HomePage /> : <Navigate to="/login" />}
        />
        <Route
          path="/explore"
          element={user ? <ExplorePage /> : <Navigate to="/login" />}
        />
        <Route
          path="/my-ticket"
          element={user ? <MyTicketPage /> : <Navigate to="/login" />}
        />
        <Route
          path="/profile"
          element={user ? <ProfilePage user={user} /> : <Navigate to="/login" />}
        />
        <Route
          path="/trains"
          element={user ? <TrainList /> : <Navigate to="/login" />}
        />
        <Route
          path="/booking/:id"
          element={user ? <BookingForm /> : <Navigate to="/login" />}
        />
        <Route
          path="/booking"
          element={user ? <BookingForm /> : <Navigate to="/login" />}
        />

        {/* Public routes */}
        <Route path="/login" element={<LoginPage setUser={setUser} />} />
        <Route path="/register" element={<RegisterPage />} />

        {/* Catch-all: redirect to home or login */}
        <Route
          path="*"
          element={<Navigate to={user ? "/" : "/login"} replace />}
        />
      </Routes>
    </div>
  );
}

export default App;
