package gr.hua.dit.aimodotes.demo.rest;

import gr.hua.dit.aimodotes.demo.dao.AimodotisDAO;
import gr.hua.dit.aimodotes.demo.dao.SecretaryDAO;
import gr.hua.dit.aimodotes.demo.entity.*;
import gr.hua.dit.aimodotes.demo.repository.AimodotisRepository;
import gr.hua.dit.aimodotes.demo.repository.RoleRepository;
import gr.hua.dit.aimodotes.demo.repository.SecretaryRepository;
import gr.hua.dit.aimodotes.demo.repository.UserRepository;
import gr.hua.dit.aimodotes.demo.service.AppFormService;
import gr.hua.dit.aimodotes.demo.service.BloodTestService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/secretary")
public class SecretaryRestController {
    @Autowired
    private SecretaryRepository secretaryRepository;

    @Autowired
    private SecretaryDAO secretaryDAO;

    @Autowired
    private AppFormService appFormService;

    //for some reason it doesn't link this secretary with the user we created on the setup on the AuthController
//    @PostConstruct
//    public void setup() {
//        secretaryRepository.findByAFM("123456789").orElseGet(() -> {
//            secretaryRepository.save(new Secretary("Maria", "Papa","123456789", "sec@gmail.gr"));
//            return null;
//        });
//    }


    //admin can see all the secretaries
    @GetMapping("")
    @Secured("ROLE_ADMIN")
    public List<Secretary> getSecretaries(){
        return secretaryDAO.getSecretaries();
    }

    //admin can save a new secretary
    @PostMapping("/new")
    @Secured("ROLE_ADMIN")
    public Secretary saveSecretary(@RequestBody Secretary secretary){
        if(secretaryRepository.findByAFM(secretary.getAFM()).isPresent()){
            System.out.println("Secretary already exists.");
            return null;
        }else {
            return secretaryDAO.saveSecretary(secretary);
        }
    }

    //admin can delete a secretary
    @DeleteMapping("/delete/{secretary_id}")
    @Secured("ROLE_ADMIN")
    public void deleteSecretary(@PathVariable Integer secretary_id){
        secretaryDAO.deleteSecretary(secretary_id);
    }

    //admin can see one secretary
    @GetMapping("{secretary_id}")
    @Secured("ROLE_ADMIN")
    public Secretary getSecretary(@PathVariable Integer secretary_id){
        return secretaryDAO.getSecretary(secretary_id);
    }

    //secretary can update their info
    @PutMapping("/update/{secretary_id}")
    @Secured("ROLE_SECRETARY")
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

    //secretary can see all the appforms
    @GetMapping("/appforms")
    @Secured("ROLE_SECRETARY")
    public List<AppForm> getAppForms(){
        return appFormService.getAppForms();
    }

    //secretary can see one appform
    @GetMapping("/appform/{appform_id}")
    @Secured("ROLE_SECRETARY")
    public AppForm getAppForm(@PathVariable Integer appform_id){
        return appFormService.getAppForm(appform_id);
    }


    //secretary can see the list with appforms that are pending
    @GetMapping("/appforms/pending")
    @Secured("ROLE_SECRETARY")
    @ResponseBody
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

    //secretary can accept one appform awaiting for confirmation of contact info so he can get the blood donor's role
    @PostMapping("/{secretary_id}/appform/pending/{appform_id}/accept")
    @Secured("ROLE_SECRETARY")
    public ResponseEntity<String> acceptAppForm(@PathVariable Integer secretary_id,@PathVariable Integer appform_id){
        try{
            AppForm appForm = appFormService.getAppForm(appform_id);
            Aimodotis aimodotis = appForm.getAimodotis();

            appForm.setStatus(AppForm.Status.ACCEPTED);
            appForm.setSecretary(secretaryRepository.findById(secretary_id).get());
            appFormService.saveAppForm(appForm, aimodotis.getId());
            return ResponseEntity.ok("Application accepted! Waiting for confirmation of contact details!");
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error accepting applicaton!");
        }
    }

    //secretary can decline one appform
    @PostMapping("/appform/pending/{appform_id}/decline")
    @Secured("ROLE_SECRETARY")
    public ResponseEntity<String> declineAppForm(@PathVariable Integer appform_id){
        try{
            appFormService.deleteAppForm(appform_id);
            return ResponseEntity.ok("Application Form declined and Blood Donator deleted!");
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error declining applicaton!");
        }
    }

}
