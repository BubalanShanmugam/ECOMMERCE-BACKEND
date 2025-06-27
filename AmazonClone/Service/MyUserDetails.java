package com.example.AmazonClone.Service;

import com.example.AmazonClone.Model.UserPrincipal;
import com.example.AmazonClone.Model.Userss;
import com.example.AmazonClone.Repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetails implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Userss userss = userRepo.findByUsername(username);
        if(userss == null){
            System.out.println("User not found");
            throw new UsernameNotFoundException("User not found !");
        }
        else{
            return new UserPrincipal(userss);
        }
    }
}
