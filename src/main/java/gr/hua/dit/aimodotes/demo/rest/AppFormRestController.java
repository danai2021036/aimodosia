package gr.hua.dit.aimodotes.demo.rest;

import gr.hua.dit.aimodotes.demo.dao.AimodotisDAO;
import gr.hua.dit.aimodotes.demo.entity.*;
import gr.hua.dit.aimodotes.demo.repository.AimodotisRepository;
import gr.hua.dit.aimodotes.demo.repository.AppFormRepository;
import gr.hua.dit.aimodotes.demo.service.AppFormService;
import gr.hua.dit.aimodotes.demo.service.BloodTestService;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/appform")
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

    @PostMapping("/new")
    @Secured({"ROLE_ADMIN","ROLE_USER"})
    public ResponseEntity<String> saveAppform(@RequestBody AimodotisAndBloodtest aimodotisAndBloodtest){
        try{
            Aimodotis aimodotis = aimodotisAndBloodtest.getAimodotis();
            BloodTest bloodTest = aimodotisAndBloodtest.getBloodTest();
            if(aimodotisRepository.findByAMKA(aimodotis.getAMKA()).isPresent()){
                System.out.println("Aimodotis already exists.");
                return null;
            }else {
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
            }
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error saving applicaton!");
        }
    }

}
