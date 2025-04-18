package com.kubernetes_hosted.azure.services;

import com.kubernetes_hosted.azure.entitys.Users;
import com.kubernetes_hosted.azure.repositorys.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    public List<Users> getAll() {
        return userRepository.findAll();
    }
    public Optional<Users> getUserById(Long id) {
        return userRepository.findById(id);
    }
    public Users create(Users user) {
        return userRepository.save(user);
    }
    public Users update(Long id, Users userDetails) {
        Users user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("not found: " + id));
        user.setName(userDetails.getName());
        user.setEmail(userDetails.getEmail());
        return userRepository.save(user);
    }
    public void delete(Long id) {
        userRepository.deleteById(id);
    }
}