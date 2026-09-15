package module.User;

public class LoginResponseDto {
    private final String token;
    private final Role role;
    private final String email;
    private final String name;

    public LoginResponseDto(String token, Role role, String email, String name) {
        this.token = token;
        this.role = role;
        this.email = email;
        this.name = name;
    }

    public String getToken() {
        return token;
    }

    public Role getRole() {
        return role;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "LoginResponseDto{" +
                "token='[PASSWORD]'" + // Avoid logging token
                ", role=" + role +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
