package com.tinqinacademy.authenticationservice.core.security;

import com.tinqinacademy.authenticationservice.api.exceptions.custom.InvalidJwtException;
import com.tinqinacademy.authenticationservice.persistence.model.entity.User;
import com.tinqinacademy.authenticationservice.persistence.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.*;
import java.util.function.Function;



@Service
@RequiredArgsConstructor
public class JwtService {

    @Value("${spring.security.jwt.secret}")
    private String SECRET_KEY;
    private final UserRepository userRepository;


    public boolean validateJwt (String jwt){
        //TODO: add check whether jwt is in the blacklist.
        String id;
        String role;
        try {
            id = extractId(jwt);
            role = extractRole(jwt);
        }
        catch (InvalidJwtException ex){
            return false;
        }

        Optional<User> user = userRepository.findById(UUID.fromString(id));

        if(user.isEmpty() || !user.get().getRole().toString().equals(role)) {return false;}

        return true;
    }

    public Optional<User> validateJwtAndReturnUser (String jwt){
        //TODO: add check whether jwt is in the blacklist.
        String id;
        String role;
        try {
            id = extractId(jwt);
            role = extractRole(jwt);
        }
        catch (InvalidJwtException ex){
            return Optional.empty();
        }

        Optional<User> user = userRepository.findById(UUID.fromString(id));

        if(user.isEmpty() || !user.get().getRole().toString().equals(role)) {return Optional.empty();}

        return user;
    }

    public String extractId(String token) {
        return extractClaim(token,Claims::getSubject);
    }

    public String extractRole(String token) {
        return extractClaim(token, claims -> claims.get("role", String.class));
    }


    private  <T> T extractClaim(String token, Function<Claims,T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token){
        try {
            return Jwts
                    .parserBuilder()
                    .setSigningKey(getSignInKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        }
        catch (Exception e){
            throw new InvalidJwtException("Invalid JWT.");
        }
    }

    public String generateToken(User user){
        return generateToken(new HashMap<>(), user);
    }

    private String generateToken(Map<String,Object> extraClaims, User user){
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(user.getId().toString())
                .claim("role",user.getRole().toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+(5*60*1000)))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getSignInKey() {
        byte [] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}