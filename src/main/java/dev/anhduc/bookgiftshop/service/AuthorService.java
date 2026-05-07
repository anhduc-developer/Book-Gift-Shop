package dev.anhduc.bookgiftshop.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import dev.anhduc.bookgiftshop.dto.response.ResAuthorDTO;
import dev.anhduc.bookgiftshop.dto.response.ResCreateAuthorDTO;
import dev.anhduc.bookgiftshop.dto.response.ResUpdateAuthorDTO;
import dev.anhduc.bookgiftshop.dto.response.ResultPaginationDTO;
import dev.anhduc.bookgiftshop.entity.Author;
import dev.anhduc.bookgiftshop.entity.Product;
import dev.anhduc.bookgiftshop.exception.IdInvalidException;
import dev.anhduc.bookgiftshop.repository.AuthorRepository;
import dev.anhduc.bookgiftshop.repository.ProductRepository;

@Service
public class AuthorService {
    private final AuthorRepository authorRepository;
    private final ProductRepository productRepository;

    public AuthorService(AuthorRepository authorRepository, ProductRepository productRepository) {
        this.authorRepository = authorRepository;
        this.productRepository = productRepository;
    }

    public ResCreateAuthorDTO creatAuthor(Author author) {
        author = this.authorRepository.save(author);
        ResCreateAuthorDTO result = new ResCreateAuthorDTO();
        result.setAvatar(author.getAvatar());
        result.setBiography(author.getBiography());
        result.setBirth(author.getBirth());
        result.setCreatedAt(author.getCreatedAt());
        result.setCreatedBy(author.getCreatedBy());
        result.setId(author.getId());
        result.setName(author.getName());
        result.setNationality(author.getNationality());
        return result;
    }

    public Author fetchAuthorById(Long id) throws IdInvalidException {
        Author author = this.authorRepository.findById(id)
                .orElseThrow(() -> new IdInvalidException("Author với  không tồn tại!"));
        return author;
    }

    public ResultPaginationDTO fetchAllAuthors(Specification<Author> specification, Pageable pageable) {
        Page<Author> authorPage = this.authorRepository.findAll(specification, pageable);
        ResultPaginationDTO result = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();
        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());
        meta.setPages(authorPage.getTotalPages());
        meta.setTotal(authorPage.getTotalElements());
        result.setMeta(meta);
        List<ResAuthorDTO> listAuthors = authorPage.getContent().stream()
                .map(item -> this.convertAuthorDTO(item))
                .collect(Collectors.toList());
        result.setResult(listAuthors);
        return result;
    }

    public ResAuthorDTO convertAuthorDTO(Author author) {
        ResAuthorDTO result = new ResAuthorDTO();
        result.setAvatar(author.getAvatar());
        result.setBiography(author.getBiography());
        result.setBirth(author.getBirth());
        result.setCreatedAt(author.getCreatedAt());
        result.setCreatedBy(author.getCreatedBy());
        result.setId(author.getId());
        result.setName(author.getName());
        result.setNationality(author.getNationality());
        result.setDeleted(author.isDeleted());
        result.setUpdatedAt(author.getUpdatedAt());
        result.setUpdatedBy(author.getUpdatedBy());
        List<Product> products = this.productRepository.findByAuthors(author);
        if (products != null) {
            List<ResAuthorDTO.ProductDTO> res = new ArrayList<ResAuthorDTO.ProductDTO>();
            products.forEach(u -> {
                ResAuthorDTO.ProductDTO ans = new ResAuthorDTO.ProductDTO();
                ans.setId(u.getId());
                ans.setName(u.getName());
                res.add(ans);
            });
            result.setProducts(res);
        }
        return result;
    }

    public ResUpdateAuthorDTO updateAuthor(Long id, Author author) throws IdInvalidException {
        Author authorInDB = this.fetchAuthorById(id);
        authorInDB.setAvatar(author.getAvatar());
        authorInDB.setBiography(author.getBiography());
        authorInDB.setBirth(author.getBirth());
        authorInDB.setName(author.getName());
        authorInDB.setNationality(author.getNationality());
        authorInDB = this.authorRepository.save(authorInDB);
        ResUpdateAuthorDTO result = new ResUpdateAuthorDTO();
        result.setAvatar(authorInDB.getAvatar());
        result.setBiography(authorInDB.getBiography());
        result.setBirth(authorInDB.getBirth());
        result.setId(authorInDB.getId());
        result.setName(authorInDB.getName());
        result.setNationality(authorInDB.getNationality());
        result.setUpdatedAt(authorInDB.getUpdatedAt());
        result.setUpdatedBy(authorInDB.getUpdatedBy());
        return result;
    }

    public void deleteAuthorById(Long id) throws IdInvalidException {
        Author author = this.fetchAuthorById(id);
        author.setDeleted(true);
        this.authorRepository.save(author);
    }
}
