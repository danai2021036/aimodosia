package gr.hua.dit.aimodotes.demo.rest;

import gr.hua.dit.aimodotes.demo.dao.AimodotisDAO;
import gr.hua.dit.aimodotes.demo.entity.Aimodotis;
import gr.hua.dit.aimodotes.demo.entity.AppForm;
import gr.hua.dit.aimodotes.demo.entity.Secretary;
import gr.hua.dit.aimodotes.demo.repository.AimodotisRepository;
import gr.hua.dit.aimodotes.demo.repository.AppFormRepository;
import gr.hua.dit.aimodotes.demo.service.AppFormService;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/appform")
@Hidden
public class AppFormRestController {

    @Autowired
    private AppFormService appFormService;

    @Autowired
    private AppFormRepository appFormRepository;

    @Autowired
    private AimodotisDAO aimodotisDAO;

    @Autowired
    private AimodotisRepository aimodotisRepository;

    @PostMapping("/new")
    public ResponseEntity<String> saveAppform(@RequestBody Aimodotis aimodotis){
        try{
            if(aimodotisRepository.findByAMKA(aimodotis.getAMKA()).isPresent()){
                System.out.println("Aimodotis already exists.");
                return null;
            }else {
                AppForm appForm = new AppForm();
                appForm.setAppDate(LocalDate.now());
                appForm.setStatus(AppForm.Status.PENDING);
                aimodotis.setAppForm(appForm);
                aimodotisDAO.saveAimodotis(aimodotis);
                appFormService.saveAppForm(appForm, aimodotis.getId());
                return ResponseEntity.ok("Application saved successfully!");
            }
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error saving applicaton!");
        }
    }

}
