package com.kubernetes_hosted.azure.entitys;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;


@Entity
public class AuthenticUser {
    @Id
    private Long id;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    //TODO AUTH
}