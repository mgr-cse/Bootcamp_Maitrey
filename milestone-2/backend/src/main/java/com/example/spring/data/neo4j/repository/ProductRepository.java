package com.example.spring.data.neo4j.repository;

import java.util.List;

import org.springframework.data.neo4j.repository.Neo4jRepository;

import com.example.spring.data.neo4j.model.Product;

public interface ProductRepository extends Neo4jRepository<Product, String> {
  List<Product> findByNameContaining(String Name);
}
