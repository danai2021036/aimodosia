package gr.hua.dit.aimodotes.demo.rest;

import gr.hua.dit.aimodotes.demo.dao.SecretaryDAO;
import gr.hua.dit.aimodotes.demo.entity.Aimodotis;
import gr.hua.dit.aimodotes.demo.entity.Secretary;
import gr.hua.dit.aimodotes.demo.repository.SecretaryRepository;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/secretary")
//@Hidden
public class SecretaryRestController {
    @Autowired
    private SecretaryRepository secretaryRepository;

    @Autowired
    private SecretaryDAO secretaryDAO;

    @PostConstruct
    public void setup() {
        secretaryRepository.findByAFM("123456789").orElseGet(() -> {
            secretaryRepository.save(new Secretary("Maria", "Papa","123456789"));
            return null;
        });
    }

    @GetMapping("")
    public List<Secretary> getSecretaries(){
        return secretaryDAO.getSecretaries();
    }

    @PostMapping("/new")
    public Secretary saveSecretary(@RequestBody Secretary secretary){
        if(secretaryRepository.findByAFM(secretary.getAFM()).isPresent()){
            System.out.println("Secretary already exists.");
            return null;
        }else {
            return secretaryDAO.saveSecretary(secretary);
        }
    }

    //delete
    @DeleteMapping("/delete/{secretary_id}")
    public void deleteSecretary(@PathVariable Integer secretary_id){
        secretaryDAO.deleteSecretary(secretary_id);
    }

    //enan
    @GetMapping("{secretary_id}")
    public Secretary getSecretary(@PathVariable Integer secretary_id){
        return secretaryDAO.getSecretary(secretary_id);
    }

    @PutMapping("/update/{secretary_id}")
    public ResponseEntity<Secretary> updateSecretary(@PathVariable Integer secretary_id, @RequestBody Secretary updatedSecretary) {
        Optional<Secretary> existingSecretaryOptional = secretaryRepository.findById(secretary_id);

        if(existingSecretaryOptional.isPresent()) {
            Secretary existingSecretary = existingSecretaryOptional.get();

            existingSecretary.setFname(updatedSecretary.getFname());
            existingSecretary.setLname(updatedSecretary.getLname());
            existingSecretary.setAFM(updatedSecretary.getAFM());

            Secretary savedSecretary = secretaryRepository.save(existingSecretary);
            return ResponseEntity.ok(savedSecretary);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
