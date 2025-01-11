package rs.raf.users_service.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import rs.raf.users_service.models.Managers;
import rs.raf.users_service.models.Users;
import rs.raf.users_service.models.requests.UserDataRequest;
import rs.raf.users_service.models.requests.UserLoginRequest;
import rs.raf.users_service.repository.UserRepository;
import rs.raf.users_service.service.jwt.JwtTokenUtil;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final JwtTokenUtil jwtTokenUtil;

    public UserService(UserRepository userRepository, JwtTokenUtil jwtTokenUtil) {
        this.userRepository = userRepository;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    public Users getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public String login(UserLoginRequest userLoginRequest) {
        Users user = userRepository.findByUsernameAndPassword(userLoginRequest.username(), userLoginRequest.password())
                .orElse(null);
        if(user != null) {
//            if (!user.getActive()) {
//                return "User is not active.";
//            }
            if (user.getBlocked()){
                return "User is blocked.";
            }
            return jwtTokenUtil.generateToken(user.getId(), user.getUsername(), user.getRole());
        }
        return "Invalid username or password.";
    }

    public Users getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    public ResponseEntity<String> updateUser(Users existingUser, UserDataRequest user) {
        if (userRepository.existsById(existingUser.getId())) {

            // Set non-optional fields
            existingUser.setUsername(user.username());
            existingUser.setEmail(user.email());
            existingUser.setDateOfBirth(user.dateOfBirth());
            existingUser.setName(user.name());
            existingUser.setSurname(user.surname());

            // Set optional fields if present
            user.password().ifPresent(existingUser::setPassword);

            if (existingUser instanceof Managers) {
                // Validate and set Manager-specific fields
                if (user.restaurantName().isEmpty()) {
                    return new ResponseEntity<>("Restaurant name is required.", HttpStatus.BAD_REQUEST);
                }
                if (user.dateOfEmployment().isEmpty()) {
                    return new ResponseEntity<>("Date of employment is required.", HttpStatus.BAD_REQUEST);
                }

                // Safely set Manager-specific optional fields
                user.restaurantName().ifPresent(((Managers) existingUser)::setRestaurantName);
                user.dateOfEmployment().ifPresent(((Managers) existingUser)::setDateOfEmployment);
            }

            // Save the updated user
            userRepository.save(existingUser);
            return ResponseEntity.ok("User updated successfully.");
        } else {
            return new ResponseEntity<>("User with id " + existingUser.getId() + " does not exist.", HttpStatus.NOT_FOUND);
        }
    }


}
