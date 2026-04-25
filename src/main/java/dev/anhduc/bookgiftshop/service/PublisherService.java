package dev.anhduc.bookgiftshop.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import dev.anhduc.bookgiftshop.domain.dto.response.ResCreatePublisherDTO;
import dev.anhduc.bookgiftshop.domain.dto.response.ResPublisherDTO;
import dev.anhduc.bookgiftshop.domain.dto.response.ResUpdatePublisherDTO;
import dev.anhduc.bookgiftshop.domain.dto.response.ResultPaginationDTO;
import dev.anhduc.bookgiftshop.domain.entity.Publisher;
import dev.anhduc.bookgiftshop.repository.PublisherRepository;
import dev.anhduc.bookgiftshop.util.errors.IdInvalidException;

@Service
public class PublisherService {
    private final PublisherRepository publishserRepository;

    public PublisherService(PublisherRepository publishserRepository) {
        this.publishserRepository = publishserRepository;
    }

    public ResCreatePublisherDTO createPublisher(Publisher publisher) throws IdInvalidException {
        Publisher publisherInDB = this.fetchPublisherByEmail(publisher.getEmail());
        if (publisherInDB != null) {
            if (!publisherInDB.isDeleted()) {
                throw new IdInvalidException(
                        "Nhà xuất bản với email = " + publisher.getEmail() + " đã tổn tại trong hệ thống!");
            } else {
                throw new IdInvalidException(
                        "Email này thuộc về một nhà xuất bản đã bị xóa. Vui lòng khôi phục thay vì tạo mới.");
            }
        }
        publisher = this.publishserRepository.save(publisher);
        ResCreatePublisherDTO res = new ResCreatePublisherDTO();
        res.setAddress(publisher.getAddress());
        res.setCreatedAt(publisher.getCreatedAt());
        res.setCreatedBy(publisher.getCreatedBy());
        res.setEmail(publisher.getEmail());
        res.setId(publisher.getId());
        res.setName(publisher.getName());
        res.setPhone(publisher.getPhone());
        return res;
    }

    public Publisher fetchPublisherById(Long id) throws IdInvalidException {
        Publisher publisher = this.publishserRepository.findById(id)
                .orElseThrow(() -> new IdInvalidException("Nhà xuất bản với id = " + id + " không tồn tại!"));
        return publisher;
    }

    public Publisher fetchPublisherByEmail(String email) throws IdInvalidException {
        Optional<Publisher> publisherOptional = this.publishserRepository.findByEmail(email);
        return publisherOptional.isPresent() ? publisherOptional.get() : null;
    }

    public ResPublisherDTO convertPublisherDTO(Publisher publisher) {
        ResPublisherDTO result = new ResPublisherDTO();
        result.setAddress(publisher.getAddress());
        result.setCreatedAt(publisher.getCreatedAt());
        result.setCreatedBy(publisher.getCreatedBy());
        result.setEmail(publisher.getEmail());
        result.setId(publisher.getId());
        result.setName(publisher.getName());
        result.setPhone(publisher.getPhone());
        result.setUpdatedAt(publisher.getUpdatedAt());
        result.setUpdatedBy(publisher.getUpdatedBy());
        return result;
    }

    public ResultPaginationDTO fetchAllPublishers(Specification<Publisher> specification, Pageable pageable) {
        Page<Publisher> publisherPage = this.publishserRepository.findAll(specification, pageable);
        ResultPaginationDTO result = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();
        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());
        meta.setPages(publisherPage.getTotalPages());
        meta.setTotal(publisherPage.getTotalElements());
        result.setMeta(meta);
        List<ResPublisherDTO> listPublishers = publisherPage.getContent().stream()
                .map(item -> this.convertPublisherDTO(item))
                .collect(Collectors.toList());
        result.setResult(listPublishers);
        return result;
    }

    public ResUpdatePublisherDTO updatePublisher(Long id, Publisher publisher) throws IdInvalidException {
        Publisher publisherDB = this.publishserRepository.findById(id)
                .orElseThrow(() -> new IdInvalidException("Nhà xuất bản với id = " + id + " không tồn tại!"));
        if (!publisher.getEmail().equals(publisherDB.getEmail())) {
            if (this.fetchPublisherByEmail(publisher.getEmail()) != null) {
                throw new IdInvalidException("Email đã tồn tại, vui lòng chọn email khác!");
            }

        }
        publisherDB.setAddress(publisher.getAddress());
        publisherDB.setEmail(publisher.getEmail());
        publisherDB.setName(publisher.getName());
        publisherDB.setPhone(publisher.getPhone());
        publisherDB = this.publishserRepository.save(publisherDB);
        ResUpdatePublisherDTO res = new ResUpdatePublisherDTO();
        res.setAddress(publisherDB.getAddress());
        res.setUpdatedAt(publisherDB.getUpdatedAt());
        res.setUpdatedBy(publisherDB.getUpdatedBy());
        res.setEmail(publisherDB.getEmail());
        res.setId(publisherDB.getId());
        res.setName(publisherDB.getName());
        res.setPhone(publisherDB.getPhone());
        return res;
    }

    public void deletePublisherById(Long id) throws IdInvalidException {
        Publisher publisher = this.fetchPublisherById(id);
        publisher.setDeleted(true);
        this.publishserRepository.save(publisher);
    }
}
