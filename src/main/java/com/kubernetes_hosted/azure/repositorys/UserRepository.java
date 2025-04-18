package com.kubernetes_hosted.azure.repositorys;


import com.kubernetes_hosted.azure.entitys.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface UserRepository extends JpaRepository<Users, Long> {
}