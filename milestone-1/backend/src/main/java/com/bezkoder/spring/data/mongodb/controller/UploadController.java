package com.bezkoder.spring.data.mongodb.controller;

import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;

import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.bezkoder.spring.data.mongodb.model.Product;
import com.bezkoder.spring.data.mongodb.repository.ProductRepository;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class UploadController {
  @Autowired
  ProductRepository productRepository;

  @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<HttpStatus> uploadFile(@RequestParam MultipartFile file) {
    // parse the received file
    try {
      DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
      Document doc = builder.parse(file.getInputStream());
      doc.getDocumentElement().normalize();
      
      NodeList nodeList  = doc.getElementsByTagName("grocery");
      if (nodeList.getLength()!=1)
        throw new Exception("Cannot parse", null);
      Node groceryNode = nodeList.item(0);
      NodeList nameNodes = ((Element) groceryNode).getElementsByTagName("name");
      NodeList priceNodes = ((Element) groceryNode).getElementsByTagName("price");
      NodeList descriptionNodes = ((Element) groceryNode).getElementsByTagName("description");

      List<Product> parsedList = new ArrayList<Product>();
      for(int i=0; i<nameNodes.getLength(); i++) {
        String pName = nameNodes.item(i).getTextContent();
        float pPrice = Float.parseFloat(priceNodes.item(i).getTextContent());
        String pDesc = descriptionNodes.item(i).getTextContent();

        parsedList.add(new Product(pName, pPrice, pDesc));
      }

      // adding items to database
      for(Product p:parsedList) {
        productRepository.save(p);
      }
      return new ResponseEntity<>(HttpStatus.CREATED);

    } catch (Exception e) {
      // error in parsing
      return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
    }
  }
}
