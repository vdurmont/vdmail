package com.vdurmont.vdmail.repository;

import com.vdurmont.vdmail.model.Session;
import com.vdurmont.vdmail.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SessionRepository extends JpaRepository<Session, Integer> {
    Session findByToken(String token);

    List<Session> findAllByUser(User user);
}
