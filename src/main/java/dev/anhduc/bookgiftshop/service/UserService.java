package dev.anhduc.bookgiftshop.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import dev.anhduc.bookgiftshop.domain.dto.response.ResCreateUserDTO;
import dev.anhduc.bookgiftshop.domain.dto.response.ResUpdateUserDTO;
import dev.anhduc.bookgiftshop.domain.dto.response.ResUserDTO;
import dev.anhduc.bookgiftshop.domain.dto.response.ResultPaginationDTO;
import dev.anhduc.bookgiftshop.domain.entity.Order;
import dev.anhduc.bookgiftshop.domain.entity.User;
import dev.anhduc.bookgiftshop.repository.OrderRepository;
import dev.anhduc.bookgiftshop.repository.RoleRepository;
import dev.anhduc.bookgiftshop.repository.UserRepository;
import dev.anhduc.bookgiftshop.util.constant.OrderStatusEnum;
import jakarta.transaction.Transactional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final OrderRepository orderRepository;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, OrderRepository orderRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.orderRepository = orderRepository;
    }

    public boolean isExistsByEmail(String email) {
        Optional<User> user = this.userRepository.findByEmail(email);
        return user.isPresent() ? true : false;
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
        res.setDeleted(user.isDeleted());
        if (user.getRole() != null) {
            ResUserDTO.RoleUser roleUser = new ResUserDTO.RoleUser();
            roleUser.setId(user.getRole().getId());
            roleUser.setName(user.getRole().getName());
            res.setRole(roleUser);
        }

        return res;
    }

    @Transactional
    public void deleteUserById(User user) {
        List<Order> orders = this.orderRepository.findByUser(user);
        orders.stream()
                .filter(order -> order.getStatus() == OrderStatusEnum.PENDING
                        || order.getStatus() == OrderStatusEnum.CONFIRMED
                        || order.getStatus() == OrderStatusEnum.PROCESSING)
                .forEach(order -> order.setStatus(OrderStatusEnum.CANCELLED));
        this.orderRepository.saveAll(orders);
        user.setDeleted(true);
        this.userRepository.save(user);
    }

    public User updateUser(Long id, User user) {
        User currentUser = this.fetchUserById(id);
        currentUser.setAddress(user.getAddress());
        currentUser.setAge(user.getAge());
        currentUser.setFullName(user.getFullName());
        currentUser.setPhoneNumber(user.getPhoneNumber());
        currentUser.setPassword(user.getPassword());
        currentUser.setGender(user.getGender());
        return this.userRepository.save(currentUser);

    }

    public ResUpdateUserDTO convertResUpdateUserDTO(User user) {
        ResUpdateUserDTO res = new ResUpdateUserDTO();
        res.setAddress(user.getAddress());
        res.setAge(user.getAge());
        res.setEmail(user.getEmail());
        res.setFullname(user.getFullName());
        res.setGender(user.getGender());
        res.setId(user.getId());
        res.setUpdatedAt(user.getUpdatedAt());
        res.setPhoneNumber(user.getPhoneNumber());
        return res;

    }

    public ResultPaginationDTO fetchAllUsers(Specification<User> specification, Pageable pageable) {
        Page<User> pageUsers = this.userRepository.findAll(specification, pageable);
        ResultPaginationDTO result = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();
        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());
        meta.setPages(pageUsers.getTotalPages());
        meta.setTotal(pageUsers.getTotalElements());
        result.setMeta(meta);
        List<ResUserDTO> listUsers = pageUsers.getContent()
                .stream().map(item -> this.convertToResUserDTO(item))
                .collect(Collectors.toList());
        result.setResult(listUsers);
        return result;
    }
}
