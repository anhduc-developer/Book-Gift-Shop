package dev.anhduc.bookgiftshop.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import dev.anhduc.bookgiftshop.dto.response.ResCreateProduct;
import dev.anhduc.bookgiftshop.dto.response.ResUpdateProductDTO;
import dev.anhduc.bookgiftshop.dto.response.ResultPaginationDTO;
import dev.anhduc.bookgiftshop.entity.Author;
import dev.anhduc.bookgiftshop.entity.Category;
import dev.anhduc.bookgiftshop.entity.Product;
import dev.anhduc.bookgiftshop.entity.Promotion;
import dev.anhduc.bookgiftshop.entity.Publisher;
import dev.anhduc.bookgiftshop.exception.IdInvalidException;
import dev.anhduc.bookgiftshop.repository.AuthorRepository;
import dev.anhduc.bookgiftshop.repository.CategoryRepository;
import dev.anhduc.bookgiftshop.repository.ProductRepository;
import dev.anhduc.bookgiftshop.repository.PromotionRepository;
import dev.anhduc.bookgiftshop.repository.PublisherRepository;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final AuthorRepository authorRepository;
    private final CategoryRepository categoryRepository;
    private final PromotionRepository promotionRepository;
    private final PublisherRepository publisherRepository;

    public ProductService(ProductRepository productRepository, AuthorRepository authorRepository,
            CategoryRepository categoryRepository, PromotionRepository promotionRepository,
            PublisherRepository publisherRepository) {
        this.productRepository = productRepository;
        this.authorRepository = authorRepository;
        this.categoryRepository = categoryRepository;
        this.promotionRepository = promotionRepository;
        this.publisherRepository = publisherRepository;
    }

    public ResCreateProduct createProduct(Product product) {
        if (product.getAuthors() != null) {
            List<Long> requestAuthors = product.getAuthors().stream().map(x -> x.getId()).collect(Collectors.toList());
            List<Author> authors = this.authorRepository.findByIdIn(requestAuthors);
            product.setAuthors(authors);
        }
        if (product.getCategories() != null) {
            List<Long> requestCategories = product.getCategories().stream().map(x -> x.getId())
                    .collect(Collectors.toList());
            List<Category> categories = this.categoryRepository.findByIdIn(requestCategories);
            product.setCategories(categories);
        }
        if (product.getPromotions() != null) {
            List<Long> requestPromotions = product.getPromotions().stream().map(x -> x.getId())
                    .collect(Collectors.toList());
            List<Promotion> promotions = this.promotionRepository.findByIdIn(requestPromotions);
            product.setPromotions(promotions);
        }
        if (product.getPublisher() != null) {
            Optional<Publisher> publisherOptional = this.publisherRepository.findById(product.getPublisher().getId());
            if (publisherOptional.isPresent()) {
                product.setPublisher(publisherOptional.get());
            }
        }
        Product savedProduct = this.productRepository.save(product);
        ResCreateProduct res = new ResCreateProduct();
        res.setId(savedProduct.getId());
        res.setName(savedProduct.getName());
        res.setDetailDescription(savedProduct.getDetailDescription());
        res.setPhoto(savedProduct.getPhoto());
        res.setShortDescription(savedProduct.getShortDescription());
        res.setSold(savedProduct.getSold());
        res.setCreatedAt(savedProduct.getCreatedAt());
        res.setCreatedBy(savedProduct.getCreatedBy());
        res.setStockQuantity(savedProduct.getStockQuantity());
        res.setPrice(savedProduct.getPrice());
        if (savedProduct.getPublisher() != null) {
            res.setPublisher(savedProduct.getPublisher().getName());
        }
        if (savedProduct.getAuthors() != null) {
            res.setAuthors(savedProduct.getAuthors().stream().map(Author::getName).toList());
        }
        if (savedProduct.getCategories() != null) {
            res.setCategories(savedProduct.getCategories().stream().map(Category::getName).toList());
        }
        if (savedProduct.getPromotions() != null) {
            res.setPromotions(savedProduct.getPromotions().stream().map(Promotion::getName).toList());
        }
        return res;
    }

    public ResultPaginationDTO fetchAllProducts(Specification<Product> specification, Pageable pageable) {
        Page<Product> productPage = this.productRepository.findAll(specification, pageable);
        ResultPaginationDTO result = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();
        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());
        meta.setPages(productPage.getTotalPages());
        meta.setTotal(productPage.getTotalElements());
        result.setMeta(meta);
        result.setResult(productPage.getContent());
        return result;
    }

    public Product fetchProductById(Long id) throws IdInvalidException {
        Optional<Product> productOptional = this.productRepository.findById(id);
        if (productOptional.isEmpty()) {
            throw new IdInvalidException("Product với id = " + id + " không tồn tại!");
        }
        return productOptional.get();
    }

    public ResUpdateProductDTO updateProductById(Long id, Product requestProduct) throws IdInvalidException {
        Optional<Product> productOptional = this.productRepository.findById(id);
        if (productOptional.isEmpty()) {
            throw new IdInvalidException("Product với id = " + id + " không tồn tại!");
        }
        if (requestProduct.getAuthors() != null) {
            List<Long> requestAuthors = requestProduct.getAuthors().stream().map(x -> x.getId())
                    .collect(Collectors.toList());
            List<Author> authors = this.authorRepository.findByIdIn(requestAuthors);
            requestProduct.setAuthors(authors);
        }
        if (requestProduct.getCategories() != null) {
            List<Long> requestCategories = requestProduct.getCategories().stream().map(x -> x.getId())
                    .collect(Collectors.toList());
            List<Category> categories = this.categoryRepository.findByIdIn(requestCategories);
            requestProduct.setCategories(categories);
        }
        if (requestProduct.getPromotions() != null) {
            List<Long> requestPromotions = requestProduct.getPromotions().stream().map(x -> x.getId())
                    .collect(Collectors.toList());
            List<Promotion> promotions = this.promotionRepository.findByIdIn(requestPromotions);
            requestProduct.setPromotions(promotions);
        }
        if (requestProduct.getPublisher() != null) {
            Optional<Publisher> publisherOptional = this.publisherRepository
                    .findById(requestProduct.getPublisher().getId());
            if (publisherOptional.isPresent()) {
                requestProduct.setPublisher(publisherOptional.get());
            }
        }
        Product savedProduct = this.productRepository.save(requestProduct);
        ResUpdateProductDTO res = new ResUpdateProductDTO();
        res.setPrice(savedProduct.getPrice());
        res.setId(savedProduct.getId());
        res.setName(savedProduct.getName());
        res.setDetailDescription(savedProduct.getDetailDescription());
        res.setPhoto(savedProduct.getPhoto());
        res.setShortDescription(savedProduct.getShortDescription());
        res.setSold(savedProduct.getSold());
        res.setUpdatedAt(savedProduct.getUpdatedAt());
        res.setUpdatedBy(savedProduct.getUpdatedBy());
        res.setStockQuantity(savedProduct.getStockQuantity());
        if (savedProduct.getPublisher() != null) {
            res.setPublisher(savedProduct.getPublisher().getName());
        }
        if (savedProduct.getAuthors() != null) {
            res.setAuthors(savedProduct.getAuthors().stream().map(Author::getName).toList());
        }
        if (savedProduct.getCategories() != null) {
            res.setCategories(savedProduct.getCategories().stream().map(Category::getName).toList());
        }
        if (savedProduct.getPromotions() != null) {
            res.setPromotions(savedProduct.getPromotions().stream().map(Promotion::getName).toList());
        }
        return res;
    }

    public void deleteProductById(Long id) throws IdInvalidException {
        Optional<Product> productOptional = this.productRepository.findById(id);
        if (productOptional.isEmpty()) {
            throw new IdInvalidException("Product với id = " + id + " không tồn tại!");
        }
        Product currentProduct = productOptional.get();
        currentProduct.setDeleted(true);
        this.productRepository.save(currentProduct);
    }
}
