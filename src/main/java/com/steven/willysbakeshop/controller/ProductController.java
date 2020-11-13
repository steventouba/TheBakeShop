package com.steven.willysbakeshop.controller;

import com.steven.willysbakeshop.model.ProductDTO;
import com.steven.willysbakeshop.service.ProductService;
import com.steven.willysbakeshop.util.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;

@RestController
@RequestMapping(value = "/products")
@Transactional
public class ProductController {

    @Autowired
    ProductService productService;

    @GetMapping(value = "/ping", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("OK");
    }

    @GetMapping(value = "/")
    public ResponseEntity<List<ProductDTO>> getProducts() {
        List<ProductDTO> products = productService.findAll();

        return ResponseEntity.ok(products);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable long id) throws NotFoundException {
       ProductDTO productDTO = productService.getById(id);

        return ResponseEntity.ok(productDTO);
    }

    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProductDTO> createProducts(@RequestBody ProductDTO productDTO ) throws NotFoundException {
        ProductDTO registeredProduct = productService.registerProduct(productDTO);

        return ResponseEntity.ok(registeredProduct);
    }

//    @PostMapping(value = "/create", consumes = TEXT_CSV_VALUE)
//    public ResponseEntity<String> createProducts(@RequestBody String csv) throws IOException {
//
//        MappingIterator<Product> iterator = new CsvMapper()
//                .readerFor(Product.class)
//                .with(CsvSchema.emptySchema().withHeader())
//                .readValues(csv);
//
//        while (iterator.hasNext()) {
//            productRepository.save(iterator.next());
//        }
//
//        return ResponseEntity.ok("OK");
//    }

    @PutMapping(value = "/{id}/edit")
    public ResponseEntity<ProductDTO> editProduct(@PathVariable long id, @RequestBody ProductDTO toEdit)
            throws NotFoundException, BadCredentialsException {
        ProductDTO productDetails = productService.editProductDetails(id, toEdit);

        return ResponseEntity.ok(productDetails);
    }

    @DeleteMapping(value = "/{id}/delete")
    public ResponseEntity<String> deleteProduct(@PathVariable long id) {
        productService.deleteProduct(id);

        return ResponseEntity.status(201).body("Success");
    }
}
