package rs.raf.users_service.controller;

import io.jsonwebtoken.Claims;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.raf.users_service.models.DTOs.ManagerDTO;
import rs.raf.users_service.models.Managers;
import rs.raf.users_service.models.requests.ManagerRegistrationRequest;
import rs.raf.users_service.service.ManagerService;
import rs.raf.users_service.service.jwt.JwtTokenUtil;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/manager")
public class ManagersController {
    private final ManagerService managerService;
    private final JwtTokenUtil jwtTokenUtil;

    public ManagersController(ManagerService managerService, JwtTokenUtil jwtTokenUtil) {
        this.managerService = managerService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @GetMapping
    public ResponseEntity<List<ManagerDTO>> getAllManagers() {
        return managerService.getAllManagers()
                .isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(managerService.getAllManagers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ManagerDTO> getManagerById(@PathVariable Long id) {
        return managerService.getManagerById(id) == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(managerService.getManagerById(id));
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerManager(@RequestBody ManagerRegistrationRequest manager) {
        if (manager.username().contains("-") || manager.username().contains(":")) {
            return new ResponseEntity<>("Username cannot contain - or :", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(managerService.createManager(manager), HttpStatus.CREATED);
    }

    @PostMapping("/restaurant/{newRestaurantName}")
    public ResponseEntity<String> updateManager(@PathVariable String newRestaurantName,
            @RequestHeader("Authorization") String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return new ResponseEntity<>("Authorization header is missing or invalid", HttpStatus.UNAUTHORIZED);
        }
        String token = authorizationHeader.substring(7);
        Claims claims = jwtTokenUtil.validateToken(token);
        String subject = claims.getSubject();
        String[] parts = subject.split(":");
        if (parts.length != 2) {
            throw new RuntimeException("Invalid token format");
        }
        Long userIdFromToken = Long.parseLong(parts[0]);
        String username = parts[1].split("-")[0];

        Optional<Managers> managerToChangeTheRestaurantName = managerService.findManagerById(userIdFromToken);

        if (!managerToChangeTheRestaurantName.isPresent()) {
            return new ResponseEntity<>("Manager not found", HttpStatus.NOT_FOUND);
        }

        Managers managerFromDB = managerToChangeTheRestaurantName.get();
        managerFromDB.setRestaurantName(newRestaurantName);

        managerService.updateManager(managerFromDB);


        return new ResponseEntity<>("Manager updated", HttpStatus.OK);
    }

}
