import React, { useEffect, useState } from "react";
import axios from "axios";
import { useLocation, useNavigate } from "react-router-dom";

function TrainList() {
  const [trains, setTrains] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const location = useLocation();
  const navigate = useNavigate();

  // Get query params from URL
  const query = new URLSearchParams(location.search);
  const fromStation = query.get("source");
  const toStation = query.get("destination");

  useEffect(() => {
    setLoading(true);
    setError(null);

    const url =
      fromStation && toStation
        ? `http://localhost:8080/trains/search?fromStation=${fromStation}&toStation=${toStation}`
        : "http://localhost:8080/trains/all";

    axios
      .get(url)
      .then((response) => {
        const data = Array.isArray(response.data)
          ? response.data
          : response.data?.trains || [];
        setTrains(data);
        setLoading(false);
      })
      .catch((err) => {
        setError("Failed to fetch trains");
        setLoading(false);
      });
  }, [fromStation, toStation]);

  if (loading) return <p style={{ textAlign: "center" }}>Loading trains...</p>;
  if (error) return <p style={{ textAlign: "center" }}>{error}</p>;

  return (
    <div
      style={{
        maxWidth: "900px",
        margin: "30px auto",
        fontFamily: "Segoe UI, Tahoma, Geneva, Verdana, sans-serif",
      }}
    >
      <h1
        style={{
          textAlign: "center",
          marginBottom: "25px",
          color: "#2c3e50", // dark professional color
        }}
      >
        RailConfirm - Available Trains
      </h1>

      {trains.length === 0 ? (
        <p style={{ textAlign: "center" }}>
          {fromStation && toStation
            ? `No trains found from ${fromStation} to ${toStation}.`
            : "No trains available."}
        </p>
      ) : (
        <table
          style={{
            width: "100%",
            borderCollapse: "collapse",
            boxShadow: "0 0 10px rgba(0,0,0,0.05)", // lighter shadow
          }}
        >
          <thead>
            <tr style={{ backgroundColor: "#ecf0f1", color: "#2c3e50" }}>
              <th style={{ padding: "12px", border: "1px solid #ddd" }}>ID</th>
              <th style={{ padding: "12px", border: "1px solid #ddd" }}>Name</th>
              <th style={{ padding: "12px", border: "1px solid #ddd" }}>Departure Time</th>
              <th style={{ padding: "12px", border: "1px solid #ddd" }}>Stops</th>
            </tr>
          </thead>
          <tbody>
            {trains.map((train, index) => (
              <tr
                key={train.id}
                style={{
                  textAlign: "center",
                  borderBottom: "1px solid #ddd",
                  cursor: "pointer",
                  backgroundColor: index % 2 === 0 ? "#ffffff" : "#f9f9f9", // soft alternating rows
                  transition: "background 0.3s",
                }}
                onClick={() => navigate(`/booking/${train.id}`)}
                onMouseEnter={(e) =>
                  (e.currentTarget.style.backgroundColor = "#dfe6e9")
                }
                onMouseLeave={(e) =>
                  (e.currentTarget.style.backgroundColor =
                    index % 2 === 0 ? "#ffffff" : "#f9f9f9")
                }
              >
                <td style={{ padding: "10px" }}>{train.id}</td>
                <td style={{ padding: "10px" }}>{train.name}</td>
                <td style={{ padding: "10px" }}>
                  {train.departureTime || "N/A"}
                </td>
                <td style={{ padding: "10px" }}>
                  {train.stops ? train.stops.join(" → ") : "N/A"}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
}

export default TrainList;
