package com.steven.willysbakeshop.controller;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.steven.willysbakeshop.model.Product;
import com.steven.willysbakeshop.repository.ProductRepository;
import com.steven.willysbakeshop.utilities.exceptions.ProductNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/products")
@Transactional
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

    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Product> createProducts(@RequestBody Product newProduct ) {

        Product product = productRepository.save(newProduct);

        return ResponseEntity.ok(product);
    }

    @PostMapping(value = "/create")
    public ResponseEntity<String> createProducts(@RequestBody String csv) throws IOException {

        MappingIterator<Product> iterator = new CsvMapper()
                .readerFor(Product.class)
                .with(CsvSchema.emptySchema().withHeader())
                .readValues(csv);

        while (iterator.hasNext()) {
            productRepository.save(iterator.next());
        }

        return ResponseEntity.ok("OK");
    }

}
