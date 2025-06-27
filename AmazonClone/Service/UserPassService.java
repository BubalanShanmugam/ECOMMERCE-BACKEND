package com.example.AmazonClone.Service;

import com.example.AmazonClone.Model.Userss;
import com.example.AmazonClone.Repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class UserPassService {

    @Autowired
    private UserRepo repo;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    //this is for adding the student with the not encrypted passwords.
//    public void addstudent(Userss u){
//        repo.save(u);
//    }


    //this is the builtin bcrypt method ni java.
    //here the 12 is the rounds.means that the password was encrypted by 2^rounds(12) times.
    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(12);

    //this is for the encrypted pass.
    public Userss register(Userss user){
    //   it was simply work for the not encrypted values.but for the encrypted values....
    //   return repo.save(user);
         user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
         return repo.save(user);
    }



    public String verify(Userss user) {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
            if(authentication.isAuthenticated()){
                return jwtService.generateToken(user.getUsername());
            }
            else{
                return "Not Authentication /Failed";
            }
    }
}
