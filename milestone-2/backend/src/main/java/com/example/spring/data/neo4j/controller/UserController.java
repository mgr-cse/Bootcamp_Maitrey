package com.example.spring.data.neo4j.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;

import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpServerErrorException;

import com.example.spring.data.neo4j.model.User;
import com.example.spring.data.neo4j.repository.UserRepository;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api")
public class UserController {

  @Autowired
  UserRepository userRepository;

  @PostMapping("/create")
  public ResponseEntity<User> createUser(@RequestBody User user) {
    try {
      List<User> users = userRepository.findByName(user.getName());
      if (!users.isEmpty()) throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR);
      User userTemp = userRepository.save(new User(user.getName(), user.getPassword()));
      return new ResponseEntity<>(userTemp, HttpStatus.CREATED);
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PostMapping("/login")
  public ResponseEntity<User> verifyUser(@RequestBody User user) {
    try {
      List<User> users = userRepository.findByName(user.getName());
      if (users.isEmpty())  throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR);
      if(users.get(0).getPassword().equals(user.getPassword()))
        return new ResponseEntity<>(user, HttpStatus.ACCEPTED);
      else throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR);

    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

}
