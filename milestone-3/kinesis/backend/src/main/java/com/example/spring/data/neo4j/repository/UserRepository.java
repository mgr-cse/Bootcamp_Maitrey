package com.example.spring.data.neo4j.repository;

import java.util.List;

import org.springframework.data.neo4j.repository.Neo4jRepository;

import com.example.spring.data.neo4j.model.User;

public interface UserRepository extends Neo4jRepository<User, String> {
  List<User> findByName(String name);
}
