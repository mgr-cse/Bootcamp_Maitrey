package com.example.spring.data.neo4j.mqtt;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.spring.data.neo4j.repository.ProductRepository;

public class ProductControl {
  @Autowired
  ProductRepository productRepository;

  
}
