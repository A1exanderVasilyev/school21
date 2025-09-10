package com.catinasw.T03.web.controller;

import com.catinasw.T03.domain.service.UserServiceImpl;
import com.catinasw.T03.domain.util.RegistrationResponse;
import com.catinasw.T03.web.model.SignUpRequest;
import com.catinasw.T03.web.model.dto.JwtRequest;
import com.catinasw.T03.web.model.dto.JwtResponse;
import com.catinasw.T03.web.model.dto.RefreshJwtRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserServiceImpl userService;

    @Autowired
    public AuthController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registration(@RequestBody SignUpRequest request) {
        RegistrationResponse response = userService.register(request);
        return response.isRegistrationSuccess() ? ResponseEntity.ok(response.getRegistrationMessage()) :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response.getRegistrationMessage());
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody JwtRequest jwtRequest) {
        JwtResponse jwtResponse;
        try {
            jwtResponse = userService.authenticate(jwtRequest);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(jwtResponse, HttpStatus.OK);
    }

    @PostMapping("/update-access-token")
    public ResponseEntity<JwtResponse> updateAccessToken(@RequestBody RefreshJwtRequest refreshJwtRequest) {
        String refreshToken = refreshJwtRequest.getRefreshToken();
        try {
            JwtResponse jwtResponse = userService.refreshAccessToken(refreshToken);
            return ResponseEntity.ok(jwtResponse);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/update-refresh-token")
    public ResponseEntity<JwtResponse> updateRefreshToken(@RequestBody RefreshJwtRequest refreshJwtRequest) {
        String refreshToken = refreshJwtRequest.getRefreshToken();
        try {
            JwtResponse jwtResponse = userService.refreshRefreshToken(refreshToken);
            return ResponseEntity.ok(jwtResponse);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
