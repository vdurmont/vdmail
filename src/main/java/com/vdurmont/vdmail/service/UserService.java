package com.vdurmont.vdmail.service;

import com.vdurmont.vdmail.exception.AlreadyDoneException;
import com.vdurmont.vdmail.exception.IllegalInputException;
import com.vdurmont.vdmail.exception.NotFoundException;
import com.vdurmont.vdmail.model.User;
import com.vdurmont.vdmail.repository.EmailRepository;
import com.vdurmont.vdmail.repository.UserRepository;
import com.vdurmont.vdmail.tools.Emails;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static com.vdurmont.vdmail.tools.Strings.isNullOrEmpty;

@Service
public class UserService {
    @Inject private EmailRepository emailRepository;
    @Inject private PasswordEncoder passwordEncoder;
    @Inject private UserRepository userRepository;

    public User getOrCreate(String address) {
        address = Emails.clean(address);
        User user = this.userRepository.findByAddress(address);
        if (user == null) {
            user = new User();
            user.setAddress(address);
            user = this.userRepository.save(user);
        }
        return user;
    }

    public List<User> getContactsForUser(User user) {
        Sort sort = new Sort(Sort.Direction.DESC, "createdDate");
        List<User> contacts = this.emailRepository.findAllRecipientsBySender(user, sort);
        // FIXME HSQLDB doesnt support ordering if the property is not in the DISTINCT so we can't use @Query("SELECT DISTINCT(e.recipient) FROM Email e WHERE e.sender = ?1 ORDER BY e.createdDate DESC")
        // TODO improve the query when we will use MySQL
        removeDuplicates(contacts);
        return contacts;
    }

    private static void removeDuplicates(List<User> contacts) {
        Set<Integer> usersIds = new HashSet<>();
        Iterator<User> ite = contacts.iterator();
        while (ite.hasNext()) {
            User user = ite.next();
            if (usersIds.contains(user.getId())) {
                ite.remove();
            } else {
                usersIds.add(user.getId());
            }
        }
    }

    public User getById(int userId) {
        User user = this.userRepository.findOne(userId);
        if (user == null) {
            throw new NotFoundException("The User#" + userId + " was not found.");
        }
        return user;
    }

    public User create(String name, String address, String password) {
        if (isNullOrEmpty(name)) {
            throw new IllegalInputException("Invalid input: name");
        }
        if (isNullOrEmpty(password)) {
            throw new IllegalInputException("Invalid input: password");
        }
        address = Emails.clean(address);
        String encoded = this.passwordEncoder.encode(password);
        User user = this.userRepository.findByAddress(address);
        if (user != null && user.getName() != null) {
            throw new AlreadyDoneException("The email address '" + address + "' is already taken!");
        } else if (user == null) {
            user = new User();
            user.setAddress(address);
        }
        user.setName(name);
        user.setPassword(encoded);
        return this.userRepository.save(user);
    }

    public User getByAddress(String address) {
        address = Emails.clean(address);
        User user = this.userRepository.findByAddress(address);
        if (user == null) {
            throw new NotFoundException("The User with address '" + address + "' was not found.");
        }
        return user;
    }
}
