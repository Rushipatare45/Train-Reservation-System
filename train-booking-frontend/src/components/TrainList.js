// src/components/TrainList.js
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

  // Station alias map
  const stationAlias = {
    mumbai: "Mumbai CSMT",
    bombay: "Mumbai CSMT",
    "mumbai csmt": "Mumbai CSMT",
    "mumbai dadar": "Mumbai Dadar",
    dadar: "Mumbai Dadar",
    shirdi: "Sainagar Shirdi",
    "sainagar shirdi": "Sainagar Shirdi",
    pune: "Pune",
    nagpur: "Nagpur",
    hyderabad: "Hyderabad",
    goa: "Madgaon",
    bangalore: "Bangalore",
    bengaluru: "Bangalore",
    delhi: "New Delhi",
    jaipur: "Jaipur",
    kolkata: "Kolkata",
    bhubaneswar: "Bhubaneswar",
  };

  // Convert user input to backend-required station
  const mapToBackendStation = (station) => {
    if (!station) return "";
    const key = station.toLowerCase().trim();
    return stationAlias[key] || station;
  };

  useEffect(() => {
    if (!fromStation || !toStation) {
      setError("Please provide both source and destination stations.");
      setLoading(false);
      return;
    }

    setLoading(true);
    setError(null);

    // Map input
    const fromMapped = mapToBackendStation(fromStation);
    const toMapped = mapToBackendStation(toStation);

    axios
      .get(
        `http://localhost:8080/trains/search?fromStation=${fromMapped}&toStation=${toMapped}`
      )
      .then((response) => {
        console.log("API Response:", response.data);

        let data = [];
        if (Array.isArray(response.data)) {
          data = response.data;
        } else if (response.data && Array.isArray(response.data.trains)) {
          data = response.data.trains;
        }

        // 🔑 Case-insensitive + order filtering on frontend too
        const filteredTrains = data.filter((train) => {
          if (!Array.isArray(train.stops) || train.stops.length === 0) return false;

          const stopsLower = train.stops.map((s) => s.toLowerCase());
          const fromIdx = stopsLower.indexOf(fromMapped.toLowerCase());
          const toIdx = stopsLower.indexOf(toMapped.toLowerCase());

          return fromIdx !== -1 && toIdx !== -1 && fromIdx < toIdx;
        });

        setTrains(filteredTrains);
        setLoading(false);
      })
      .catch((err) => {
        console.error("Error fetching trains:", err);
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
        fontFamily: "Arial, sans-serif",
      }}
    >
      <h1
        style={{
          textAlign: "center",
          marginBottom: "25px",
          color: "#007bff",
        }}
      >
        RailConfirm - Available Trains
      </h1>

      {trains.length === 0 ? (
        <p style={{ textAlign: "center" }}>
          No trains found from {fromStation} to {toStation}.
        </p>
      ) : (
        <table
          style={{
            width: "100%",
            borderCollapse: "collapse",
            boxShadow: "0 0 10px rgba(0,0,0,0.1)",
          }}
        >
          <thead>
            <tr style={{ backgroundColor: "#007bff", color: "white" }}>
              <th style={{ padding: "12px", border: "1px solid #ddd" }}>ID</th>
              <th style={{ padding: "12px", border: "1px solid #ddd" }}>Name</th>
              <th style={{ padding: "12px", border: "1px solid #ddd" }}>
                Departure Time
              </th>
              <th style={{ padding: "12px", border: "1px solid #ddd" }}>Stops</th>
            </tr>
          </thead>
          <tbody>
            {trains.map((train) => (
              <tr
                key={train.id}
                style={{
                  textAlign: "center",
                  borderBottom: "1px solid #ddd",
                  cursor: "pointer",
                  transition: "background 0.3s",
                }}
                onClick={() =>
                  navigate(`/booking/${train.id}`, {
                    state: { train, fromStation, toStation },
                  })
                }
                onMouseEnter={(e) =>
                  (e.currentTarget.style.backgroundColor = "#f0f8ff")
                }
                onMouseLeave={(e) =>
                  (e.currentTarget.style.backgroundColor = "transparent")
                }
              >
                <td style={{ padding: "10px" }}>{train.id}</td>
                <td style={{ padding: "10px" }}>{train.name}</td>
                <td style={{ padding: "10px" }}>{train.departureTime || "N/A"}</td>
                <td style={{ padding: "10px" }}>
                  {Array.isArray(train.stops) && train.stops.length > 0
                    ? train.stops.join(" → ")
                    : "N/A"}
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
