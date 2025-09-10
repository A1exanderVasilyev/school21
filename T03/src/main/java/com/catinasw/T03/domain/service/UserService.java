package com.catinasw.T03.domain.service;

import com.catinasw.T03.domain.util.RegistrationResponse;
import com.catinasw.T03.web.model.SignUpRequest;
import com.catinasw.T03.web.model.dto.JwtRequest;
import com.catinasw.T03.web.model.dto.JwtResponse;

import javax.naming.AuthenticationException;

public interface UserService {
    RegistrationResponse register(SignUpRequest request);
    JwtResponse authenticate(JwtRequest authHeader) throws AuthenticationException;
}
