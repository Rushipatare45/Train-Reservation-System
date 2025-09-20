// src/pages/LoginPage.js
import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import { FaEye, FaEyeSlash } from "react-icons/fa"; 
import "./LoginPage.css";

function LoginPage({ setUser }) {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [showPassword, setShowPassword] = useState(false);
  const [message, setMessage] = useState("");
  const navigate = useNavigate();

  const handleLogin = async (e) => {
    e.preventDefault();

    if (!email || !password) {
      setMessage("❌ Please fill all fields");
      return;
    }

    try {
      const res = await axios.post("http://localhost:8080/api/users/login", { email, password });
      const userData = res.data;

      // ✅ Save full user object in localStorage (including ID)
      localStorage.setItem("user", JSON.stringify(userData));
      localStorage.setItem("userId", userData.id);
      localStorage.setItem("userName", userData.name);

      // ✅ Update parent state
      setUser(userData);

      setMessage("✅ Login successful!");
      navigate("/"); // redirect to home
    } catch (err) {
      setMessage(err.response?.data?.message || "❌ Incorrect email or password");
    }
  };

  return (
    <div className="login-container">
      <div className="login-card">
        <h2 className="login-title">🔑 Login</h2>
        <form onSubmit={handleLogin} className="login-form">
          <input
            type="email"
            placeholder="Email"
            value={email}
            onChange={e => setEmail(e.target.value)}
            required
          />

          <div className="password-container">
            <input
              type={showPassword ? "text" : "password"}
              placeholder="Password"
              value={password}
              onChange={e => setPassword(e.target.value)}
              required
            />
            <span
              className="password-icon"
              onClick={() => setShowPassword(!showPassword)}
            >
              {showPassword ? <FaEyeSlash /> : <FaEye />}
            </span>
          </div>

          <button type="submit">Login</button>
        </form>

        {message && <p className="login-message">{message}</p>}

        {/* ✅ Register Button */}
        <p className="register-text">
          Don’t have an account?{" "}
          <button
            type="button"
            className="register-button"
            onClick={() => navigate("/register")}
          >
            Register
          </button>
        </p>
      </div>
    </div>
  );
}

export default LoginPage;
