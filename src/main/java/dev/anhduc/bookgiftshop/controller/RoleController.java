package dev.anhduc.bookgiftshop.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import dev.anhduc.bookgiftshop.dto.response.ResultPaginationDTO;
import dev.anhduc.bookgiftshop.entity.Role;
import dev.anhduc.bookgiftshop.exception.IdInvalidException;
import dev.anhduc.bookgiftshop.service.RoleService;
import dev.anhduc.bookgiftshop.utils.annotation.ApiMessage;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/v1/admin")
public class RoleController {
    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping("/roles")
    @ApiMessage("Create New Role")
    public ResponseEntity<Role> createRole(@Valid @RequestBody Role role) throws IdInvalidException {
        if (this.roleService.isExistsByName(role.getName())) {
            throw new IdInvalidException("Role với tên = " + role.getName() + " đã tồn tại trong hệ thống!");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(this.roleService.createRole(role));
    }

    @GetMapping("/roles/{id}")
    @ApiMessage("Fetch Role By Id")
    public ResponseEntity<Role> fetchRoleById(@PathVariable("id") Long id) throws IdInvalidException {
        Role role = this.roleService.fetchRoleById(id);
        if (role == null) {
            throw new IdInvalidException("Role với id = " + id + " không tồn tại!");
        }
        return ResponseEntity.ok().body(role);
    }

    @GetMapping("/roles")
    @ApiMessage("Fetch All Roles")
    public ResponseEntity<ResultPaginationDTO> fetchAllRoles(@Filter Specification<Role> specification,
            Pageable pageable) {
        return ResponseEntity.ok().body(this.roleService.fetchAllRoles(specification, pageable));
    }

    @DeleteMapping("/roles/{id}")
    @ApiMessage("Delete A Role")
    public ResponseEntity<Void> deleteRoleById(@PathVariable("id") Long id) throws IdInvalidException {
        this.roleService.deleteRoleById(id);
        return ResponseEntity.ok().body(null);
    }

    @PutMapping("/roles/{id}")
    @ApiMessage("Upder A Role")
    public ResponseEntity<Role> updateRoleById(@PathVariable("id") Long id,
            @Valid @RequestBody Role requestRole) throws IdInvalidException {
        return ResponseEntity.ok().body(this.roleService.updateRoleById(id,
                requestRole));
    }
}
