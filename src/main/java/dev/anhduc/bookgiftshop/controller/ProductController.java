package dev.anhduc.bookgiftshop.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.anhduc.bookgiftshop.service.ProductService;

@RestController
@RequestMapping("/api/v1")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // @PostMapping("/products")
    // @ApiMessage("Create New Product")
    // public ResponseEntity<Product> createProduct(@Valid @RequestBody Product
    // product) {

    // }
}
