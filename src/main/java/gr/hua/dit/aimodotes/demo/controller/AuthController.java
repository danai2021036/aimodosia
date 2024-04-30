package gr.hua.dit.aimodotes.demo.controller;

import gr.hua.dit.aimodotes.demo.config.JwtUtils;
import gr.hua.dit.aimodotes.demo.entity.Role;
import gr.hua.dit.aimodotes.demo.entity.User;
import gr.hua.dit.aimodotes.demo.payload.request.LoginRequest;
import gr.hua.dit.aimodotes.demo.payload.request.SignupRequest;
import gr.hua.dit.aimodotes.demo.payload.response.JwtResponse;
import gr.hua.dit.aimodotes.demo.payload.response.MessageResponse;
import gr.hua.dit.aimodotes.demo.repository.RoleRepository;
import gr.hua.dit.aimodotes.demo.repository.UserRepository;
import gr.hua.dit.aimodotes.demo.service.UserDetailsImpl;
import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    BCryptPasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    //roles setup and creation of 1 admin, 1 secretary, 3 blood donors
    @PostConstruct
    public void setup() {
        roleRepository.findByName("ROLE_ADMIN").orElseGet(() -> {
            roleRepository.save(new Role("ROLE_ADMIN"));
            return null;
        });
        roleRepository.findByName("ROLE_USER").orElseGet(() -> {
            roleRepository.save(new Role("ROLE_USER"));
            return null;
        });
        roleRepository.findByName("ROLE_SECRETARY").orElseGet(() -> {
            roleRepository.save(new Role("ROLE_SECRETARY"));
            return null;
        });
        roleRepository.findByName("ROLE_AIMODOTIS").orElseGet(() -> {
            roleRepository.save(new Role("ROLE_AIMODOTIS"));
            return null;
        });


        this.userRepository.findByEmail("admin@gmail.com").orElseGet(() -> {
            User user = new User("admin","admin@gmail.com", encoder.encode("1234"));
            Set<Role> roles = new HashSet<>();
            roles.add(this.roleRepository.findByName("ROLE_USER").orElseThrow());
            roles.add(this.roleRepository.findByName("ROLE_ADMIN").orElseThrow());
            user.setRoles(roles);
            userRepository.save(user);
            return null;
        });

        this.userRepository.findByEmail("sec@gmail.com").orElseGet(() -> {
            User user = new User("sec","sec@gmail.com", encoder.encode("1234"));
            Set<Role> roles = new HashSet<>();
            roles.add(this.roleRepository.findByName("ROLE_USER").orElseThrow());
            roles.add(this.roleRepository.findByName("ROLE_SECRETARY").orElseThrow());
            user.setRoles(roles);
            userRepository.save(user);
            return null;
        });

        this.userRepository.findByEmail("naf@gmail.com").orElseGet(() -> {
            User user = new User("naf","naf@gmail.com", encoder.encode("1234"));
            Set<Role> roles = new HashSet<>();
            roles.add(this.roleRepository.findByName("ROLE_USER").orElseThrow());
            roles.add(this.roleRepository.findByName("ROLE_AIMODOTIS").orElseThrow());
            user.setRoles(roles);
            userRepository.save(user);
            return null;
        });

        this.userRepository.findByEmail("geo@gmail.com").orElseGet(() -> {
            User user = new User("geo","geo@gmail.com", encoder.encode("1234"));
            Set<Role> roles = new HashSet<>();
            roles.add(this.roleRepository.findByName("ROLE_USER").orElseThrow());
            roles.add(this.roleRepository.findByName("ROLE_AIMODOTIS").orElseThrow());
            user.setRoles(roles);
            userRepository.save(user);
            return null;
        });

        this.userRepository.findByEmail("dan@gmail.com").orElseGet(() -> {
            User user = new User("dan","dan@gmail.com", encoder.encode("1234"));
            Set<Role> roles = new HashSet<>();
            roles.add(this.roleRepository.findByName("ROLE_USER").orElseThrow());
            roles.add(this.roleRepository.findByName("ROLE_AIMODOTIS").orElseThrow());
            user.setRoles(roles);
            userRepository.save(user);
            return null;
        });
    }

    //signin/authentication
    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        System.out.println("authentication");

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        System.out.println("authentication: " + authentication);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        System.out.println("post authentication");
        String jwt = jwtUtils.generateJwtToken(authentication);
        System.out.println("jw: " + jwt);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles));
    }

    //signup / create new user with the role of user only
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user's account
        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName("ROLE_USER")
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);

                        break;
                    case "secretary":
                        Role secRole = roleRepository.findByName("ROLE_SECRETARY")
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(secRole);

                        break;
                    case "aimodotis":
                        Role aimRole = roleRepository.findByName("ROLE_AIMODOTIS")
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(aimRole);
                        break;

                    default:
                        Role userRole = roleRepository.findByName("ROLE_USER")
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }



}
