package rs.raf.users_service.controller;

import io.jsonwebtoken.Claims;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.raf.users_service.models.ROLES;
import rs.raf.users_service.models.Users;
import rs.raf.users_service.service.AdminService;
import rs.raf.users_service.service.UserService;
import rs.raf.users_service.service.jwt.JwtTokenUtil;

@RestController
@RequestMapping("/admin")
public class AdminsController {
    private final AdminService adminService;
    private final UserService userService;
    private final JwtTokenUtil jwtTokenUtil;

    public AdminsController(AdminService adminService, UserService userService, JwtTokenUtil jwtTokenUtil) {
        this.adminService = adminService;
        this.userService = userService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @PostMapping("/{id}/toggle-block")
    public ResponseEntity<?> disableUser(@PathVariable Long id, @RequestHeader("Authorization") String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return new ResponseEntity<>("Authorization header is missing or invalid", HttpStatus.UNAUTHORIZED);
        }
        try {
            String token = authorizationHeader.substring(7);
            Claims claims = jwtTokenUtil.validateToken(token);
            String subject = claims.getSubject();
            String[] parts = subject.split(":");
            if (parts.length != 2) {
                throw new RuntimeException("Invalid token format");
            }
            Long userIdFromToken = Long.parseLong(parts[0]);
            String username = parts[1].split("-")[0];
            String role = parts[1].split("-")[1];

            if (!role.equals(ROLES.ADMIN.toString())) {
                return new ResponseEntity<>("Invalid role", HttpStatus.UNAUTHORIZED);
            }

            Users user = userService.getUserById(id);

            if (user == null) {
                return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
            }
            if (user.getRole() == ROLES.ADMIN) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            return userService.toggleBlock(user);

        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }
}
