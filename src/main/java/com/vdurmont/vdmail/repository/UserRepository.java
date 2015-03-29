package com.vdurmont.vdmail.repository;

import com.vdurmont.vdmail.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByAddress(String address);
}
