package com.example.spring.data.mongodb.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.spring.data.mongodb.model.Product;

public interface ProductRepository extends MongoRepository<Product, String> {
  List<Product> findByNameContaining(String Name);
}
