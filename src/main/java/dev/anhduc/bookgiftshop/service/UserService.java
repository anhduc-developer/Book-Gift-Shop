package dev.anhduc.bookgiftshop.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import dev.anhduc.bookgiftshop.domain.Role;
import dev.anhduc.bookgiftshop.domain.User;
import dev.anhduc.bookgiftshop.domain.response.ResCreateUserDTO;
import dev.anhduc.bookgiftshop.domain.response.ResUserDTO;
import dev.anhduc.bookgiftshop.repository.RoleRepository;
import dev.anhduc.bookgiftshop.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public User createUser(User user) {
        if (user.getRole() == null) {
            user.setRole(this.roleRepository.findByName("USER"));
        }
        return this.userRepository.save(user);
    }

    public ResCreateUserDTO convertResCreateUserDTO(User user) {
        ResCreateUserDTO res = new ResCreateUserDTO();
        res.setId(user.getId());
        res.setAddress(user.getAddress());
        res.setAge(user.getAge());
        res.setCreatedAt(user.getCreatedAt());
        res.setGender(user.getGender());
        res.setFullname(user.getFullName());
        res.setPhoneNumber(user.getPhoneNumber());
        return res;
    }

    public User fetchUserById(Long id) {
        Optional<User> optionalUser = this.userRepository.findById(id);
        return optionalUser.isPresent() ? optionalUser.get() : null;
    }

    public ResUserDTO convertToResUserDTO(User user) {
        ResUserDTO res = new ResUserDTO();

        res.setId(user.getId());
        res.setEmail(user.getEmail());
        res.setFullName(user.getFullName());
        res.setAge(user.getAge());
        res.setUpdatedAt(user.getUpdatedAt());
        res.setCreatedAt(user.getCreatedAt());
        res.setGender(user.getGender());
        res.setAddress(user.getAddress());
        res.setPhoneNumber(user.getPhoneNumber());

        if (user.getRole() != null) {
            ResUserDTO.RoleUser roleUser = new ResUserDTO.RoleUser();
            roleUser.setId(user.getRole().getId());
            roleUser.setName(user.getRole().getName());
            res.setRole(roleUser);
        }

        return res;
    }
}
