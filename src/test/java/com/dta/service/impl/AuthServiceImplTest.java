package com.dta.service.impl;

import com.dta.dto.request.LoginRequest;
import com.dta.dto.request.RefreshRequest;
import com.dta.dto.request.RegisterRequest;
import com.dta.dto.response.AuthResponse;
import com.dta.entity.RefreshToken;
import com.dta.entity.User;
import com.dta.entity.UserRole;
import com.dta.exception.ConflictException;
import com.dta.exception.UnauthorizedException;
import com.dta.repository.RefreshTokenRepository;
import com.dta.repository.UserRepository;
import com.dta.security.JwtProperties;
import com.dta.security.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private JwtProperties jwtProperties;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    void testRegister_Success() {
        RegisterRequest request = new RegisterRequest();
        request.setFullName("Jane Doe");
        request.setEmail("jane@example.com");
        request.setPassword("password123");

        User savedUser = new User();
        savedUser.setId(UUID.randomUUID());
        savedUser.setFullName("Jane Doe");
        savedUser.setEmail("jane@example.com");
        savedUser.setRole(UserRole.USER);

        when(userRepository.existsByEmailIgnoreCase("jane@example.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("hashedPassword");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        
        when(jwtUtil.generateAccessToken(savedUser.getId(), savedUser.getEmail(), "USER")).thenReturn("mockAccessToken");
        when(jwtUtil.generateRefreshToken(savedUser.getId())).thenReturn("mockRefreshToken");
        when(jwtProperties.getRefreshTokenExpiration()).thenReturn(86400000L);

        AuthResponse response = authService.register(request);

        assertNotNull(response);
        assertEquals("jane@example.com", response.getEmail());
        assertEquals("mockAccessToken", response.getAccessToken());
        verify(refreshTokenRepository).save(any(RefreshToken.class));
    }

    @Test
    void testRegister_EmailExists_ThrowsConflictException() {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("jane@example.com");

        when(userRepository.existsByEmailIgnoreCase("jane@example.com")).thenReturn(true);

        assertThrows(ConflictException.class, () -> authService.register(request));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testLogin_Success() {
        LoginRequest request = new LoginRequest();
        request.setEmail("jane@example.com");
        request.setPassword("password123");

        User user = new User();
        user.setId(UUID.randomUUID());
        user.setEmail("jane@example.com");
        user.setRole(UserRole.USER);

        when(userRepository.findByEmailIgnoreCase("jane@example.com")).thenReturn(Optional.of(user));
        when(jwtUtil.generateAccessToken(any(), anyString(), anyString())).thenReturn("mockAccessToken");
        when(jwtUtil.generateRefreshToken(any())).thenReturn("mockRefreshToken");
        when(jwtProperties.getRefreshTokenExpiration()).thenReturn(86400000L);

        AuthResponse response = authService.login(request);

        assertNotNull(response);
        assertEquals("mockAccessToken", response.getAccessToken());
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    void testRefresh_Success() {
        RefreshRequest request = new RefreshRequest();
        request.setRefreshToken("validOldToken");

        UUID userId = UUID.randomUUID();
        RefreshToken storedToken = new RefreshToken();
        storedToken.setToken("validOldToken");
        storedToken.setUserId(userId);
        storedToken.setRevoked(false);
        storedToken.setExpiresAt(Instant.now().plusSeconds(3600));

        User user = new User();
        user.setId(userId);
        user.setEmail("jane@example.com");
        user.setRole(UserRole.USER);

        when(refreshTokenRepository.findByToken("validOldToken")).thenReturn(Optional.of(storedToken));
        when(jwtUtil.validate("validOldToken")).thenReturn(true);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(jwtUtil.generateAccessToken(any(), anyString(), anyString())).thenReturn("newAccessToken");
        when(jwtUtil.generateRefreshToken(any())).thenReturn("newRefreshToken");

        AuthResponse response = authService.refresh(request);

        assertNotNull(response);
        assertEquals("newAccessToken", response.getAccessToken());
        assertTrue(storedToken.isRevoked()); // Verifies the old token was revoked
        verify(refreshTokenRepository, times(2)).save(any(RefreshToken.class));
    }

    @Test
    void testLogout_Success() {
        RefreshRequest request = new RefreshRequest();
        request.setRefreshToken("tokenToRevoke");

        RefreshToken storedToken = new RefreshToken();
        storedToken.setRevoked(false);

        when(refreshTokenRepository.findByToken("tokenToRevoke")).thenReturn(Optional.of(storedToken));

        authService.logout(request);

        assertTrue(storedToken.isRevoked());
        verify(refreshTokenRepository).save(storedToken);
    }
}