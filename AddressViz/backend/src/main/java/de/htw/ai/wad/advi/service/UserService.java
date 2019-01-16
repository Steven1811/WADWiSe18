package de.htw.ai.wad.advi.service;

import de.htw.ai.wad.advi.model.User;
import de.htw.ai.wad.advi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service("userService")
public class UserService {

    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    public User findUserByUserId(String userId) {
        return userRepository.findByUserId(userId);
    }
    
    public void saveUser(User user) {
        userRepository.save(user);
    }
}