package com.kubernetes_hosted.azure.services;

import com.kubernetes_hosted.azure.entitys.Users;
import com.kubernetes_hosted.azure.repositorys.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;



@Service
public class AuthenticationService {
    public boolean authenticate(String username, String password) {
        // authentication logic
       // todo auth
        return false;
    }
}