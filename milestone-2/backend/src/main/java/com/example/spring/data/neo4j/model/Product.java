package com.example.spring.data.neo4j.model;



import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Node
public class Product {
  @Id @GeneratedValue
  private String id;

  private String name;
  private String description;
  private float price;


  public Product(String name, float price ,String description) {
    this.name = name;
    this.price = price;
    this.description = description;
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public float getPrice() {
    return price;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setPrice(float price) {
    this.price = price;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @Override
  public String toString() {
    return "Product [id=" + id + ", name=" + name + ", desc=" + description + ", price=" + price + "]";
  }
}
