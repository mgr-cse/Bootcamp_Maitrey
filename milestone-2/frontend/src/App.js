import React, { Component } from "react";
import { Routes, Route } from "react-router-dom";
import "bootstrap/dist/css/bootstrap.min.css";
import "./App.css";

import AddProduct from "./components/add-product.component";
import Product from "./components/product.component";
import ProductsList from "./components/products-list.component";
import Login from "./components/login.component"
import Register from "./components/register.component"

class App extends Component {
  constructor(props) {
    super(props);
    this.logOut = this.logOut.bind(this);

    this.state = {
      currentUser: undefined,
    };
  }

  componentDidMount() {
    
  }

  logOut() {
    this.setState({
      currentUser: undefined,
    });
  }

  render() {
    return (
      <div>
        <Routes>
          <Route path="/" element={<Login/>} />
          <Route path="/products" element={<ProductsList/>} />
          <Route path="/add" element={<AddProduct/>} />
          <Route path="/products/:id" element={<Product/>} />
          <Route path="/login" element={<Login/>} />
          <Route path="/register" element={<Register/>} />
        </Routes>
      </div>
    );
  }
}

export default App;
