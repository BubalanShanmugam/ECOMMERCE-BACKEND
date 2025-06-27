package com.example.AmazonClone.Security;

import com.example.AmazonClone.Service.JWTService;
import com.example.AmazonClone.Service.MyUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
//
//@Configuration
//public class JwtFilter extends OncePerRequestFilter {//we are using this filter once for the process so we are using the oneperrequestfilter.
//
//    @Autowired
//    private JWTService jwtService;
//
//    @Autowired
//    ApplicationContext context;
//    /*
//    *in the request,
//    * in authentication we give the username nd pass and the tokens fo the same use.
//    * in the authentication we give the token in the bearer option so we ae getting the token by taking the substring of the request.
//     */
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        //Bearer jdnjndvunojfnvuwfnvuwrtuuqepojdksms cknvqmdokenvindojfonaskdc quobvonckfvovl ckc uwinvkd
//        //0123456//from 7 th infer we are having the token(so in the below we put the seven.
//        String authheader = request.getHeader("Authorization");
//        String token = null;
//        String username = null;
//        if(authheader!=null && authheader.startsWith("Bearer ")){
//        token=authheader.substring(7);//"bearer " -> 7 characters.
//        username= jwtService.extractUseName(token);
//        }
//        //to check if the user is exist.
//        // to check if the usr was already authenticted .
//        if(username!= null && SecurityContextHolder.getContext().getAuthentication()==null){
//            UserDetails userDetails = context.getBean(MyUserDetails.class).loadUserByUsername(username);
//
//            //if the UserTokenFilter is verifeied then we hve to continue with the next filter.
//            if(jwtService.validateToken(token, userDetails)){
//                UsernamePasswordAuthenticationToken authtoken= new UsernamePasswordAuthenticationToken(userDetails, null,userDetails.getAuthorities());//it parameters are: 1.principle, 2.credentials,3.authorities.
//                authtoken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                SecurityContextHolder.getContext().setAuthentication(authtoken);
//            }
//        }
//        filterChain.doFilter(request,response);
//    }
//}









@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JWTService jwtService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            try {
                username = jwtService.extractUsername(token);
            } catch (Exception e) {
                System.out.println("Invalid JWT: " + e.getMessage());
            }
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (jwtService.validateToken(token, userDetails)) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}
