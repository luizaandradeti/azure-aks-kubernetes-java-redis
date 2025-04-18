package com.kubernetes_hosted.azure.controllers;


import com.kubernetes_hosted.azure.entitys.Users;
import com.kubernetes_hosted.azure.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class AuthController {
    // TODO AUTH
}