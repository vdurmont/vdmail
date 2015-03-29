package com.vdurmont.vdmail.service;

import com.vdurmont.vdmail.model.User;
import com.vdurmont.vdmail.repository.UserRepository;
import com.vdurmont.vdmail.tools.Emails;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

@Service
public class UserService {
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
}
