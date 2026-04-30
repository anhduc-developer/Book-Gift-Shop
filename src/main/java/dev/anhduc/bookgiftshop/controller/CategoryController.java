package dev.anhduc.bookgiftshop.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import dev.anhduc.bookgiftshop.dto.response.ResCreateCategoryDTO;
import dev.anhduc.bookgiftshop.dto.response.ResUpdateCategoryDTO;
import dev.anhduc.bookgiftshop.dto.response.ResultPaginationDTO;
import dev.anhduc.bookgiftshop.entity.Category;
import dev.anhduc.bookgiftshop.exception.IdInvalidException;
import dev.anhduc.bookgiftshop.service.CategoryService;
import dev.anhduc.bookgiftshop.utils.annotation.ApiMessage;
import jakarta.validation.Valid;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/v1")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/categories")
    @ApiMessage("Create New Category")
    public ResponseEntity<ResCreateCategoryDTO> createCategory(@Valid @RequestBody Category category)
            throws IdInvalidException {
        Category result = this.categoryService.createCategory(category);
        return ResponseEntity.status(HttpStatus.CREATED).body(this.categoryService.convertResCreateCategoryDTO(result));
    }

    @GetMapping("/categories/{id}")
    @ApiMessage("Fetch Category By Id")
    public ResponseEntity<Category> fetchCategoryById(@PathVariable("id") Long id) throws IdInvalidException {
        Category result = this.categoryService.fetchCategoryById(id);
        if (result == null) {
            throw new IdInvalidException("Category với id = " + id + " không tồn tại!");
        }
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/categories")
    @ApiMessage("Fetch All Categories")
    public ResponseEntity<ResultPaginationDTO> fetchAllCategories(@Filter Specification<Category> specification,
            Pageable pageable) {
        return ResponseEntity.ok().body(this.categoryService.fetchAllCategories(specification, pageable));
    }

    @PutMapping("/categories/{id}")
    @ApiMessage("Update Category By Id")
    public ResponseEntity<ResUpdateCategoryDTO> updateCategoryById(@Valid @PathVariable("id") Long id,
            @RequestBody Category requestCategory) throws IdInvalidException {
        Category category = this.categoryService.fetchCategoryById(id);
        if (category == null) {
            throw new IdInvalidException("Category với id = " + id + " không tồn tại!");
        }
        category = this.categoryService.updateCategory(id, requestCategory);
        return ResponseEntity.ok().body(this.categoryService.convertToUpdateCategoryDTO(category));
    }

    @DeleteMapping("/categories/{id}")
    @ApiMessage("Delete Category By Id")
    public ResponseEntity<Void> deleteCategoryById(@PathVariable("id") Long id) throws IdInvalidException {
        Category category = this.categoryService.fetchCategoryById(id);
        if (category == null) {
            throw new IdInvalidException("Category với id = " + id + " không tồn tại!");
        }
        this.categoryService.deleteCategoryById(category);
        return ResponseEntity.ok().body(null);
    }
}
