package dev.anhduc.bookgiftshop.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import dev.anhduc.bookgiftshop.dto.response.ResultPaginationDTO;
import dev.anhduc.bookgiftshop.entity.Role;
import dev.anhduc.bookgiftshop.entity.User;
import dev.anhduc.bookgiftshop.exception.IdInvalidException;
import dev.anhduc.bookgiftshop.repository.RoleRepository;
import dev.anhduc.bookgiftshop.repository.UserRepository;
import jakarta.transaction.Transactional;

@Service
public class RoleService {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    public RoleService(RoleRepository roleRepository, UserRepository userRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
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

    @Transactional
    public void deleteRoleById(Long id) throws IdInvalidException {
        Role role = this.roleRepository.findById(id)
                .orElseThrow(() -> new IdInvalidException("Role với id = " + id + " không tồn tại!"));
        if (role.isRoleDefault()) {
            throw new IdInvalidException("Định mệnh, xóa Role này lấy gì dùng?");
        }

        List<User> users = this.userRepository.findByRole(role);
        role.setDeleted(true);
        Role roleDefault = roleRepository.findByRoleDefaultTrue()
                .orElseThrow(() -> new RuntimeException("Không tìm thấy default role"));
        users.stream().forEach(user -> user.setRole(roleDefault));
        this.userRepository.saveAll(users);
        this.roleRepository.save(role);
    }

    public Role updateRoleById(Long id, Role requestRole) throws IdInvalidException {
        Role role = this.roleRepository.findById(id)
                .orElseThrow(() -> new IdInvalidException("Role với id = " + id + " không tồn tại!"));
        Role roleInDB = this.roleRepository.findByName(requestRole.getName());
        if (roleInDB != null) {
            throw new IdInvalidException("Role " + requestRole.getName() + " đã tồn tại!");
        }
        role.setName(requestRole.getName());
        role.setDescription(requestRole.getDescription());
        return this.roleRepository.save(role);
    }
}
