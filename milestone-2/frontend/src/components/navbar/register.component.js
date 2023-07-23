import React, { Component } from "react";
import { Link } from "react-router-dom";
import { withRouter } from "../../common/with-router";

class RegisterNavBar extends Component {
  render() {
    return (
      <nav className="navbar navbar-expand navbar-dark bg-dark">
          <Link to={"/login"} className="navbar-brand">
            Inventory
          </Link>
          <div className="navbar-nav mr-auto">
          </div>

          <div className="navbar-nav ml-auto">
              <li className="nav-item">
                <Link to={"/login"} className="nav-link">
                  Login
                </Link>
              </li>
          </div>
        </nav>
    )
  }
}

export default withRouter(RegisterNavBar);