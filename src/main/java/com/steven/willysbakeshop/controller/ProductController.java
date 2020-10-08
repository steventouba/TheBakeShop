package com.steven.willysbakeshop.controller;

import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.steven.willysbakeshop.model.Product;
import com.steven.willysbakeshop.repository.ProductRepository;
import com.steven.willysbakeshop.utilities.exceptions.ProductNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/products")
public class ProductController {
    public final static String CSV = "text/csv";


    @Autowired
    ProductRepository productRepository;

    @GetMapping(value = "/")
    public ResponseEntity<List<Product>> getProducts() {
        List<Product> products = productRepository.findAll();

        return ResponseEntity.ok(products);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable long id) throws ProductNotFoundException {
        Optional<Product> product = productRepository.findById(id);

        if (!product.isPresent()) { throw new ProductNotFoundException(String.format("product: %d does not exist", id)); }

        return ResponseEntity.ok(product.get());
    }

    @PostMapping(value = "/create")
    public ResponseEntity<String> createProducts(@RequestBody MultipartFile file) {
        new CsvMapper().readerWithSchemaFor(Product.class);

        return ResponseEntity.ok("OK");
    }

}
