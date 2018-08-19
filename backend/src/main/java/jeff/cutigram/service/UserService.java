package jeff.cutigram.service;

import jeff.cutigram.database.model.User;
import jeff.cutigram.database.repository.UserRepository;
import jeff.cutigram.exception.UserDuplicatedValueException;
import jeff.cutigram.payload.request.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Transactional
    public User createUser(RegisterRequest registerRequest) {
        final String userId = registerRequest.getUserId();
        final String password = passwordEncoder.encode(registerRequest.getPassword());
        final String displayName = registerRequest.getDisplayName();

        if (userRepository.existsById(userId)) throw new UserDuplicatedValueException("id");

        User user = User.builder()
                .id(userId)
                .password(password)
                .displayName(displayName)
                .build();

        return userRepository.save(user);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        User user = userRepository.findById(userId).orElse(null);

        if (user == null) {
            throw new UsernameNotFoundException(String.format("No user found with id '%s'", userId));
        } else {
            return user;
        }
    }

    @Transactional
    public UserDetails loadUserById(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("User not found with id: "+ userId));

        return User.builder()
                .id(user.getId())
                .password(user.getPassword())
                .displayName(user.getDisplayName())
                .build();
    }
}
