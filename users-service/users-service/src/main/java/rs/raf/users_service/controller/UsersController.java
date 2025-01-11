package rs.raf.users_service.controller;

import io.jsonwebtoken.Claims;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.raf.users_service.models.ROLES;
import rs.raf.users_service.models.Users;
import rs.raf.users_service.models.requests.UserDataRequest;
import rs.raf.users_service.models.requests.UserLoginRequest;
import rs.raf.users_service.service.ClientService;
import rs.raf.users_service.service.ManagerService;
import rs.raf.users_service.service.UserService;
import rs.raf.users_service.service.jwt.JwtTokenUtil;
import rs.raf.users_service.service.mappers.RequestToClass.RequestClientMapper;
import rs.raf.users_service.service.mappers.RequestToClass.RequestManagerMapper;

@RestController
@RequestMapping("/user")
public class UsersController {
    private final UserService userService;
    private final ManagerService managerService;
    private final ClientService clientService;
    private final JwtTokenUtil jwtTokenUtil;
    private final RequestClientMapper requestClientMapper;
    private final RequestManagerMapper requestManagerMapper;

    public UsersController(UserService userService, ManagerService managerService, ClientService clientService,
                           JwtTokenUtil jwtTokenUtil, RequestClientMapper requestClientMapper, RequestManagerMapper requestManagerMapper) {
        this.userService = userService;
        this.managerService = managerService;
        this.clientService = clientService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.requestClientMapper = requestClientMapper;
        this.requestManagerMapper = requestManagerMapper;
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginClient(@RequestBody UserLoginRequest loginData) {
        return new ResponseEntity<>(userService.login(loginData), HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateManager(
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




