import React, { Component } from "react";
import { Link } from "react-router-dom";
import { withRouter } from "../../common/with-router";

class HomeNavBar extends Component {
  render() {
    return (
      <nav className="navbar navbar-expand navbar-dark bg-dark">
          <Link to={"/login"} className="navbar-brand">
            Inventory
          </Link>
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
          </div>

          <div className="navbar-nav ml-auto">
              <li className="nav-item">
                <Link to={"/login"} className="nav-link">
                  Logout
                </Link>
              </li>
          </div>
        </nav>
    )
  }
}

export default withRouter(HomeNavBar);