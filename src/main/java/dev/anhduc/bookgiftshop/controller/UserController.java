package dev.anhduc.bookgiftshop.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.anhduc.bookgiftshop.domain.User;
import dev.anhduc.bookgiftshop.domain.response.ResCreateUserDTO;
import dev.anhduc.bookgiftshop.domain.response.ResUserDTO;
import dev.anhduc.bookgiftshop.service.UserService;
import dev.anhduc.bookgiftshop.util.annotation.ApiMessage;
import dev.anhduc.bookgiftshop.util.errors.IdInvalidException;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/v1")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users")
    @ApiMessage("Create New User")
    public ResponseEntity<ResCreateUserDTO> createUser(@RequestBody User user) {
        User createUser = this.userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(this.userService.convertResCreateUserDTO(createUser));
    }

    @GetMapping("/users/{id}")
    @ApiMessage("Fetch User By Id")
    public ResponseEntity<ResUserDTO> fetchUserById(@PathVariable("id") Long id) throws IdInvalidException {
        User fetchUser = this.userService.fetchUserById(id);
        if (fetchUser == null) {
            throw new IdInvalidException("User với id = " + id + " không tồn tại !");
        }
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.convertToResUserDTO(fetchUser));
    }
}
