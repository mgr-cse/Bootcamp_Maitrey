package com.example.spring.data.mongodb.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.spring.data.mongodb.model.User;

public interface UserRepository extends MongoRepository<User, String> {
  List<User> findByName(String name);
}
