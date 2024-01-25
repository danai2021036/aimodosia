package gr.hua.dit.aimodotes.demo.rest;

import gr.hua.dit.aimodotes.demo.entity.Role;
import gr.hua.dit.aimodotes.demo.entity.User;
import gr.hua.dit.aimodotes.demo.payload.response.MessageResponse;
import gr.hua.dit.aimodotes.demo.repository.RoleRepository;
import gr.hua.dit.aimodotes.demo.repository.UserRepository;
import gr.hua.dit.aimodotes.demo.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/admin")
public class AdminRestController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    BCryptPasswordEncoder encoder;

    //admin can see all the users' details
    @GetMapping("/users")
    @Secured("ROLE_ADMIN")
    public List<User> getUsers() {
        return userDetailsService.getUsers();
    }

    //admin can see one user's details
    @GetMapping("/users/{user_id}")
    @Secured("ROLE_ADMIN")
    public User getUser(@PathVariable Integer user_id) {
        return userDetailsService.getUser(user_id);
    }

    //admin can add a role to the user
    @PostMapping("/addroles/{user_id}/{role_id}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<Map<String,String>> addRole(@PathVariable Integer user_id, @PathVariable Integer role_id){
        Map<String,String> response = new HashMap<>();
        User user = userDetailsService.getUser(user_id);
        Role role = roleRepository.findById(role_id).get();
        user.getRoles().add(role);
        userRepository.save(user);
        response.put("message","Added Role");
        return ResponseEntity.ok(response);
    }


    //admin can remove a role from a user
    @DeleteMapping("/deleteroles/{user_id}/{role_id}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<String> deleteRole(@PathVariable Integer user_id, @PathVariable Integer role_id){
        User user = userRepository.findById(user_id).get();
        Set<Role> roles = user.getRoles();
        roles.remove(this.roleRepository.findById(role_id).orElseThrow());
        user.setRoles(roles);
        userRepository.save(user);
        return ResponseEntity.ok("Removed Role");
    }

    @PostMapping("/user/new")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<String> createNewUser(@RequestBody User user){
        if (userRepository.existsByUsername(user.getUsername())) {
            return ResponseEntity.badRequest().body("Error username already exists!");
        }
        if(userRepository.existsByEmail(user.getEmail())){
            return ResponseEntity.badRequest().body("Error email is already in use!");
        }
        Set<Role> roles = new HashSet<>();
        roles.add(this.roleRepository.findByName("ROLE_USER").orElseThrow());
        user.setRoles(roles);
        user.setPassword(encoder.encode(user.getPassword()));
        userRepository.save(user);
        return ResponseEntity.ok("Added user!");
    }

    @PutMapping("/user/update/{user_id}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<String> updateUser(@PathVariable Integer user_id, @RequestBody User updatedUser){
        Optional<User> existingUserOptional = userRepository.findById(user_id);
        if(existingUserOptional.isPresent()){
            User existingUser = existingUserOptional.get();
            existingUser.setUsername(updatedUser.getUsername());
            existingUser.setEmail(updatedUser.getEmail());
            userRepository.save(existingUser);
            return ResponseEntity.ok("User updated!");
        }else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/user/delete/{user_id}")
    @Secured("ROLE_ADMIN")
    public void deleteUser(@PathVariable Integer user_id){
        User user = userRepository.findById(user_id).get();
        userRepository.delete(user);
    }
}
