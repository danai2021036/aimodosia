package gr.hua.dit.aimodotes.demo.rest;

import gr.hua.dit.aimodotes.demo.dao.AimodotisDAO;
import gr.hua.dit.aimodotes.demo.dao.SecretaryDAO;
import gr.hua.dit.aimodotes.demo.entity.Aimodotis;
import gr.hua.dit.aimodotes.demo.entity.AppForm;
import gr.hua.dit.aimodotes.demo.entity.DonationRequest;
import gr.hua.dit.aimodotes.demo.entity.Secretary;
import gr.hua.dit.aimodotes.demo.repository.SecretaryRepository;
import gr.hua.dit.aimodotes.demo.service.AppFormService;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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

    @Autowired
    private AppFormService appFormService;

    @Autowired
    private AimodotisDAO aimodotisDAO;

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

    @GetMapping("/appforms")
    public List<AppForm> getAppForms(){
        return appFormService.getAppForms();
    }

    @GetMapping("/appform/{appform_id}")
    public AppForm getAppForm(@PathVariable Integer appform_id){
        return appFormService.getAppForm(appform_id);
    }

    //list with appforms that are pending
    @GetMapping("/appform/pending")
    public List<AppForm> getPendingAppForms(){
        List<AppForm> pendingAppForms = new ArrayList<>();
        List<AppForm> appForms = appFormService.getAppForms();
        for(int i=0; i<appForms.size(); i++){
            if(appForms.get(i).getStatus().equals(AppForm.Status.PENDING)){
                pendingAppForms.add(appForms.get(i));
            }
        }
        return pendingAppForms;
    }

    @PostMapping("/appform/pending/{appform_id}/accept")
    public ResponseEntity<String> acceptAppForm(@PathVariable Integer appform_id){
        try{
            AppForm appForm = appFormService.getAppForm(appform_id);
            Aimodotis aimodotis = appForm.getAimodotis();

            appForm.setStatus(AppForm.Status.ACCEPTED);
            appFormService.saveAppForm(appForm, aimodotis.getId());
            return ResponseEntity.ok("Application accepted!");
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error accepting applicaton!");
        }
    }

    @PostMapping("/appform/pending/{appform_id}/decline")
    public ResponseEntity<String> declineAppForm(@PathVariable Integer appform_id){
        try{
            AppForm appForm = appFormService.getAppForm(appform_id);
            Aimodotis aimodotis = appForm.getAimodotis();
            appFormService.deleteAppForm(appform_id);
            return ResponseEntity.ok("Application Form declined and Blood Donator deleted!");
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error declining applicaton!");
        }
    }

}
