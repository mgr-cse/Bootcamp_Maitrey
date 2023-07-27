package com.example.spring.data.neo4j.mqtt;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.example.spring.data.neo4j.model.Product;
import com.example.spring.data.neo4j.repository.ProductRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ProductControlTest {

    @InjectMocks
    ProductControl productControl;

    @Mock
    ProductRepository productRepository;

    private JSONObject sampleJSONObject() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", "test");
        jsonObject.put("description", "test");
        jsonObject.put("price", 1.0f);
        return jsonObject;
    }

    @Test
    void create() {
        productControl.create(sampleJSONObject());
    }

    @Test
    void update() {
        String id = "testId";
        when(productRepository.findById(id)).thenReturn(Optional.of(new Product("test1", 2.0f, "test2")));
        productControl.update(id ,sampleJSONObject());
        String id2 = "testId2";
        when(productRepository.findById(id2)).thenReturn(Optional.empty());
        productControl.update(id2, sampleJSONObject());
    }

    @Test
    void deleteProduct() {
        productRepository.deleteById("test");
    }

    @Test
    void deleteAll() {
        productRepository.deleteAll();
    }

    @Test
    void fromXML() {
        String testXML1 = """
                <?xml version="1.0"?>
                <grocery>
                <product>
                <name>Apples</name>
                <price>1.50</price>
                <description>Red apples from Washington state</description>
                </product>
                </grocery>""";

        String testXML2 = "random";

        String testXML3 = """
                <?xml version="1.0"?>
                <grocery>
                <product>
                <name>Apples</name>
                </product>
                </grocery>""";

        String testXML4 =  """
                <?xml version="1.0"?>
                <grocery>
                </grocery>
                <grocery>
                <name></name>
                </grocery>
                """;

        productControl.fromXML(testXML1);
        productControl.fromXML(testXML2);
        productControl.fromXML(testXML3);
        productControl.fromXML(testXML4);
    }
}