package com.kubernetes_hosted.azure.services;

import com.kubernetes_hosted.azure.entitys.Articles;
import com.kubernetes_hosted.azure.repositorys.ArticlesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticlesService {

    @Autowired
    private ArticlesRepository articlesRepository;

    public List<Articles> findAll() {
        return articlesRepository.findAll();
    }
}
