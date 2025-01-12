package rs.raf.users_service.controller;

import io.jsonwebtoken.Claims;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.raf.users_service.models.Clients;
import rs.raf.users_service.models.DTOs.ClientDTO;
import rs.raf.users_service.models.Users;
import rs.raf.users_service.models.requests.ClientRegistrationRequest;
import rs.raf.users_service.service.ClientService;
import rs.raf.users_service.service.UserService;
import rs.raf.users_service.service.jwt.JwtTokenUtil;

import java.util.List;

@RestController
@RequestMapping("/client")
public class ClientsController {
    private final ClientService clientService;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserService userService;

    public ClientsController(ClientService clientService, JwtTokenUtil jwtTokenUtil, UserService userService) {
        this.clientService = clientService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<ClientDTO>> getAllClients() {
        return clientService.getAllClients()
                .isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(clientService.getAllClients());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientDTO> getClientById(@PathVariable Long id) {
        return clientService.getClientById(id) == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(clientService.getClientById(id));
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerClient(@RequestBody ClientRegistrationRequest client) {
        if (client.username().contains("-") || client.username().contains(":")) {
            return new ResponseEntity<>("Username cannot contain - or :", HttpStatus.BAD_REQUEST);
        }
        Users clientByUsername = userService.getUserByUsername(client.username());
        if (clientByUsername != null) {
            return new ResponseEntity<>("User with username already exists", HttpStatus.CONFLICT);
        }
        Users clientByEmail = userService.getUserByEmail(client.email());
        if (clientByEmail != null) {
            return new ResponseEntity<>("User with email already exists", HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(clientService.createClient(client), HttpStatus.CREATED);
    }


    @PutMapping("/update-reservation")
    public ResponseEntity<String> addReservation(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestParam String operation
    ) {
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
            Long userId = Long.parseLong(parts[0]);
            String role = parts[1].split("-")[1];

            if (!role.equals("SERVER")) {
                return new ResponseEntity<>("Invalid role", HttpStatus.UNAUTHORIZED);
            }

            if (!operation.equals("increment") && !operation.equals("decrement")) {
                return new ResponseEntity<>("Invalid operation", HttpStatus.BAD_REQUEST);
            }

            ClientDTO clientFromDb = clientService.getClientById(userId);

            clientService.updateReservationNumber(clientFromDb.id(), operation);
            return ResponseEntity.ok("Client updated successfully");

        } catch (RuntimeException e) {
            return new ResponseEntity<>("Invalid or expired token: " + e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }
}
