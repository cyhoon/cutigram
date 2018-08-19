package jeff.cutigram.controller;

import jeff.cutigram.database.model.User;
import jeff.cutigram.exception.AuthenticationException;
import jeff.cutigram.payload.request.LoginRequest;
import jeff.cutigram.payload.request.RegisterRequest;
import jeff.cutigram.payload.response.TokenResponse;
import jeff.cutigram.security.JwtTokenProvider;
import jeff.cutigram.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Objects;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserService userService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUserId(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        final String token = jwtTokenProvider.generateToken(authentication);
        return ResponseEntity.ok(new TokenResponse(token));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest registerRequest) {
        final String planTextPassword = registerRequest.getPassword();

        User user = userService.createUser(registerRequest);
        Authentication authentication = authenticate(user.getId(), planTextPassword);

        final String token = jwtTokenProvider.generateToken(authentication);
        return ResponseEntity.ok(new TokenResponse(token));
    }

    private Authentication authenticate(String userId, String password) throws UsernameNotFoundException {
        Objects.requireNonNull(userId);
        Objects.requireNonNull(password);

        Authentication authentication;

        try {
            return authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userId, password));
        } catch (DisabledException e) {
            throw new AuthenticationException("User is disabled!", e);
        } catch (BadCredentialsException e) {
            throw new AuthenticationException("Bad credentials!", e);
        }
    }
}
