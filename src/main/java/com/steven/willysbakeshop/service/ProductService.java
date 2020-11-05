package com.steven.willysbakeshop.service;

import com.steven.willysbakeshop.model.Product;
import com.steven.willysbakeshop.model.ProductDTO;
import com.steven.willysbakeshop.model.User;
import com.steven.willysbakeshop.repository.ProductRepository;
import com.steven.willysbakeshop.repository.UserRepository;
import com.steven.willysbakeshop.utilities.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;


    @Transactional
    public Product registerProduct(ProductDTO productDTO) throws NotFoundException {
        Optional<User> user = userRepository.findById(productDTO.getSeller());

        user.orElseThrow(() -> new NotFoundException("Could not locate listed seller"));

        Product product = new Product();
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        user.get().setProducts(product);
        productRepository.save(product);

        return product;
    }

    public ProductDTO getById(long id) throws NotFoundException {
        Optional<Product> product = productRepository.findById(id);

        product.orElseThrow(() -> new NotFoundException("Could not locate requested product"));

        ProductDTO productDTO = new ProductDTO();
        productDTO.setName(product.get().getName());
        productDTO.setDescription(product.get().getDescription());
        productDTO.setSeller(product.get().getSeller().getId());

        return productDTO;
    }

    public List<ProductDTO> findAll() {
        List<Product> products = productRepository.findAll();

        List<ProductDTO> collect = products
                .stream()
                .map(product -> {
                    ProductDTO productDTO = new ProductDTO();
                    productDTO.setName(product.getName());
                    productDTO.setDescription(product.getDescription());
                    productDTO.setSeller(product.getSeller().getId());
                    return productDTO;
                })
                .collect(Collectors.toList());

        return collect;
    }
}
