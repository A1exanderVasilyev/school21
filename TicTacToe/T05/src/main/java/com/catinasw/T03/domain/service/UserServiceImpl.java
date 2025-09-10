package com.catinasw.T03.domain.service;

import com.catinasw.T03.datasource.UserRepository;
import com.catinasw.T03.domain.model.User;
import com.catinasw.T03.domain.util.RegistrationResponse;
import com.catinasw.T03.domain.util.Role;
import com.catinasw.T03.security.JwtProvider;
import com.catinasw.T03.web.model.JwtAuthentication;
import com.catinasw.T03.web.model.SignUpRequest;
import com.catinasw.T03.web.model.dto.JwtRequest;
import com.catinasw.T03.web.model.dto.JwtResponse;
import io.jsonwebtoken.Claims;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import java.util.Collections;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    @Autowired
    public UserServiceImpl(UserRepository repository, PasswordEncoder passwordEncoder, JwtProvider jwtProvider) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
    }

    @Transactional
    @Override
    public RegistrationResponse register(SignUpRequest request) {
        String login = request.getLogin();
        RegistrationResponse response;
        if (repository.existsUserByLogin(login)) {
            response = RegistrationResponse.failed("User with that login already exists");
            return response;
        }
        String password = request.getPassword();
        try {
            User newUser = repository.save(new User(login, passwordEncoder.encode(password), Collections.singletonList(Role.USER.getAuthority())));
            response = RegistrationResponse.success("Registration complete", newUser.getUuid());
        } catch (Exception e) {
            response = RegistrationResponse.failed(e.getMessage());
        }
        return response;
    }

    @Override
    public JwtResponse authenticate(JwtRequest jwtRequest) throws AuthenticationException {

        String login = jwtRequest.getLogin();
        User user = repository.getUserByLogin(login).orElseThrow(
                () -> new AuthenticationException("Cant find user with that login")
        );
        String password = jwtRequest.getPassword();
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new AuthenticationException("Incorrect credentials");
        }

        return new JwtResponse("Bearer",
                jwtProvider.generateAccessToken(user),
                jwtProvider.generateRefreshToken(user));
    }

    public JwtResponse refreshAccessToken(String refreshToken) throws AuthenticationException {
        User user = getUserFromToken(refreshToken);

        return new JwtResponse("Bearer",
                jwtProvider.generateAccessToken(user),
                refreshToken);
    }

    public JwtResponse refreshRefreshToken(String refreshToken) throws AuthenticationException {
        User user = getUserFromToken(refreshToken);

        return new JwtResponse("Bearer",
                jwtProvider.generateAccessToken(user),
                jwtProvider.generateRefreshToken(user));
    }

    private User getUserFromToken(String token) throws AuthenticationException {
        if (!jwtProvider.isRefreshTokenValid(token)) {
            throw new AuthenticationException("Invalid refresh token");
        }

        Claims claims = jwtProvider.retrieveClaims(token);
        UUID uuid = UUID.fromString(claims.get("uuid", String.class));
        return repository.findById(uuid)
                .orElseThrow(() -> new AuthenticationException("User not found"));
    }

    public JwtAuthentication getJwtAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuthentication) {
            return (JwtAuthentication) authentication;
        }
        throw new IllegalStateException("Expected JwtAuthentication, but found: " + authentication);
    }
}
