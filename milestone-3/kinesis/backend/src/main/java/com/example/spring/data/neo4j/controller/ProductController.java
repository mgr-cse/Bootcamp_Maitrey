package com.example.spring.data.neo4j.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.spring.data.neo4j.model.Product;
import com.example.spring.data.neo4j.repository.ProductRepository;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api")
public class ProductController {

  @Autowired
  ProductRepository productRepository;

  @GetMapping("/products")
  public ResponseEntity<List<Product>> getAllProducts(@RequestParam(required = false) String name) {
    try {
      List<Product> products = new ArrayList<Product>();

      if (name == null)
        productRepository.findAll().forEach(products::add);

      if (products.isEmpty()) {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
      }

      return new ResponseEntity<>(products, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("/products/{id}")
  public ResponseEntity<Product> getProductById(@PathVariable("id") String id) {
    Optional<Product> productData = productRepository.findById(id);

    if (productData.isPresent()) {
      return new ResponseEntity<>(productData.get(), HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

}
