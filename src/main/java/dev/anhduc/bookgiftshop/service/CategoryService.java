package dev.anhduc.bookgiftshop.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import dev.anhduc.bookgiftshop.domain.dto.response.ResCreateCategoryDTO;
import dev.anhduc.bookgiftshop.domain.dto.response.ResUpdateCategoryDTO;
import dev.anhduc.bookgiftshop.domain.dto.response.ResultPaginationDTO;
import dev.anhduc.bookgiftshop.domain.entity.Category;
import dev.anhduc.bookgiftshop.repository.CategoryRepository;
import dev.anhduc.bookgiftshop.util.errors.IdInvalidException;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public boolean isExistsByName(String name) {
        Optional<Category> categoryOptional = this.categoryRepository.findByName(name);
        return categoryOptional.isPresent() ? true : false;
    }

    public Category createCategory(Category category) throws IdInvalidException {
        if (this.isExistsByName(category.getName())) {
            throw new IdInvalidException("Category already exists");
        }
        return this.categoryRepository.save(category);
    }

    public ResCreateCategoryDTO convertResCreateCategoryDTO(Category category) {
        ResCreateCategoryDTO res = new ResCreateCategoryDTO();
        res.setId(category.getId());
        res.setName(category.getName());
        res.setDescription(category.getDescription());
        res.setCreatedAt(category.getCreatedAt());
        res.setCreatedBy(category.getCreatedBy());
        return res;
    }

    public Category fetchCategoryById(Long id) {
        Optional<Category> categoryOptional = this.categoryRepository.findById(id);
        return categoryOptional.isPresent() ? categoryOptional.get() : null;
    }

    public ResultPaginationDTO fetchAllCategories(Specification<Category> specification, Pageable pageable) {
        Page<Category> categoryPage = this.categoryRepository.findAll(specification, pageable);
        ResultPaginationDTO result = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();
        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());
        meta.setPages(categoryPage.getTotalPages());
        meta.setTotal(categoryPage.getTotalElements());
        result.setMeta(meta);
        result.setResult(categoryPage.getContent());
        return result;
    }

    public Category updateCategory(Long id, Category requestCategory) {
        Category currenCategory = this.fetchCategoryById(id);
        currenCategory.setDescription(requestCategory.getDescription());
        currenCategory.setName(requestCategory.getName());
        return this.categoryRepository.save(currenCategory);
    }

    public ResUpdateCategoryDTO convertToUpdateCategoryDTO(Category category) {
        ResUpdateCategoryDTO result = new ResUpdateCategoryDTO();
        result.setId(category.getId());
        result.setName(category.getName());
        result.setDescription(category.getDescription());
        result.setUpdatedAt(category.getUpdatedAt());
        result.setUpdatedBy(category.getUpdatedBy());
        return result;
    }

    public void deleteCategoryById(Category category) {
        category.setDeleted(true);
        this.categoryRepository.save(category);
    }
}
