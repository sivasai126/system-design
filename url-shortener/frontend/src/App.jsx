import { useState } from "react";
import "./App.css";

function App() {
  const [url, setUrl] = useState("");
  const [shortUrl, setShortUrl] = useState("");
  const [stats, setStats] = useState(null);
  const [copied, setCopied] = useState(false);
  const [loading, setLoading] = useState(false);

  const shortenUrl = async () => {
    try {

      const response = await fetch(
        "http://localhost:8080/shorten",
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json"
          },
          body: JSON.stringify({
            url: url
          })
        }
      );

      console.log("Status:", response.status);

      const data = await response.json();

      console.log("Response:", data);

      setShortUrl(data.shortUrl);

    } catch (err) {
      console.error(err);
    }
  };

  const fetchStats = async () => {
    try {
      const code = shortUrl.split("/").pop();

      const response = await fetch(
        `http://localhost:8080/stats/${code}`
      );

      const data = await response.json();

      setStats(data);
    } catch (err) {
      console.error(err);
    }
  };

  const copyLink = async () => {
    await navigator.clipboard.writeText(shortUrl);

    setCopied(true);

    setTimeout(() => {
      setCopied(false);
    }, 2000);
  };

  return (
    <div className="app">
      <div className="container">
        <div className="header">
          <h1>🔗 URL Shortener</h1>
          <p>
            Fast URL shortening powered by Spring Boot, PostgreSQL, Redis & React
          </p>
        </div>

        <div className="input-section">
          <input
            type="text"
            placeholder="Paste your long URL here..."
            value={url}
            onChange={(e) => setUrl(e.target.value)}
          />

          <button
            onClick={shortenUrl}
            disabled={loading}
          >
            {loading ? "Creating..." : "Shorten"}
          </button>
        </div>

        {shortUrl && (
          <div className="dashboard">
            <div className="card">
              <h3>Generated Link</h3>

              <div className="short-url">
                <a
                  href={shortUrl}
                  target="_blank"
                  rel="noreferrer"
                >
                  {shortUrl}
                </a>
              </div>

              <div className="actions">
                <button onClick={copyLink}>
                  {copied ? "✓ Copied" : "Copy"}
                </button>

                <button onClick={fetchStats}>
                  Analytics
                </button>
              </div>
            </div>

            <div className="card analytics">
              <h3>Analytics</h3>

              {stats ? (
                <>
                  <div className="metric">
                    <span>Total Clicks</span>
                    <strong>{stats.clicks}</strong>
                  </div>

                  <div className="metric">
                    <span>Short Code</span>
                    <strong>{stats.shortCode}</strong>
                  </div>

                  <div className="url-box">
                    {stats.originalUrl}
                  </div>
                </>
              ) : (
                <p>Click Analytics to load stats</p>
              )}
            </div>
          </div>
        )}
      </div>
    </div>
  );
}

export default App;