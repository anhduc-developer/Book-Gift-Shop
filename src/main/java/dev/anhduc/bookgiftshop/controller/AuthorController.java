package dev.anhduc.bookgiftshop.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import dev.anhduc.bookgiftshop.dto.response.ResAuthorDTO;
import dev.anhduc.bookgiftshop.dto.response.ResCreateAuthorDTO;
import dev.anhduc.bookgiftshop.dto.response.ResUpdateAuthorDTO;
import dev.anhduc.bookgiftshop.dto.response.ResultPaginationDTO;
import dev.anhduc.bookgiftshop.entity.Author;
import dev.anhduc.bookgiftshop.exception.IdInvalidException;
import dev.anhduc.bookgiftshop.service.AuthorService;
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
public class AuthorController {
    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @PostMapping("/admin/authors")
    @ApiMessage("Create New Author")
    public ResponseEntity<ResCreateAuthorDTO> createAuthor(@Valid @RequestBody Author author) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.authorService.creatAuthor(author));
    }

    @GetMapping("/admin/authors/{id}")
    @ApiMessage("Fetch Author By Id")
    public ResponseEntity<ResAuthorDTO> fetchAuthorById(@PathVariable("id") Long id) throws IdInvalidException {
        Author author = this.authorService.fetchAuthorById(id);
        return ResponseEntity.ok().body(this.authorService.convertAuthorDTO(author));
    }

    @GetMapping("/admin/authors")
    @ApiMessage("Fetch All Authors")
    public ResponseEntity<ResultPaginationDTO> fetchAllAuthor(@Filter Specification<Author> specification,
            Pageable pageable) {
        return ResponseEntity.ok().body(this.authorService.fetchAllAuthors(specification, pageable));
    }

    @PutMapping("/admin/authors/{id}")
    @ApiMessage("Update Author By Id")
    public ResponseEntity<ResUpdateAuthorDTO> updateAuthorById(@PathVariable("id") Long id,
            @Valid @RequestBody Author requestAuthor) throws IdInvalidException {
        return ResponseEntity.ok().body(this.authorService.updateAuthor(id, requestAuthor));
    }

    @DeleteMapping("/admin/authors/{id}")
    @ApiMessage("Delete Author By Id")
    public ResponseEntity<Void> deleteAuthorById(@PathVariable("id") Long id) throws IdInvalidException {
        this.authorService.deleteAuthorById(id);
        return ResponseEntity.ok(null);
    }
}
