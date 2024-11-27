import React from 'react';

function ProductCard({ product, onSelect }) {
  return (
    <div className="product-card" onClick={onSelect} style={styles.card}>
      <h3>{product.name}</h3>
      <p>{product.description}</p>
      <p><strong>Starting Price:</strong> ${product.startingPrice}</p>
      <p><strong>Current Bid Price:</strong> ${product.currentHighestBid}</p>
    </div>
  );
}

const styles = {
  card: {
    width: '200px',
    height: '200px',
    border: '1px solid #ccc',
    borderRadius: '8px',
    margin: '10px',
    padding: '10px',
    display: 'inline-block',
    cursor: 'pointer',
    textAlign: 'center',
    boxShadow: '0 4px 8px rgba(0,0,0,0.1)',
    transition: 'transform 0.2s ease',
  }
};

// Optional: Hover effect to enlarge the card when hovering over it
styles.card[':hover'] = {
  transform: 'scale(1.05)',
};

export default ProductCard;
