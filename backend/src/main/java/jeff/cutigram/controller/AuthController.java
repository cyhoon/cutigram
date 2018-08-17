package jeff.cutigram.controller;

import jeff.cutigram.dto.auth.RegisterDto;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @GetMapping("/")
    public String test() {
        return "test";
    }

    @PostMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/register")
    public String register(@Valid @RequestBody RegisterDto dto) {
        final String password = dto.getPassword();
        System.out.println("여기 안옴?");
        return "register";
    }
}
