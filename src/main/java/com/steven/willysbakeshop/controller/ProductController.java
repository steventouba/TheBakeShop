package com.steven.willysbakeshop.controller;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.steven.willysbakeshop.model.Product;
import com.steven.willysbakeshop.model.ProductDTO;
import com.steven.willysbakeshop.model.User;
import com.steven.willysbakeshop.repository.ProductRepository;
import com.steven.willysbakeshop.repository.UserRepository;
import com.steven.willysbakeshop.utilities.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static com.steven.willysbakeshop.configuration.MediaTypes.TEXT_CSV_VALUE;

@RestController
@RequestMapping(value = "/products")
@Transactional
public class ProductController {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    UserRepository userRepository;

    @GetMapping(value = "/ping", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("OK");
    }

    @GetMapping(value = "/")
    public ResponseEntity<List<Product>> getProducts() {
        List<Product> products = productRepository.findAll();

        return ResponseEntity.ok(products);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable long id) throws NotFoundException {
        Optional<Product> product = productRepository.findById(id);

        if (!product.isPresent()) {
            throw new NotFoundException(String.format("product: %d does not exist", id));
        }

        return ResponseEntity.ok(product.get());
    }

    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Product> createProducts(@RequestBody ProductDTO productDTO ) {
        Optional<User> user = userRepository.findById(productDTO.getSeller());

        if (!user.isPresent()) { throw new NotFoundException("Could not locate listed seller"); }

        Product product = new Product();
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setSeller(user.get());
        productRepository.save(product);

        return ResponseEntity.ok(product);
    }

    @PostMapping(value = "/create", consumes = TEXT_CSV_VALUE)
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

    @PutMapping(value = "/{id}/edit")
    public ResponseEntity<Product> editProduct(@PathVariable long id, @Valid @RequestBody Product toEdit)
            throws NotFoundException{
        Optional<Product> product = productRepository.findById(id);

        if (!product.isPresent()) { throw new NotFoundException("Product not found"); }

        Product body = product.get();
        body.setName(toEdit.getName());
        body.setDescription(toEdit.getDescription());

        productRepository.save(body);

        return ResponseEntity.ok(body);
    }

    @DeleteMapping(value = "{id}/delete")
    public ResponseEntity<Product> deleteProduct(@PathVariable long id) {
        Optional<Product> product = productRepository.findById(id);

        if (!product.isPresent()) { throw new NotFoundException("Product does not exist"); }

        productRepository.delete(product.get());

        return ResponseEntity.ok(product.get());
    }
}
