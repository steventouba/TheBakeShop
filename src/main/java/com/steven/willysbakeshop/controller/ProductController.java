package com.steven.willysbakeshop.controller;

import com.steven.willysbakeshop.model.ProductRequestDTO;
import com.steven.willysbakeshop.model.ProductResponseDTO;
import com.steven.willysbakeshop.service.ProductService;
import com.steven.willysbakeshop.service.S3Service;
import com.steven.willysbakeshop.util.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.List;

@RestController
@RequestMapping(value = "/products")
@Transactional
public class ProductController {

    @Autowired
    ProductService productService;
    @Autowired
    S3Service s3Service;

    @GetMapping(value = "/ping", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("OK");
    }

//    @PostMapping(value = "/test")
//    public ResponseEntity<String> test(
//            @RequestParam("file") MultipartFile file,
//            @RequestParam("name") String name,
//            @RequestParam("description") String description
//    ) {
//        URL imageUrl = s3Service.uploadFile(file);
//        return ResponseEntity.ok(imageUrl.toString());
//    }

    @GetMapping(value = "/")
    public ResponseEntity<List<ProductResponseDTO>> getProducts() {
        List<ProductResponseDTO> products = productService.findAll();

        return ResponseEntity.ok(products);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ProductResponseDTO> getProductById(@PathVariable long id) throws NotFoundException {
       ProductResponseDTO product = productService.getById(id);

        return ResponseEntity.ok(product);
    }

    @PostMapping(value = "/create")
    public ResponseEntity<ProductResponseDTO> createProducts(
            @RequestParam("file") MultipartFile file,
            @RequestParam("name") String name,
            @RequestParam("description") String description
    ) throws NotFoundException {
        ProductRequestDTO requestDTO = new ProductRequestDTO(name, description, file);

        ProductResponseDTO product = productService.registerProduct(requestDTO);

        return ResponseEntity.ok(product);
    }


    @DeleteMapping(value = "/{id}/delete")
    public ResponseEntity<Void> deleteProduct(@PathVariable long id) {
        productService.deleteProduct(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping(value = "/{id}/edit")
    public ResponseEntity<ProductResponseDTO> editProduct(@PathVariable long id,
                                                          @RequestBody ProductRequestDTO toEdit
    ) throws NotFoundException, BadCredentialsException {
        ProductResponseDTO productDetails = productService.editProductDetails(id, toEdit);

        return ResponseEntity.ok(productDetails);
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
}
