package dev.anhduc.bookgiftshop.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import dev.anhduc.bookgiftshop.dto.response.ResCreateProduct;
import dev.anhduc.bookgiftshop.dto.response.ResProductDTO;
import dev.anhduc.bookgiftshop.dto.response.ResUpdateProductDTO;
import dev.anhduc.bookgiftshop.dto.response.ResultPaginationDTO;
import dev.anhduc.bookgiftshop.dto.response.ResCartDTO.ResProduct;
import dev.anhduc.bookgiftshop.entity.Product;
import dev.anhduc.bookgiftshop.exception.IdInvalidException;
import dev.anhduc.bookgiftshop.service.ProductService;
import dev.anhduc.bookgiftshop.utils.annotation.ApiMessage;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/v1/admin")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/products")
    @ApiMessage("Create New Product")
    public ResponseEntity<ResCreateProduct> createProduct(@Valid @RequestBody Product product) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.productService.createProduct(product));
    }

    @GetMapping("/products")
    @ApiMessage("Fetch All Products")
    public ResponseEntity<ResultPaginationDTO> fetchAllProducts(@Filter Specification<Product> specification,
            Pageable pageable) {
        return ResponseEntity.ok().body(this.productService.fetchAllProducts(specification, pageable));
    }

    @GetMapping("/products/{id}")
    @ApiMessage("Fetch Product By Id")
    public ResponseEntity<ResProductDTO> fetchProductById(@PathVariable("id") Long id) throws IdInvalidException {
        return ResponseEntity.ok().body(this.productService.fetchProductById(id));
    }

    @PutMapping("/products/{id}")
    @ApiMessage("Update Product By Id")
    public ResponseEntity<ResUpdateProductDTO> updateProductById(@PathVariable("id") Long id,
            @RequestBody Product requestProduct) throws IdInvalidException {
        return ResponseEntity.ok().body(this.productService.updateProductById(id, requestProduct));
    }

    @DeleteMapping("/products/{id}")
    @ApiMessage("Delete Product By Id")
    public ResponseEntity<Void> deleteProductById(@PathVariable("id") Long id) throws IdInvalidException {
        this.productService.deleteProductById(id);
        return ResponseEntity.ok(null);
    }
}
