package dev.anhduc.bookgiftshop.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import dev.anhduc.bookgiftshop.dto.response.ResCreateProduct;
import dev.anhduc.bookgiftshop.dto.response.ResProductDTO;
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
        if (product.getAuthors() != null) {
            List<Long> requestAuthors = product.getAuthors().stream().map(x -> x.getId()).collect(Collectors.toList());
            List<Author> authors = this.authorRepository.findByIdIn(requestAuthors);
            product.setAuthors(authors);
            this.productRepository.save(product);
            authors.forEach(u -> {
                List<Product> productsAuthor = u.getProducts();
                productsAuthor.add(product);
                u.setProducts(productsAuthor);
            });
            this.authorRepository.saveAll(authors);
        }
        Product savedProduct = this.productRepository.save(product);
        ResCreateProduct res = new ResCreateProduct();
        ResCreateProduct.PublishserDTO publishserDTO = new ResCreateProduct.PublishserDTO();
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
            publishserDTO.setId(savedProduct.getPublisher().getId());
            publishserDTO.setName(savedProduct.getPublisher().getName());
            res.setPublisher(publishserDTO);
        }
        if (savedProduct.getAuthors() != null) {
            List<ResCreateProduct.AuthorDTO> authorDTOs = savedProduct.getAuthors().stream()
                    .map(x -> new ResCreateProduct.AuthorDTO(x.getId(), x.getName())).toList();
            res.setAuthors(authorDTOs);
        }
        if (savedProduct.getCategories() != null) {
            List<ResCreateProduct.CategoryDTO> categoryDTOs = savedProduct.getCategories().stream()
                    .map(x -> new ResCreateProduct.CategoryDTO(x.getId(), x.getName()))
                    .toList();
            res.setCategories(categoryDTOs);
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
        List<ResProductDTO> res = productPage.getContent().stream().map(x -> this.convertToProductDTO(x)).toList();
        result.setResult(res);
        return result;
    }

    public ResProductDTO fetchProductById(Long id) throws IdInvalidException {
        Optional<Product> productOptional = this.productRepository.findById(id);
        if (productOptional.isEmpty()) {
            throw new IdInvalidException("Product với id = " + id + " không tồn tại!");
        }
        Product product = productOptional.get();
        return this.convertToProductDTO(product);
    }

    public ResProductDTO convertToProductDTO(Product product) {
        ResProductDTO res = new ResProductDTO();
        res.setId(product.getId());
        res.setCreatedAt(product.getCreatedAt());
        res.setCreatedBy(product.getCreatedBy());
        res.setDetailDescription(product.getDetailDescription());
        res.setName(product.getName());
        res.setPhoto(product.getPhoto());
        res.setPrice(product.getPrice());
        res.setShortDescription(product.getShortDescription());
        res.setSold(product.getSold());
        res.setStockQuantity(product.getStockQuantity());
        res.setUpdatedAt(product.getUpdatedAt());
        res.setUpdatedBy(product.getUpdatedBy());
        if (product.getAuthors() != null) {
            List<ResProductDTO.AuthorDTO> authorDTOs = product.getAuthors().stream()
                    .map(x -> new ResProductDTO.AuthorDTO(x.getId(), x.getName())).toList();
            res.setAuthors(authorDTOs);
        }
        if (product.getPublisher() != null) {
            ResProductDTO.PublishserDTO publishserDTO = new ResProductDTO.PublishserDTO(product.getPublisher().getId(),
                    product.getPublisher().getName());
            res.setPublisher(publishserDTO);
        }

        if (product.getCategories() != null) {
            List<ResProductDTO.CategoryDTO> categoryDTOs = product.getCategories().stream()
                    .map(x -> new ResProductDTO.CategoryDTO(x.getId(), x.getName()))
                    .toList();
            res.setCategories(categoryDTOs);
        }

        return res;
    }

    public ResUpdateProductDTO updateProductById(Long id, Product requestProduct) throws IdInvalidException {
        Optional<Product> productOptional = this.productRepository.findById(id);

        if (productOptional.isEmpty()) {
            throw new IdInvalidException("Product với id = " + id + " không tồn tại!");
        }
        Product product = productOptional.get();
        if (requestProduct.getCategories() != null) {
            List<Long> requestCategories = requestProduct.getCategories().stream().map(x -> x.getId())
                    .collect(Collectors.toList());
            List<Category> categories = this.categoryRepository.findByIdIn(requestCategories);
            product.setCategories(categories);
        }
        if (requestProduct.getPromotions() != null) {
            List<Long> requestPromotions = requestProduct.getPromotions().stream().map(x -> x.getId())
                    .collect(Collectors.toList());
            List<Promotion> promotions = this.promotionRepository.findByIdIn(requestPromotions);
            product.setPromotions(promotions);
        }
        if (requestProduct.getPublisher() != null) {
            Optional<Publisher> publisherOptional = this.publisherRepository
                    .findById(requestProduct.getPublisher().getId());
            if (publisherOptional.isPresent()) {
                product.setPublisher(publisherOptional.get());
            }
        }
        if (requestProduct.getAuthors() != null) {
            List<Long> requestAuthors = requestProduct.getAuthors().stream().map(x -> x.getId())
                    .collect(Collectors.toList());
            List<Author> authors = this.authorRepository.findByIdIn(requestAuthors);
            for (Author author : authors) {
                author.getProducts().add(product);
            }
            authorRepository.saveAll(authors);
        }
        product.setDetailDescription(requestProduct.getDetailDescription());
        product.setName(requestProduct.getName());
        product.setPhoto(requestProduct.getPhoto());
        product.setPrice(requestProduct.getPrice());
        product.setShortDescription(requestProduct.getShortDescription());
        product.setSold(requestProduct.getSold());
        product.setStockQuantity(requestProduct.getStockQuantity());
        product = this.productRepository.save(product);
        ResUpdateProductDTO res = new ResUpdateProductDTO();
        ResUpdateProductDTO.PublishserDTO publishserDTO = new ResUpdateProductDTO.PublishserDTO();
        res.setId(product.getId());
        res.setName(product.getName());
        res.setDetailDescription(product.getDetailDescription());
        res.setPhoto(product.getPhoto());
        res.setShortDescription(product.getShortDescription());
        res.setSold(product.getSold());
        res.setUpdatedAt(product.getUpdatedAt());
        res.setUpdatedBy(product.getUpdatedBy());
        res.setStockQuantity(product.getStockQuantity());
        res.setPrice(product.getPrice());
        if (product.getPublisher() != null) {
            publishserDTO.setId(product.getPublisher().getId());
            publishserDTO.setName(product.getPublisher().getName());
            res.setPublisher(publishserDTO);
        }
        if (product.getAuthors() != null) {
            List<ResUpdateProductDTO.AuthorDTO> authorDTOs = product.getAuthors().stream()
                    .map(x -> new ResUpdateProductDTO.AuthorDTO(x.getId(), x.getName())).toList();
            res.setAuthors(authorDTOs);
        }
        if (product.getCategories() != null) {
            List<ResUpdateProductDTO.CategoryDTO> categoryDTOs = product.getCategories().stream()
                    .map(x -> new ResUpdateProductDTO.CategoryDTO(x.getId(), x.getName()))
                    .toList();
            res.setCategories(categoryDTOs);
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
