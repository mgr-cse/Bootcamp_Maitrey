package com.example.spring.data.mongodb.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;

import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.spring.data.mongodb.model.User;
import com.example.spring.data.mongodb.repository.UserRepository;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/user")
public class UserController {

  @Autowired
  UserRepository userRepository;



  @PostMapping("/create")
  public ResponseEntity<User> createUser(@RequestBody User user) {
    try {
      User _user = userRepository.save(new User(user.getName(), user.getPassword()));
      return new ResponseEntity<>(_user, HttpStatus.CREATED);
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PostMapping("/login")
  public ResponseEntity<User> verifyUser(@RequestBody User user) {
    try {
      Optional<User> _user = userRepository.findById(user.getName());
      if(_user.isPresent()) {
        if (_user.get().getPassword().equals(user.getPassword()))
          return new ResponseEntity<>(user, HttpStatus.CREATED);
        else throw new Exception("password incorrect", null);
      }
      else throw new Exception("user not found", null);
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

}
