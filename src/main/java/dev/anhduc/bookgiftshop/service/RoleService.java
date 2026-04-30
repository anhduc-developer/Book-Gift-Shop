package dev.anhduc.bookgiftshop.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import dev.anhduc.bookgiftshop.dto.response.ResultPaginationDTO;
import dev.anhduc.bookgiftshop.entity.Role;
import dev.anhduc.bookgiftshop.repository.RoleRepository;

@Service
public class RoleService {
    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role createRole(Role role) {
        return this.roleRepository.save(role);
    }

    public boolean isExistsByName(String name) {
        Role role = this.roleRepository.findByName(name);
        return role != null;
    }

    public Role fetchRoleById(Long id) {
        Optional<Role> roleOptional = this.roleRepository.findById(id);
        return roleOptional.isPresent() ? roleOptional.get() : null;
    }

    public ResultPaginationDTO fetchAllRoles(Specification<Role> specification, Pageable pageable) {
        Page<Role> pageRoles = this.roleRepository.findAll(specification, pageable);
        ResultPaginationDTO result = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();
        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());
        meta.setPages(pageRoles.getTotalPages());
        meta.setTotal(pageRoles.getTotalElements());
        result.setResult(pageRoles.getContent());
        return result;
    }

    public void deleteRoleById(Long id) {
        this.roleRepository.deleteById(id);
    }
}
