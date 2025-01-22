package com.ecom.productcatalogservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RestController
@RequestMapping("api/v1/products")
public class ProductCatalogController
{
    private static final Logger logger = LoggerFactory.getLogger(ProductCatalogController.class.getName());
    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductMapper productMapper;

    @Autowired
    Producer producer = new Producer();

    @PostMapping("/update") // URIs SERVE CHUNKS OF DATA UNLIKE URLs WHICH SERVE PAGES
    public ResponseEntity<String> updateProductDetails(@RequestBody Product product) throws JsonProcessingException {
        logger.info("initiating product update in Product Catalog Controller");
        productRepository.save(product);
        logger.info(" product update completed successfully in productCatalog Table");
        logger.info(product.getProductname()," initiating product topic");
        producer.pubUpdateProductDetailsMessage(product.getProductname(), "PRODUCT DETAILS UPDATED SUCCESSFULLY");

        return ResponseEntity.ok("Details Updated Successfully");
    }


    @GetMapping("/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable String id) {
        Optional<Product> product = productRepository.findById(id);
        return product.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/getAll")
    public ResponseEntity<?> getAllProducts() {
        try {
            List<Product> products = productRepository.findAll();

            if (products.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No products available.");
            }

            return ResponseEntity.ok(products.stream()
                    .map(productMapper::toProductView)
                    .collect(Collectors.toList()));

        } catch (Exception e) {
            // Log the error (logging can be added here for production)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while fetching products: " + e.getMessage());
        }
    }


    @GetMapping("/all")
    public List<ProductView>  fetchAllProducts() {
        try {
            List<Product> products = productRepository.findAll();

            if (products.isEmpty()) {
                return new ArrayList<>();
            }

            return (products.stream()
                    .map(productMapper::toProductView)
                    .collect(Collectors.toList()));

        } catch (Exception e) {
            // Log the error (logging can be added here for production)
            return new ArrayList<>();
        }
    }
}



