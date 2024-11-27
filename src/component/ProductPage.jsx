import React, { useEffect, useState } from 'react';
import axios from 'axios';
import './ProductPage.css'; // Importing the CSS file
import ProductCard from './ProductCard'; // Assuming you have a ProductCard component
import { useNavigate } from "react-router-dom";

const ProductPage = () => {
  const [products, setProducts] = useState([]);
  const [showForm, setShowForm] = useState(false);
  const [newProduct, setNewProduct] = useState({
    name: '',
    description: '',
    startingPrice: '',
  });
  const navigate = useNavigate();

  // Fetch products from backend when component mounts
  useEffect(() => {
    const fetchProducts = async () => {
      try {
        const response = await axios.get('http://localhost:8080/products', {
          action: 'getProducts', // Example payload for fetching products
        });
        // console.log(response);
        setProducts(response.data); // Assuming backend sends a 'products' array
      } catch (error) {
        console.error('Error fetching products:', error);
      }
    };

    fetchProducts();
  }, [products.length]);

  const handleSelectProduct = (product) => {
    // Redirect to the product details page
    navigate(`/product/${product.name}`);
  };

  const handleAddProduct = async (e) => {
    e.preventDefault();
    const product = newProduct;
    console.log(product);
    try {
      const response = await axios.post('http://localhost:8080/products', product);

      if (response.status === 200) {
        setProducts([...products, product]);
        setNewProduct({ name: '', description: '', startingPrice: '' }); // Reset form fields
        setShowForm(false); // Hide the form after adding the product
      }
    } catch (error) {
      console.error('Error adding product:', error);
    }
  };

  return (
    <div>
      <button
        className="add-product-button"
        onClick={() => setShowForm(true)}
      >
        Add Product
      </button>
      {showForm && (
        <div className="product-form">
          <h3>Add New Product</h3>
          <form>
            <input
              type="text"
              placeholder="Product Name"
              value={newProduct.name}
              onChange={(e) => setNewProduct({ ...newProduct, name: e.target.value })}
              className="form-input"
            />
            <input
              type="text"
              placeholder="Description"
              value={newProduct.description}
              onChange={(e) => setNewProduct({ ...newProduct, description: e.target.value })}
              className="form-input"
            />
            <input
              type="number"
              placeholder="Start Price"
              value={newProduct.startingPrice}
              onChange={(e) => setNewProduct({ ...newProduct, startingPrice: e.target.value })}
              className="form-input"
            />
            <button
              type="button"
              onClick={handleAddProduct}
              className="submit-button"
            >
              Add Product
            </button>
          </form>
        </div>
      )}
      <h1>Product Page</h1>
      <div className="product-list">
        {products?.map((product,index) => (
          <ProductCard
            key = {index}
            product={product}
            onSelect={() => handleSelectProduct(product)}
          />
        ))}
      </div>
    </div>
  );
};

export default ProductPage;

