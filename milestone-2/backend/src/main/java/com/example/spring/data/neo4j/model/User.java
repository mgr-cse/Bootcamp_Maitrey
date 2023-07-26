package com.example.spring.data.neo4j.model;


import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Node
public class User {

  @Id
  private String name;
  
  private String password;

  public User(String name, String password) {
    this.name = name;
    this.password = password;
  }

public  User() {

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
    return "User [name=" + name + ", password=" + password + "]";
  }
}
