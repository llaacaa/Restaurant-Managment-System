package rs.raf.users_service.controller;

import io.jsonwebtoken.Claims;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.raf.users_service.models.*;
import rs.raf.users_service.models.requests.UserDataRequest;
import rs.raf.users_service.models.requests.UserLoginRequest;
import rs.raf.users_service.service.AdminService;
import rs.raf.users_service.service.ClientService;
import rs.raf.users_service.service.ManagerService;
import rs.raf.users_service.service.UserService;
import rs.raf.users_service.service.jwt.JwtTokenUtil;
import rs.raf.users_service.service.mappers.RequestToClass.RequestClientMapper;
import rs.raf.users_service.service.mappers.RequestToClass.RequestManagerMapper;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UsersController {
    private final UserService userService;
    private final ManagerService managerService;
    private final ClientService clientService;
    private final JwtTokenUtil jwtTokenUtil;
    private final AdminService adminService;

    public UsersController(UserService userService, ManagerService managerService, ClientService clientService, JwtTokenUtil jwtTokenUtil, AdminService adminService) {
        this.userService = userService;
        this.managerService = managerService;
        this.clientService = clientService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.adminService = adminService;
    }

    @GetMapping("/all")
    public List<Users> getUsers() {
        return  userService.getAllUsers();
    }

    @GetMapping
    public ResponseEntity<?> getUserFromToken(@RequestHeader(value = "Authorization") String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return new ResponseEntity<>("Authorization header is missing or invalid", HttpStatus.UNAUTHORIZED);
        }
        String token = authorizationHeader.substring(7);
        try {
            Claims claims = jwtTokenUtil.validateToken(token);

            String subject = claims.getSubject();
            String[] parts = subject.split(":");
            if (parts.length != 2) {
                throw new RuntimeException("Invalid token format");
            }
            Long userIdFromToken = Long.parseLong(parts[0]);
            String username = parts[1].split("-")[0];

            Users userFromDb = userService.getUserById(userIdFromToken);
            if (userFromDb == null) {
                return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
            }

            if (userFromDb instanceof Clients) {
                return new ResponseEntity<>(clientService.getClientById(userFromDb.getId()), HttpStatus.OK);
            }
            if (userFromDb instanceof Admins) {
                return new ResponseEntity<>(adminService.getAdminById(userFromDb.getId()), HttpStatus.OK);
            }
            if (userFromDb instanceof Managers) {
                return new ResponseEntity<>(managerService.getManagerById(userFromDb.getId()), HttpStatus.OK);
            }
            return new ResponseEntity<>("Invalid role", HttpStatus.INTERNAL_SERVER_ERROR);

        } catch (RuntimeException e) {
            return new ResponseEntity<>("Internal error: " + e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }


    @PostMapping("/login")
    public ResponseEntity<?> loginClient(@RequestBody UserLoginRequest loginData) {
        return userService.login(loginData);
    }

    @PatchMapping("/update")
    public ResponseEntity<String> updateUser(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody UserDataRequest userData) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return new ResponseEntity<>("Authorization header is missing or invalid", HttpStatus.UNAUTHORIZED);
        }
        String token = authorizationHeader.substring(7);
        if (userData.id() == null) {
            return new ResponseEntity<>("ID is required", HttpStatus.BAD_REQUEST);
        }
        if (userData.username() == null || userData.username().isBlank()) {
            return new ResponseEntity<>("Username is required", HttpStatus.BAD_REQUEST);
        }
        if (userData.email() == null || userData.email().isBlank()) {
            return new ResponseEntity<>("Email is required", HttpStatus.BAD_REQUEST);
        }
        if (userData.dateOfBirth() == null) {
            return new ResponseEntity<>("Date of birth is required", HttpStatus.BAD_REQUEST);
        }
        if (userData.name() == null || userData.name().isBlank()) {
            return new ResponseEntity<>("Name is required", HttpStatus.BAD_REQUEST);
        }
        if (userData.surname() == null || userData.surname().isBlank()) {
            return new ResponseEntity<>("Surname is required", HttpStatus.BAD_REQUEST);
        }

        try {
            Claims claims = jwtTokenUtil.validateToken(token);

            String subject = claims.getSubject();
            String[] parts = subject.split(":");
            if (parts.length != 2) {
                throw new RuntimeException("Invalid token format");
            }
            Long userIdFromToken = Long.parseLong(parts[0]);
            String username = parts[1].split("-")[0];

            Users userFromDb = userService.getUserById(userIdFromToken);
            Users userToChange = userService.getUserById(userData.id());

            if (userToChange == null) {
                return new ResponseEntity<>("User with id " + userData.id() + " does not exist.", HttpStatus.NOT_FOUND);
            }

            if (!userData.password().isPresent()) {
                // MEANJAMO DRUGU OSOBU -> UserFromDb != userData
                // TO MOZE SAMO ADMIN
                // SAMO ADMIN MOZE SVOJ PROFIL, TJ NE MOGU DRUGI ADMINI
                if (userFromDb.getRole() != ROLES.ADMIN && !userFromDb.getId().equals(userToChange.getId())) {
                    return new ResponseEntity<>("You are not authorized to update this user", HttpStatus.FORBIDDEN);
                }
            } else {
                // MEANJAMO SEBE -> UserFromDb == userData
                // ID-ovi MORAJU DA SE POKLOPE
                if (!userFromDb.getId().equals(userToChange.getId())) {
                    return new ResponseEntity<>("You are not authorized to update this user", HttpStatus.FORBIDDEN);
                }
            }

            // VRATI PROMENJENOG USERA
            return userService.updateUser(userToChange, userData);

        } catch (RuntimeException e) {
            return new ResponseEntity<>("Internal error: " + e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

}




