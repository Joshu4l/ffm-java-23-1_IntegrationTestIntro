package com.example.productrepository.products.controller;
import com.example.productrepository.products.ProductRepository;
import com.example.productrepository.products.models.NewProduct;
import com.example.productrepository.products.models.Product;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ProductRepository productRepository;

    @Test
    @DirtiesContext
    void getAllProductsTest() throws Exception {

        productRepository.save(new Product("1", "test-title", 15));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/products"))
		/*
			 Ein erfolgreicher Test muss aus unserer Sicht sowohl aus einem
			 korrekten STATUS CODE als auch aus einem korrekten BODY bestehen,
			 konkret ergänzen wir dafür folgendes:
		*/
            .andExpect(status().isOk())
            .andExpect(content().json("""
            [
                {
                 "id": "1",
                 "title": "test-title",
                 "price": 15
                }
            ]
            """)
        );
    }


    @Test
    @DirtiesContext
    void addProductTest() throws Exception {
        //GIVEN

        //WHEN
        mockMvc.perform(MockMvcRequestBuilders.post("/api/products")

            .contentType(MediaType.APPLICATION_JSON)
            .content("""
             {
              "title": "test-title",
              "price": 23
             }
            """))

            //THEN
            .andExpect(status().isOk())
            .andExpect(content().json("""
             {
              "title": "test-title",
              "price": 23
             }
            """))
            .andExpect(jsonPath("$.id").isNotEmpty()
        );
    }


    @Test
    @DirtiesContext
    void getProductByIdTest() throws Exception {

        // GIVEN
        String id = "1";
        productRepository.save(new Product("1", "test-title", 15));

        mockMvc.perform( MockMvcRequestBuilders.get("/api/products/{id}", id))
            .andExpect( status().isOk() )
            .andExpect( content().json("""
             {
              "id": "1",
              "title": "test-title",
              "price": 15
             }
            """)
        );
    }


    @Test
    @DirtiesContext
    void putProductTest() throws Exception {

        //GIVEN
        productRepository.save(new Product("1", "test-title", 15));
        String id = "1";

        mockMvc.perform( MockMvcRequestBuilders.put("/api/products/{id}", id)
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
             {
              "id": "1",
              "title": "test-title",
              "price": 20
             }
            """))

            .andExpect( status().isOk() )
            .andExpect( content().json("""
            {
             "id": "1",
             "title": "test-title",
             "price": 20
            }
            """)
        );
    }

    @Test
    @DirtiesContext
    void deleteProductTest() throws Exception {

        // GIVEN
        productRepository.save(new Product("1", "test-title", 15));
        String id = "1";

        mockMvc.perform( MockMvcRequestBuilders.delete("/api/products/{id}", id))
            .andExpect( status().isOk() );
    }


}