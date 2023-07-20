import React, { Component } from "react";
import { Routes, Route, Link } from "react-router-dom";
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
    const loggedUser = (window.location.href.includes("product") || window.location.href.includes("add"));
    return (
      <div>
        <nav className="navbar navbar-expand navbar-dark bg-dark">
          <Link to={"/login"} className="navbar-brand">
            Inventory
          </Link>
          {loggedUser ? (
            <div className="navbar-nav mr-auto">
            <li className="nav-item">
              <Link to={"/products"} className="nav-link">
                Products
              </Link>
            </li>
            <li className="nav-item">
              <Link to={"/add"} className="nav-link">
                Add
              </Link>
            </li>
          </div>) : (<div className="navbar-nav mr-auto"></div>)}

          
          
          { (!loggedUser) ? (<div className="navbar-nav ml-auto">
              <li className="nav-item">
                <Link to={"/login"} className="nav-link">
                  Login
                </Link>
              </li>

              <li className="nav-item">
                <Link to={"/register"} className="nav-link">
                  Sign Up
                </Link>
              </li>
          </div>) : (<div className="navbar-nav ml-auto">
              <li className="nav-item">
                <Link to={"/login"} className="nav-link">
                  Logout
                </Link>
              </li>
        </div>)}


        </nav>

        <div className="container mt-3">
          <Routes>
            <Route path="/" element={<Login/>} />
            <Route path="/products" element={<ProductsList/>} />
            <Route path="/add" element={<AddProduct/>} />
            <Route path="/products/:id" element={<Product/>} />
            <Route path="/login" element={<Login/>} />
            <Route path="/register" element={<Register/>} />
          </Routes>
        </div>
      </div>
    );
  }
}

export default App;
