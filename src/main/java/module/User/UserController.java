package module.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import comman.response.ApiResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin/users")
public class UserController {
    @Autowired
    private UserService userService;

    // only super admin have the access
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PostMapping("/createAdmin")
    public ResponseEntity<ApiResponse<UserDto>> createAdmin(
            @Valid @RequestBody RegisterRequestDto requestDto) {
        UserDto response = userService.createAdmin(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Admin created successfully", response));
    }
}
