package dev.anhduc.bookgiftshop.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import dev.anhduc.bookgiftshop.domain.dto.response.ResCreateProductDTO;
import dev.anhduc.bookgiftshop.domain.entity.Author;
import dev.anhduc.bookgiftshop.domain.entity.Product;
import dev.anhduc.bookgiftshop.repository.AuthorRepository;
import dev.anhduc.bookgiftshop.repository.ProductRepository;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final AuthorRepository authorRepository;

    public ProductService(ProductRepository productRepository, AuthorRepository authorRepository) {
        this.productRepository = productRepository;
        this.authorRepository = authorRepository;
    }

    // public ResCreateProductDTO createProduct(Product product) {
    // if (product.getAuthors() != null) {
    // List<Author> authorOptional = this.authorRepository.findById);
    // }
    // }
}
