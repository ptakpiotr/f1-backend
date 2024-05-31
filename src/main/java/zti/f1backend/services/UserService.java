package zti.f1backend.services;

import org.springframework.security.authentication.*;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import zti.f1backend.data.UserRepository;
import zti.f1backend.models.User;
import zti.f1backend.models.dto.LoginDTO;
import zti.f1backend.util.JwtUtils;

@Service
public class UserService {
    private UserRepository userRepository;
    private AuthenticationProvider provider;

    public UserService(UserRepository userRepository, AuthenticationProvider provider) {
        this.userRepository = userRepository;
        this.provider = provider;
    }

    public String login(LoginDTO loginDto) {
        final var authentication = provider.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.getEmail(),
                        loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        final var user = userRepository.findByEmail(loginDto.getEmail());
        return JwtUtils.generateToken(user.getEmail());
    }

    public User getUser() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.findByEmail(userDetails.getUsername());
    }
}
