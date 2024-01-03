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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/secretary/appform")
@Hidden
public class AppFormRestController {

    @Autowired
    private AppFormService appFormService;

    @Autowired
    private AppFormRepository appFormRepository;

    /*@PostConstruct
    public void setup() {
            appFormRepository.save(new AppForm("A", aimodotisDAO.getAimodotis(4)));

    }*/

    @GetMapping("")
    public List<AppForm> getAppForms(){
        return appFormService.getAppForms();
    }

    @GetMapping("{appform_id}")
    public AppForm getAppForm(@PathVariable Integer appform_id){
        return appFormService.getAppForm(appform_id);
    }

//    @PostMapping("{appform_id}/accept")
//    public

    @DeleteMapping("{appform_id}/decline")
    public void deleteAppForm(@PathVariable Integer appform_id){
        appFormService.deleteAppForm(appform_id);
    }


}
