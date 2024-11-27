import React, { useEffect, useState,  useRef } from "react";
import { useParams } from "react-router-dom";
import axios from "axios";
import "./ProductDetail.css";

const ProductDetail = () => {
  const { name } = useParams();
  const [product, setProduct] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [userName, setUserName] = useState("");
  const [bidAmount, setBidAmount] = useState("");
  const [bidError, setBidError] = useState("");
  const [successMessage, setSuccessMessage] = useState("");
  const wsRef = useRef(null); 

  useEffect(() => {
    const fetchProductDetails = async () => {
      try {
        setLoading(true);
        const response = await axios.get(`http://localhost:8080/products/${name}`);
        setProduct(response.data);
      } catch (err) {
        setError(err.response ? err.response.data.message : err.message);
      } finally {
        setLoading(false);
      }
    };
  
    fetchProductDetails();
  
    if (!wsRef.current) {
      wsRef.current = new WebSocket(`ws://localhost:8080/bidding?productName=${name}`);
  
      wsRef.current.onopen = () => {
        console.log("WebSocket connection established.");
      };
  
      wsRef.current.onmessage = (event) => {
        console.log("Message from server:", event.data);
  
        const match = event.data.match(/Product:\s*(\w+),\s*User:\s*(\w+),\s*Amount:\s*([\d.]+)/);
  
        if (match) {
          const product = match[1];
          const user = match[2];
          const amount = parseFloat(match[3]);
  
          setProduct((prevProduct) => ({
            ...prevProduct,
            currentHighestBid: amount,
            highestBidder: { username: user },
          }));
        } else {
          console.error("Unexpected message format:", event.data);
        }
      };
  
      wsRef.current.onclose = () => {
        console.log("WebSocket connection closed.");
      };
  
      wsRef.current.onerror = (error) => {
        console.error("WebSocket error:", error);
      };
    }
  
    return () => {
      if (wsRef.current) {
        try {
          wsRef.current.close();
          console.log("WebSocket connection cleaned up.");
        } catch (err) {
          console.error("Error closing WebSocket:", err);
        } finally {
          wsRef.current = null;
        }
      }
    };
  }, [name]);

  const handleBidSubmission = async () => {
    if (!userName.trim() || !bidAmount.trim()) {
      setBidError("Both user name and bid amount are required.");
      setSuccessMessage("");
      return;
    }

    if (parseFloat(bidAmount) < product.currentHighestBid) {
      setBidError("Your bid must be higher than the current highest bid.");
      setSuccessMessage("");
      return;
    }

    try {
      const payload = {
        product: product.name,
        name: userName,
        amount: parseFloat(bidAmount),
      };
      console.log(payload);
      const response = await axios.post(`http://localhost:8080/bids`, payload);
      
      setProduct((prevProduct) => ({
        ...prevProduct,
        currentHighestBid: parseFloat(bidAmount),
        highestBidder: { username: userName },
      }));
      console.log(product);
      setBidError("");
      setSuccessMessage(response.data.message || "Bid placed successfully!");
    } catch (err) {
      setBidError(err.response ? err.response.data.message : err.message);
      setSuccessMessage("");
    }
  };

  if (loading) {
    return <p>Loading...</p>;
  }

  if (error) {
    return <p>Error fetching product details: {error}</p>;
  }

  if (!product) {
    return <p>Product not found.</p>;
  }

  return (
    <div className="product-detail-container">
      <div className="product-card">
        <h1 className="product-name">{product.name}</h1>
        <p className="product-description">{product.description}</p>
        <p className="product-price"><strong>Starting Price:</strong> ${product.startingPrice}</p>
        <p className="current-bid"><strong>Current Highest Bid:</strong> ${product.currentHighestBid}</p>
        {product.highestBidder && (
          <p className="highest-bidder"><strong>User Winning:</strong> {product.highestBidder.username}</p>
        )}
      </div>

      <div className="bid-section">
        <h3>Place Your Bid</h3>
        <input
          type="text"
          placeholder="Your Name"
          value={userName}
          onChange={(e) => setUserName(e.target.value)}
          className="input-field"
        />
        <input
          type="number"
          placeholder="Your Bid Amount"
          value={bidAmount}
          onChange={(e) => setBidAmount(e.target.value)}
          className="input-field"
        />
        <button onClick={handleBidSubmission} className="submit-button">
          Submit Bid
        </button>
        {bidError && <p className="error-message">{bidError}</p>}
        {successMessage && <p className="success-message">{successMessage}</p>}
      </div>
    </div>
  );
};

export default ProductDetail;
