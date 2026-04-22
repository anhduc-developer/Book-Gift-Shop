package dev.anhduc.bookgiftshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.anhduc.bookgiftshop.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}
