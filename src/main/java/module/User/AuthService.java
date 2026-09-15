package module.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import comman.exceptions.BadRequestException;
import comman.exceptions.UnauthorizedException;
import comman.security.JwtTokenProvider;

@Service
public class AuthService {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserRepository userRepository;

    public LoginResponseDto register(RegisterRequestDto requestDto) {
        String email = requestDto.getEmail();

        if (userRepository.existsByEmail(email)) {
            throw new BadRequestException("Email is already registered: " + email);
        }

        UserEntity user = new UserEntity();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        user.setName(requestDto.getName());
        user.setRole(Role.USER);
        user.setIsActive(true);

        UserEntity savedUser = userRepository.save(user);
        String token = jwtTokenProvider.generateToken(savedUser);

        return new LoginResponseDto(token, savedUser.getRole(), savedUser.getEmail(), savedUser.getName());
    }

    public LoginResponseDto login(LoginRequestDto requestDto) {
        String email = requestDto.getEmail();

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UnauthorizedException("Invalid email or password"));

        if (Boolean.FALSE.equals(user.getIsActive())) {
            throw new UnauthorizedException("User account is inactive. Please contact administrator.");
        }

        boolean matches = passwordEncoder.matches(requestDto.getPassword(), user.getPassword());
        if (!matches) {
            throw new UnauthorizedException("Invalid email or password");
        }

        String token = jwtTokenProvider.generateToken(user);

        return new LoginResponseDto(token, user.getRole(), user.getEmail(), user.getName());
    }
}
