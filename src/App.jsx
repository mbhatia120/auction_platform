import { useState } from 'react'
import reactLogo from './assets/react.svg'
import viteLogo from '/vite.svg'
import './App.css'
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";

import ProductDetail from "./component/ProductDetail";

import ProductPage from './component/ProductPage'

function App() {
  

  return (
    <Router>
      <Routes>
        <Route path="/" element={<ProductPage />} />
        <Route path="/product/:name" element={<ProductDetail />} />
      </Routes>
    </Router>
  )
}

export default App
