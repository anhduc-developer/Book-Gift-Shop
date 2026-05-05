package dev.anhduc.bookgiftshop.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.anhduc.bookgiftshop.dto.request.RequestRegiterDTO;
import dev.anhduc.bookgiftshop.dto.request.RequestUpdateProfileUserDTO;
import dev.anhduc.bookgiftshop.dto.response.ResRegisterUserDTO;
import dev.anhduc.bookgiftshop.dto.response.ResUpdateProfileUserDTO;
import dev.anhduc.bookgiftshop.exception.IdInvalidException;
import dev.anhduc.bookgiftshop.service.UserService;
import dev.anhduc.bookgiftshop.utils.annotation.ApiMessage;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/v1")
public class NormalUserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public NormalUserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/users")
    @ApiMessage("Register User")
    public ResponseEntity<ResRegisterUserDTO> registerUser(@Valid @RequestBody RequestRegiterDTO dto)
            throws IdInvalidException {
        String hashCode = this.passwordEncoder.encode(dto.getPassword());
        dto.setPassword(hashCode);
        return ResponseEntity.status(HttpStatus.CREATED).body(this.userService.registerUser(dto));
    }

    @PutMapping("/users")
    @ApiMessage("Update Profile User By Id")
    public ResponseEntity<ResUpdateProfileUserDTO> updateProfileUserById(
            @Valid @RequestBody RequestUpdateProfileUserDTO dto) throws IdInvalidException {
        return ResponseEntity.ok().body(this.userService.updateProfileUser(dto));
    }

}
