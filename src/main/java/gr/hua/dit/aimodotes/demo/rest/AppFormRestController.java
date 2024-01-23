package gr.hua.dit.aimodotes.demo.rest;

import gr.hua.dit.aimodotes.demo.dao.AimodotisDAO;
import gr.hua.dit.aimodotes.demo.entity.*;
import gr.hua.dit.aimodotes.demo.repository.AimodotisRepository;
import gr.hua.dit.aimodotes.demo.repository.AppFormRepository;
import gr.hua.dit.aimodotes.demo.service.AppFormService;
import gr.hua.dit.aimodotes.demo.service.BloodTestService;
import gr.hua.dit.aimodotes.demo.service.UserDetailsImpl;
import gr.hua.dit.aimodotes.demo.service.UserDetailsServiceImpl;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/appform")
@Hidden
public class AppFormRestController {

    @Autowired
    private AppFormService appFormService;

    @Autowired
    private AimodotisDAO aimodotisDAO;

    @Autowired
    private AimodotisRepository aimodotisRepository;

    @Autowired
    private BloodTestService bloodTestService;

    //user can fill a new appform including his blood test
    @PostMapping("/new")
    @Secured("ROLE_USER")
    public ResponseEntity<String> saveAppform(Authentication authentication, @RequestBody AimodotisAndBloodtest aimodotisAndBloodtest){
        try{
            Aimodotis aimodotis = aimodotisAndBloodtest.getAimodotis();
            BloodTest bloodTest = aimodotisAndBloodtest.getBloodTest();
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            String userEmail = userDetails.getEmail();
            if(aimodotisRepository.findByAMKA(aimodotis.getAMKA()).isPresent()){
                System.out.println("Aimodotis already exists.");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Aimodotis already exists!");
            }else if(aimodotisAndBloodtest.getAimodotis().getEmail().equals(userEmail)){
                AppForm appForm = new AppForm();
                appForm.setAppDate(LocalDate.now());
                appForm.setStatus(AppForm.Status.PENDING);
                appForm.setBloodTest(bloodTest);
                aimodotis.setAppForm(appForm);
                bloodTest.setAppForm(appForm);
                aimodotisDAO.saveAimodotis(aimodotis);
                bloodTestService.saveBloodTest(bloodTest, appForm.getId());
                appFormService.saveAppForm(appForm, aimodotis.getId());
                return ResponseEntity.ok("Application saved successfully!");
            }else{
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("User's email doesnt match with the appform");
            }
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error saving applicaton!");
        }
    }

}
