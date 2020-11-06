package com.steven.willysbakeshop.service;

import com.steven.willysbakeshop.model.Product;
import com.steven.willysbakeshop.model.ProductDTO;
import com.steven.willysbakeshop.model.User;
import com.steven.willysbakeshop.model.UserDTO;
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

    public List<ProductDTO> findAll() {
        List<Product> products = productRepository.findAll();

       return products
                .stream()
                .map(product -> {
                    ProductDTO productDTO = new ProductDTO();
                    productDTO.setName(product.getName());
                    productDTO.setDescription(product.getDescription());
                    productDTO.setSeller(mapSellerDetails(product.getSeller()));
                    return productDTO;
                })
                .collect(Collectors.toList());
    }

    public ProductDTO getById(long id) throws NotFoundException {
        Optional<Product> productOptional = productRepository.findById(id);

        productOptional.orElseThrow(() -> new NotFoundException("Could not locate requested product"));

        Product product = productOptional.get();


        ProductDTO productDTO = new ProductDTO();
        productDTO.setName(product.getName());
        productDTO.setDescription(product.getDescription());
        productDTO.setSeller(mapSellerDetails(product.getSeller()));

        return productDTO;
    }

    @Transactional
    public ProductDTO registerProduct(ProductDTO productDTO) throws NotFoundException {
        Optional<User> user = userRepository.findById(productDTO.getSeller().getId());

        user.orElseThrow(() -> new NotFoundException("Could not locate listed seller"));

        Product product = new Product();
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        user.get().setProducts(product);
        productRepository.save(product);

        return new ProductDTO(
                product.getName(),
                product.getDescription(),
                mapSellerDetails(product.getSeller())
        );
    }

    private UserDTO mapSellerDetails(User user) {
        return new UserDTO.Builder(
                user.getFirstName(),
                user.getLastName(),
                user.getEmail()
        )
                .build();
    }

}
