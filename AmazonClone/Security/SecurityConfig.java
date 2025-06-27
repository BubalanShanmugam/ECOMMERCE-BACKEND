package com.example.AmazonClone.Security;

import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    ////        http.authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
    ////            .oauth2Login(Customizer.withDefaults());
    ////        return http.build();
//
//        return http.csrf(customizer -> customizer.disable())
//                    .authorizeHttpRequests(request -> request.anyRequest().authenticated())
//                    .httpBasic(Customizer.withDefaults())
//                    .sessionManagement(session ->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .build();
//    }

    @Autowired
    private  UserDetailsService userDetailsService;

    //for the user authentication via token
    @Autowired
    private JwtFilter jwtFilter;

//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//
//    return http
//            .csrf(csrf -> csrf
//                    .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
//            )
//            .authorizeHttpRequests(auth -> auth
//                    .requestMatchers("/api/csrf-token","/api/login","/api/register").permitAll()  // allow CSRF token endpoint
//                    .anyRequest().authenticated()
//            )
//            .httpBasic(Customizer.withDefaults())
//            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
//            .build();
//    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        HttpSessionCsrfTokenRepository csrfTokenRepository = new HttpSessionCsrfTokenRepository();
        csrfTokenRepository.setHeaderName("X-CSRF-TOKEN"); // ✅ Change default header name

        return http
                .csrf(AbstractHttpConfigurer::disable // Use session-based CSRF token
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/csrf-token", "/api/login", "/api/register").permitAll()
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }



    //for code level user info storing

//    @Bean
//    public UserDetailsService userDetailsService(){
//        UserDetails user1= User
//                .withDefaultPasswordEncoder()
//                .username("bubal")
//                .password("bubal")
//                .roles("user")
//                .build();
//
//        UserDetails user2= User
//                .withDefaultPasswordEncoder()
//                .username("santhiya")
//                .password("santhiya")
//                .roles("user")
//                .build();
//        return new InMemoryUserDetailsManager(user2,user1);
//    }


    //I have to use the Authentication interface.But it is the interfce so I can't.Hence I going to use the Class which is  implementing the Authenticationprovider.
    //DaoAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider , AbstractUserDetailsAuthenticationProvider implements AuthenticationProvider.
    @Bean
    public AuthenticationProvider AuthenticationProvider(){
        //by the above comment lins.
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
//        provider.setPasswordEncoder(NoOpPasswordEncoder.getInstance());//default password encoder.
//        if you want to enter the original pass and it need to convert as a encrypted one for the validation process...........this is the code.
        provider.setPasswordEncoder(new BCryptPasswordEncoder(12));
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }

    //we want the authenticationManager but it is the interface so we are returning the class which is implementing the AuthenticationProvider.
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration)throws Exception{
        return configuration.getAuthenticationManager();
    }
}