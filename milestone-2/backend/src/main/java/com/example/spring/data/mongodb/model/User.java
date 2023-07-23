package com.example.spring.data.mongodb.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
public class User {

  @Id
  private String id;

  @Indexed(unique = true)
  private String name;
  
  private String password;

  public User() {

  }

  public User(String name, String password) {
    this.name = name;
    this.password = password;
  }



  public String getName() {
    return name;
  }

  public String getPassword() {
    return password;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  @Override
  public String toString() {
    return "User [name=" + ", password=" + password + "]";
  }
}
