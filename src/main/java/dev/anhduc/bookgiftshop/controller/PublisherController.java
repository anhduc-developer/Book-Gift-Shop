package dev.anhduc.bookgiftshop.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import dev.anhduc.bookgiftshop.dto.response.ResCreatePublisherDTO;
import dev.anhduc.bookgiftshop.dto.response.ResPublisherDTO;
import dev.anhduc.bookgiftshop.dto.response.ResUpdatePublisherDTO;
import dev.anhduc.bookgiftshop.dto.response.ResultPaginationDTO;
import dev.anhduc.bookgiftshop.entity.Publisher;
import dev.anhduc.bookgiftshop.exception.IdInvalidException;
import dev.anhduc.bookgiftshop.service.PublisherService;
import dev.anhduc.bookgiftshop.utils.annotation.ApiMessage;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/v1")
public class PublisherController {
    private final PublisherService publisherService;

    public PublisherController(PublisherService publisherService) {
        this.publisherService = publisherService;
    }

    @PostMapping("/publishers")
    @ApiMessage("Create New Publisher")
    public ResponseEntity<ResCreatePublisherDTO> createPublishser(@Valid @RequestBody Publisher publisher)
            throws IdInvalidException {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.publisherService.createPublisher(publisher));
    }

    @GetMapping("/publishers/{id}")
    @ApiMessage("Fetch Publisher By Id")
    public ResponseEntity<ResPublisherDTO> fetchPublisherById(@PathVariable("id") Long id) throws IdInvalidException {
        Publisher publisher = this.publisherService.fetchPublisherById(id);
        return ResponseEntity.ok().body(this.publisherService.convertPublisherDTO(publisher));
    }

    @GetMapping("/publishers")
    @ApiMessage("Fetch All Publishers")
    public ResponseEntity<ResultPaginationDTO> fetchAllPublishers(@Filter Specification<Publisher> specification,
            Pageable pageable) throws IdInvalidException {
        return ResponseEntity.ok().body(this.publisherService.fetchAllPublishers(specification, pageable));
    }

    @PutMapping("/publishers/{id}")
    @ApiMessage("Update Publisher By Id")
    public ResponseEntity<ResUpdatePublisherDTO> updatePublisherById(@PathVariable("id") Long id,
            @Valid @RequestBody Publisher publisher) throws IdInvalidException {
        return ResponseEntity.ok().body(this.publisherService.updatePublisher(id, publisher));
    }

    @DeleteMapping("/publishers/{id}")
    @ApiMessage("Delete Publisher By Id")
    public ResponseEntity<Void> deletePublisherById(@PathVariable("id") Long id) throws IdInvalidException {
        this.publisherService.deletePublisherById(id);
        return ResponseEntity.ok().body(null);
    }
}
