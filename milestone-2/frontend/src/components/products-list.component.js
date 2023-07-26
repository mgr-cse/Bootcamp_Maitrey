import React, { Component } from "react";
import ProductDataService from "../services/product.service";
import { Link } from "react-router-dom";
import HomeNavBar from "./navbar/home.component"
import ProductMqttService from "../services/productMqtt.service"

export default class ProductsList extends Component {
  constructor(props) {
    super(props);
    this.retrieveTutorials = this.retrieveTutorials.bind(this);
    this.refreshList = this.refreshList.bind(this);
    this.setActiveTutorial = this.setActiveTutorial.bind(this);
    this.removeAllTutorials = this.removeAllTutorials.bind(this);

    this.state = {
      tutorials: [],
      currentTutorial: null,
      currentIndex: -1,
      selectedFile: ""
    };
  }

  componentDidMount() {
    this.retrieveTutorials();
  }

  retrieveTutorials() {
    ProductDataService.getAll()
      .then(response => {
        this.setState({
          tutorials: response.data
        });
        console.log(response.data);
      })
      .catch(e => {
        console.log(e);
      });
  }

  refreshList() {
    this.retrieveTutorials();
    this.setState({
      currentTutorial: null,
      currentIndex: -1
    });
  }

  setActiveTutorial(tutorial, index) {
    this.setState({
      currentTutorial: tutorial,
      currentIndex: index
    });
  }

  removeAllTutorials() {

    ProductMqttService.deleteAll(() => {
      alert("requesting mqtt service to delete all items")
      setTimeout(() => {
        this.refreshList();
      }, 1000);
    });

    /*ProductDataService.deleteAll()
      .then(response => {
        console.log(response.data);
        this.refreshList();
      })
      .catch(e => {
        console.log(e);
      });*/
  }

  onFileChangeHandler = (e) => {
    e.preventDefault();
    this.setState({
      selectedFile: e.target.files[0]
    });
    
  }

  onFileUploadHandler = (e) => {
    try {
      const formData = new FormData();
      formData.append('file', this.state.selectedFile);
      console.log(this.state.selectedFile);
      const reader = new FileReader()
      reader.readAsText(this.state.selectedFile);
      reader.onload = (e) => {
        console.log(e.target.result);
        ProductMqttService.uploadXML(e.target.result, ()=>{
          alert("file delivered to mqtt server successfully");
          window.location.reload(false);
        });
      }
    } catch(err) {
      alert("Exception: file upload failed")
    }  
  }

  render() {
    const { tutorials, currentTutorial, currentIndex } = this.state;

    return (
      <div>
        <HomeNavBar />
        <div className="container mt-3">
          <div className="list row">
        <div className="col-md-6">
          <h4>Products List</h4>

          <ul className="list-group">
            {tutorials &&
              tutorials.map((tutorial, index) => (
                <li
                  className={
                    "list-group-item " +
                    (index === currentIndex ? "active" : "")
                  }
                  onClick={() => this.setActiveTutorial(tutorial, index)}
                  key={index}
                >
                  {tutorial.name}
                </li>
              ))}
          </ul>

          <button
            className="m-3 btn btn-sm btn-danger"
            onClick={this.removeAllTutorials}
          >
            Remove All
          </button>

          <div className="form-group files color">
            <label>Upload XML File </label>
            <input type="file" className="form-control" name="file" onChange={this.onFileChangeHandler}/>
            <button className="m-3 btn-sm" onClick={this.onFileUploadHandler}>
              Upload
            </button>
          </div>

        </div>
        <div className="col-md-6">
          {currentTutorial ? (
            <div>
              <h4>Product</h4>
              <div>
                <label>
                  <strong>Name:</strong>
                </label>{" "}
                {currentTutorial.name}
              </div>
              <div>
                <label>
                  <strong>Description:</strong>
                </label>{" "}
                {currentTutorial.description}
              </div>
              <div>
                <label>
                  <strong>Price:</strong>
                </label>{" "}
                {currentTutorial.price}
              </div>

              <Link
                to={"/products/" + currentTutorial.id}
                className="badge badge-warning"
              >
                Edit
              </Link>
            </div>
          ) : (
            <div>
              <br />
              <p>Please click on a Product...</p>
            </div>
          )}
        </div>
          </div>
        </div>
      </div>
    );
  }
}
