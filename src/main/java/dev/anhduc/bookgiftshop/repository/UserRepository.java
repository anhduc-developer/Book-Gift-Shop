package dev.anhduc.bookgiftshop.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import dev.anhduc.bookgiftshop.entity.Role;
import dev.anhduc.bookgiftshop.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    Optional<User> findByEmail(String email);

    User findByRefreshTokenAndEmail(String token, String email);

    List<User> findByRole(Role role);
}
