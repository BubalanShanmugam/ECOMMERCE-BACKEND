package com.example.AmazonClone.Repository;

import com.example.AmazonClone.Model.Userss;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<Userss, Integer> {
    Userss findByUsername(String username);
}
