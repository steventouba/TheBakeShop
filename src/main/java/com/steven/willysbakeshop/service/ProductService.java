package com.steven.willysbakeshop.service;

import com.steven.willysbakeshop.model.*;
import com.steven.willysbakeshop.repository.ProductRepository;
import com.steven.willysbakeshop.repository.UserRepository;
import com.steven.willysbakeshop.security.AuthenticationFacade;
import com.steven.willysbakeshop.util.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
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

    @Autowired S3Service s3Service;

    @Autowired
    private AuthenticationFacade authenticationFacade;

    public List<ProductResponseDTO> findAll() {
        List<Product> products = productRepository.findAll();

       return products
                .stream()
                .map(product -> new ProductResponseDTO.Builder(
                        product.getId(),
                        product.getName(),
                        product.getDescription())
//                            .withSeller(mapSellerDetails(product.getSeller()))
                        .withSelfUrl(product.getId())
                        .withImageUrl(product.getImageUrl())
                        .build()).collect(Collectors.toList());
    }

    public ProductResponseDTO getById(long id) throws NotFoundException {
        Optional<Product> productOptional = productRepository.findById(id);
        productOptional.orElseThrow(() -> new NotFoundException("Could not locate requested product"));

        Product product = productOptional.get();
        return new ProductResponseDTO.Builder(
                product.getId(),
                product.getName(),
                product.getDescription())
                .withSeller(mapSellerDetails(product.getSeller()))
                .build();
    }

    @Transactional
    public ProductResponseDTO registerProduct(ProductRequestDTO productRequestDTO) throws NotFoundException {
        Authentication authentication = authenticationFacade.getAuthentication();
        Optional<User> user = userRepository.findByEmail(authentication.getName());
        user.orElseThrow(() -> new NotFoundException("Could not locate listed seller"));

        Product product = new Product();
        product.setName(productRequestDTO.getName());
        product.setDescription(productRequestDTO.getDescription());
        user.get().setProducts(product);
        product.setImageUrl(s3Service.uploadFile(productRequestDTO.getImage()).toString());
        productRepository.save(product);

        return new ProductResponseDTO.Builder(
                product.getId(),
                product.getName(),
                product.getDescription())
                .withSeller(mapSellerDetails(product.getSeller()))
                .withSelfUrl(product.getId())
                .withImageUrl(product.getImageUrl())
                .build();
    }

    @Transactional
    public ProductResponseDTO editProductDetails(long productId,
                                                ProductRequestDTO productRequestDTO
    ) throws NotFoundException, BadCredentialsException {
        Optional<Product> optional = productRepository.findById(productId);
        optional.orElseThrow(() -> new NotFoundException("Could not locate requested product"));

        Product product = optional.get();
        if (!authenticationFacade.isPrincipalMatch(product.getSeller().getEmail())) {
            throw new BadCredentialsException("Unauthorized");
        }

        product.setName(productRequestDTO.getName());
        product.setDescription(productRequestDTO.getDescription());
        productRepository.save(product);

        return new ProductResponseDTO.Builder(
                product.getId(),
                product.getName(),
                product.getDescription())
                .withSeller(mapSellerDetails(product.getSeller()))
                .withSelfUrl(product.getId())
                .build();
    }

    @Transactional
    public void deleteProduct(long id) {
        Optional<Product> optional = productRepository.findById(id);
        optional.orElseThrow(() -> new NotFoundException("Could not locate requested product"));

        Product product = optional.get();
        if (!authenticationFacade.isPrincipalMatch(product.getSeller().getEmail())) {
            throw new BadCredentialsException("Unauthorized");
        }
        productRepository.delete(product);
    }

    private UserResponseDTO mapSellerDetails(User user) {
        return new UserResponseDTO.Builder(
                user.getFirstName(),
                user.getLastName(),
                user.getEmail()
        )
                .withSelfLink(user.getId())
                .build();
    }
}
