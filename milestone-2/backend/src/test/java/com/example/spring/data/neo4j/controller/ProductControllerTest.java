package com.example.spring.data.neo4j.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.example.spring.data.neo4j.repository.ProductRepository;
import com.example.spring.data.neo4j.model.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.dao.CannotAcquireLockException;


@ExtendWith(MockitoExtension.class)
class ProductControllerTest {
    @InjectMocks
    ProductController productController;

    @Mock
    ProductRepository productRepository;

    private List<Product> getProductList() {
        Product p1 = new Product("p1", 1.0f, "p1-dsc");
        Product p2 = new Product("p2", 1.0f, "p2-dsc");
        List<Product> productList = new ArrayList<>();
        productList.add(p1);
        productList.add(p2);
        return  productList;
    }

    private  List<Product> getEmptyProductList() {
        List<Product> productList = new ArrayList<>();
        return productList;
    }

    @Test
    public void testGetAllProducts() {
        List<Product> productList = getProductList();

        // fakery
        when(productRepository.findAll()).thenReturn(productList);

        // test func
        ResponseEntity<List<Product>> products = productController.getAllProducts(null);


        // correctness
        assertThat(products.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(products.getBody().size()).isEqualTo(2);

        assertThat(products.getBody().get(0).getName()).isEqualTo("p1");
        assertThat(products.getBody().get(1).getName()).isEqualTo("p2");

        assertThat(products.getBody().get(0).getPrice()).isEqualTo(1.0f);
        assertThat(products.getBody().get(1).getPrice()).isEqualTo(1.0f);

        assertThat(products.getBody().get(0).getDescription()).isEqualTo("p1-dsc");
        assertThat(products.getBody().get(1).getDescription()).isEqualTo("p2-dsc");

    }

    @Test
    void getAllProductsEmptyList() {
        List<Product> productList = getEmptyProductList();

        // fakery
        when(productRepository.findAll()).thenReturn(productList);

        // test func
        ResponseEntity<List<Product>> products = productController.getAllProducts(null);


        // correctness
        assertThat(products.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

    }

    @Test
    void getAllProductsDbException() {
        when(productRepository.findAll()).thenThrow(new CannotAcquireLockException("no lock"));
        // test func
        ResponseEntity<List<Product>> products = productController.getAllProducts(null);
        //correctness
        assertThat(products.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    void getProductById() {
        List<Product> productList = getProductList();
        when(productRepository.findById("0")).thenReturn(Optional.of(productList.get(0)));
        when(productRepository.findById("2")).thenReturn(Optional.empty());

        ResponseEntity<Product> response = productController.getProductById("0");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getName()).isEqualTo("p1");

        response = productController.getProductById("2");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

}