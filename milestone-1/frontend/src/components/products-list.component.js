import React, { Component } from "react";
import ProductDataService from "../services/product.service";
import UploadService from "../services/upload.service"
import { Link } from "react-router-dom";

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
    ProductDataService.deleteAll()
      .then(response => {
        console.log(response.data);
        this.refreshList();
      })
      .catch(e => {
        console.log(e);
      });
  }

  onFileChangeHandler = (e) => {
    e.preventDefault();
    this.setState({
      selectedFile: e.target.files[0]
    });
    
  }

  onFileUploadHandler = (e) => {
    const formData = new FormData();
    formData.append('file', this.state.selectedFile);
    UploadService.upload(formData)
      .then(
        res => {
          console.log(res.data)
          if (res.status === 201) {
            alert("file uploaded successfully");
          } else {
            alert("file upload failed!");
          }
          window.location.reload(true)
        }
      ).catch( res => {
        alert("file upload failed!");
      })
  }

  render() {
    const { tutorials, currentTutorial, currentIndex } = this.state;

    return (
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
    );
  }
}
