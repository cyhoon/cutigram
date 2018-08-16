package jeff.cutigram.controller;

import jeff.cutigram.dto.auth.RegisterDto;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @PostMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/register")
    public String register(@Valid @RequestBody RegisterDto dto) {
        final String password = dto.getPassword();

        return "register";
    }
}
