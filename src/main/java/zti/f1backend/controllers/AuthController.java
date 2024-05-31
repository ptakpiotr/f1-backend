package zti.f1backend.controllers;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import zti.f1backend.data.UserRepository;
import zti.f1backend.models.GeneralizedResponse;
import zti.f1backend.models.User;
import zti.f1backend.models.dto.LoginDTO;
import zti.f1backend.models.dto.UserDTO;
import zti.f1backend.services.UserService;
import zti.f1backend.util.JwtUtils;
import zti.f1backend.util.PasswordEncoder;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final UserService userService;
    private final PasswordEncoder encoder;

    public AuthController(UserRepository userRepository, UserService userService,
            @Qualifier("passwordEncoder") PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.encoder = encoder;
    }

    @PostMapping("login")
    public ResponseEntity<GeneralizedResponse> login(@Valid @RequestBody LoginDTO login) {
        User user = userRepository.findByEmail(login.getEmail());

        if (user == null) {
            return new ResponseEntity<>(new GeneralizedResponse("User cannot be retrieved"), HttpStatus.BAD_REQUEST);
        }
        String loginPass = login.getPassword();
        String savedPass = user.getPasswordHash();

        if (!encoder.matches(loginPass, savedPass)) {
            return new ResponseEntity<>(new GeneralizedResponse("User cannot be retrieved"), HttpStatus.BAD_REQUEST);
        }

        String token = userService.login(login);

        return new ResponseEntity<>(new GeneralizedResponse(token), HttpStatus.OK);
    }

    @PostMapping("register")
    public ResponseEntity<GeneralizedResponse> register(@Valid @RequestBody UserDTO user) {
        if (!(user.getPassword().equals(user.getConfirmedPassword()))) {
            return new ResponseEntity<>(new GeneralizedResponse("Invalid user request"), HttpStatus.BAD_REQUEST);
        }

        var exisitingUser = userRepository.findByEmail(user.getEmail());

        if (exisitingUser != null) {
            return new ResponseEntity<>(new GeneralizedResponse("Invalid user request"), HttpStatus.BAD_REQUEST);
        }

        User newUser = new User();
        newUser.setEmail(user.getEmail());

        String result = encoder.encode(user.getPassword());
        newUser.setPasswordHash(result);

        userRepository.save(newUser);
        String token = JwtUtils.generateToken(newUser.getEmail());

        return new ResponseEntity<>(new GeneralizedResponse(token), HttpStatus.CREATED);
    }

    @DeleteMapping()
    public ResponseEntity<HttpStatusCode> delete() {
        try {
            SecurityContext context = SecurityContextHolder.getContext();
            UserDetails userDetails = (UserDetails) context.getAuthentication().getPrincipal();

            User user = userRepository.findByEmail(userDetails.getUsername());

            userRepository.deleteById(user.getId());
        } catch (Exception ex) {

        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
