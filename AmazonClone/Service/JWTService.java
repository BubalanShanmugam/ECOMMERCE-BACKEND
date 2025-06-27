package com.example.AmazonClone.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
//
//
//@Service
//public class JWTService {
//    private String key = "";
//
//    public JWTService(){
//        try {
//            KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA256");
//            SecretKey secretKey = keyGenerator.generateKey();
//            key = Base64.getEncoder().encodeToString(secretKey.getEncoded());
//        }
//        catch (NoSuchAlgorithmException e)
//        {
//            throw new RuntimeException(e);
//        }
//    }
//
//    public String generateToken(String username) {
//        Map<String,Object> claims = new HashMap<>();
//        return Jwts.builder()
//                .claims()
//                .add(claims)
//                .subject(username)
//                .issuedAt(new Date(System.currentTimeMillis()))
//                .expiration(new Date(System.currentTimeMillis() +60*60*100))
//                .and()
//                .signWith(getKey())
//                .compact();
//    }
//
//    private SecretKey getKey() {
//        byte[] bytes= Decoders.BASE64.decode(key);
//        return Keys.hmacShaKeyFor(bytes);
//    }
//
//    public String extractUseName(String token) {
//        return extractClaim(token, Claims::getSubject);
//    }
//    private <T> T extractClaim(String token, Function<Claims,T> claimResolver){
//        final Claims claims = extracAllClaims(token);
//        return claimResolver.apply(claims);
//    }
//    private Claims extracAllClaims(String token) {
//        return Jwts.parser()
//                .verifyWith(getKey())
//                .build().parseSignedClaims(token).getPayload();
//    }
//
//
//
//    public boolean validateToken(String token , UserDetails userDetails){
//            final String username = extractUseName(token);
//            return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
//    }
//    private  boolean isTokenExpired(String token){
//        return extractExpiration(token).before(new Date());
//    }
//    private Date extractExpiration(String token){
//        return extractClaim(token, Claims::getExpiration);
//    }
//}












@Service
public class JWTService {

    // ✅ Use a constant key for token validation
    private static final String SECRET_KEY = "bubalSanthiyaSuperSecretKey123456789012345"; // min 32 chars

    public String generateToken(String username) {
        Map<String,Object> claims = new HashMap<>();
        return Jwts.builder()
                .claims()
                .add(claims)
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 hour
                .and()
                .signWith(getKey())
                .compact();
    }

    private SecretKey getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(Base64.getEncoder().encodeToString(SECRET_KEY.getBytes()));
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }



    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}
