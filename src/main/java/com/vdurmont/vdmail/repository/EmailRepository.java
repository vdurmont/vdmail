package com.vdurmont.vdmail.repository;

import com.vdurmont.vdmail.model.Email;
import com.vdurmont.vdmail.model.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmailRepository extends JpaRepository<Email, Integer> {
    @Query("SELECT e.recipient FROM Email e WHERE e.sender = ?1")
    List<User> findAllRecipientsBySender(User user, Sort sort);

    List<Email> findAllBySender(User user, Sort sort);
}
