package com.example.spring.data.neo4j.mqtt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xml.sax.InputSource;

import com.example.spring.data.neo4j.model.Product;
import com.example.spring.data.neo4j.repository.ProductRepository;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;



@Component(value = "productControl")
public class ProductControl {
  @Autowired
  ProductRepository productRepository;

  public void create(JSONObject productJson) {
    productRepository.save(new Product(productJson.getString("name"), productJson.getFloat("price"), productJson.getString("description")));
  }

  public void update(String id ,JSONObject prodJson) {
    Optional<Product> productData = productRepository.findById(id);
    if(productData.isPresent()) {
      Product _product = productData.get();
      _product.setName(prodJson.getString("name"));
      _product.setDescription(prodJson.getString("description"));
      _product.setPrice(prodJson.getFloat("price"));
      productRepository.save(_product);
    }
  }

  public void deleteProduct(String id) {
    productRepository.deleteById(id);
  }

  public void deleteAll() {
    productRepository.deleteAll();
  }

  public void fromXML(String xml) {
    try {
      DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
      Document doc = builder.parse(new InputSource(new StringReader(xml)));
      
      doc.getDocumentElement().normalize();
      
      NodeList nodeList  = doc.getElementsByTagName("grocery");
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
    } catch (Exception e) {
      System.out.println("XML parse error");
    }
  }
}
