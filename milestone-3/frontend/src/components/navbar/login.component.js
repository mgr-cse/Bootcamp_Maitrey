import React, { Component } from "react";
import { Link } from "react-router-dom";
import { withRouter } from "../../common/with-router";

class LoginNavBar extends Component {
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
                <Link to={"/register"} className="nav-link">
                  Register
                </Link>
              </li>
          </div>
        </nav>
    )
  }
}

export default withRouter(LoginNavBar);