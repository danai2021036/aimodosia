package gr.hua.dit.aimodotes.demo.rest;

import gr.hua.dit.aimodotes.demo.entity.Role;
import gr.hua.dit.aimodotes.demo.entity.User;
import gr.hua.dit.aimodotes.demo.repository.RoleRepository;
import gr.hua.dit.aimodotes.demo.repository.UserRepository;
import gr.hua.dit.aimodotes.demo.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/admin")
public class AdminRestController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;


    @GetMapping("")
    @Secured("ROLE_ADMIN")
    public List<User> getUsers() {
        return userDetailsService.getUsers();
    }

    @GetMapping("{user_id}")
    @Secured("ROLE_ADMIN")
    public User getUser(@PathVariable Integer user_id) {
        return userDetailsService.getUser(user_id);
    }

    @PostMapping("/addroles/{user_id}/{role_id}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<String> addRole(@PathVariable Integer user_id, @PathVariable Integer role_id){
        User user = userDetailsService.getUser(user_id);
        Role role = roleRepository.findById(role_id).get();
        user.getRoles().add(role);
        userRepository.save(user);
        return ResponseEntity.ok("Added Role");
    }

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

}
