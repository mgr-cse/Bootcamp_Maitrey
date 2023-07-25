import React, { Component } from "react";
import HomeNavBar from  "./navbar/home.component"
import ProductMqttService from "../services/productMqtt.service"


export default class AddProduct extends Component {
  constructor(props) {
    super(props);
    this.onChangeName = this.onChangeName.bind(this);
    this.onChangeDescription = this.onChangeDescription.bind(this);
    this.onChangePrice = this.onChangePrice.bind(this);
    this.saveTutorial = this.saveTutorial.bind(this);
    this.newTutorial = this.newTutorial.bind(this);

    this.state = {
      id: null,
      name: "",
      price: 0.0,
      description: "", 
      submitted: false
    };
  }

  onChangeName(e) {
    this.setState({
      name: e.target.value
    });
  }

  onChangeDescription(e) {
    this.setState({
      description: e.target.value
    });
  }

  onChangePrice(e) {
    this.setState({
      price: e.target.value
    });
  }

  saveTutorial() {
    var data = {
      name: this.state.name,
      description: this.state.description,
      price: this.state.price
    };

    ProductMqttService.create(data , ()=> {
      console.log("state:" + this.state.submitted);
      //alert("Request delivered to mqtt server")
      this.setState({
        submitted: true
      });
      console.log("state:" + this.state.submitted);
  
    });
  }

  newTutorial() {
    this.setState({
      id: null,
      name: "",
      description: "",
      price: 0.0,

      submitted: false
    });
  }

  render() {
    return (
      <div>
        <HomeNavBar />

        <div className="container mt-3">
          <div className="submit-form">
        {this.state.submitted ? (
          <div>
            <h4>You submitted successfully to broker!</h4>
            <button className="btn btn-success" onClick={this.newTutorial}>
              Add
            </button>
          </div>
        ) : (
          <div>
            <div className="form-group">
              <label htmlFor="name">Name</label>
              <input
                type="text"
                className="form-control"
                id="name"
                required
                value={this.state.name}
                onChange={this.onChangeName}
                name="name"
              />
            </div>

            <div className="form-group">
              <label htmlFor="price">Price</label>
              <input
                type="number"
                step="any"
                className="form-control"
                id="price"
                required
                value={this.state.price}
                onChange={this.onChangePrice}
                name="price"
              />
            </div>

            <div className="form-group">
              <label htmlFor="description">Description</label>
              <input
                type="text"
                className="form-control"
                id="description"
                required
                value={this.state.description}
                onChange={this.onChangeDescription}
                name="description"
              />
            </div>

            <button onClick={this.saveTutorial} className="btn btn-success">
              Submit
            </button>
          </div>
        )}
          </div>
        </div>
      </div>
    );
  }
}
