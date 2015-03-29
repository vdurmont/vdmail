package com.vdurmont.vdmail.repository;

import com.vdurmont.vdmail.model.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionRepository extends JpaRepository<Session, Integer> {
    Session findByToken(String token);
}
