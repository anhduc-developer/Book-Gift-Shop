package dev.anhduc.bookgiftshop.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.anhduc.bookgiftshop.dto.response.ResCreateUserDTO;
import dev.anhduc.bookgiftshop.dto.response.ResUserDTO;
import dev.anhduc.bookgiftshop.dto.response.ResultPaginationDTO;
import dev.anhduc.bookgiftshop.entity.User;
import dev.anhduc.bookgiftshop.exception.IdInvalidException;
import dev.anhduc.bookgiftshop.service.UserService;
import dev.anhduc.bookgiftshop.utils.annotation.ApiMessage;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import com.turkraft.springfilter.boot.Filter;

@RestController
@RequestMapping("/api/v1/admin")
public class UserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/users")
    @ApiMessage("Create New User")
    public ResponseEntity<ResCreateUserDTO> createUser(@Valid @RequestBody User user) throws IdInvalidException {
        if (this.userService.isExistsByEmail(user.getEmail())) {
            throw new IdInvalidException("User đã tồn tại!");
        }
        String hashPassword = this.passwordEncoder.encode(user.getPassword());
        user.setPassword(hashPassword);
        User createUser = this.userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(this.userService.convertResCreateUserDTO(createUser));
    }

    @GetMapping("/users/{id}")
    @ApiMessage("Fetch User By Id")
    public ResponseEntity<ResUserDTO> fetchUserById(@PathVariable("id") Long id) throws IdInvalidException {
        User fetchUser = this.userService.fetchUserById(id);
        if (fetchUser == null) {
            throw new IdInvalidException("User với id = " + id + " không tồn tại!");
        }
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.convertToResUserDTO(fetchUser));
    }

    @DeleteMapping("/users/{id}")
    @ApiMessage("Delete User By Id")
    public ResponseEntity<?> deleteUserById(@PathVariable("id") Long id) throws IdInvalidException {
        User user = this.userService.fetchUserById(id);
        if (user == null) {
            throw new IdInvalidException("User với id = " + id + " không tồn tại!");
        }
        this.userService.deleteUserById(user);
        return ResponseEntity.ok().body(null);
    }

    @PutMapping("/users/{id}")
    @ApiMessage("Update User By Id")
    public ResponseEntity<?> updateUserById(@PathVariable("id") Long id, @Valid @RequestBody User requestUser) {
        if (requestUser.getPassword() != null && !requestUser.getPassword().isEmpty()) {
            requestUser.setPassword(passwordEncoder.encode(requestUser.getPassword()));
        }
        User user = this.userService.updateUser(id, requestUser);
        return ResponseEntity.ok().body(this.userService.convertResUpdateUserDTO(user));
    }

    @GetMapping("/users")
    @ApiMessage("Fetch All Users")
    public ResponseEntity<ResultPaginationDTO> fetchAllUsers(@Filter Specification<User> specification,
            Pageable pageable) {
        return ResponseEntity.ok().body(this.userService.fetchAllUsers(specification, pageable));
    }
}
