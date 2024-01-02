package gr.hua.dit.aimodotes.demo.rest;

import gr.hua.dit.aimodotes.demo.dao.AimodotisDAO;
import gr.hua.dit.aimodotes.demo.entity.Aimodotis;
import gr.hua.dit.aimodotes.demo.entity.AppForm;
import gr.hua.dit.aimodotes.demo.repository.AimodotisRepository;
import gr.hua.dit.aimodotes.demo.repository.AppFormRepository;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/appform")
@Hidden
public class AppFormRestController {

    @Autowired
    private AimodotisDAO aimodotisDAO;

    @Autowired
    private AppFormRepository appFormRepository;

    /*@PostConstruct
    public void setup() {
            appFormRepository.save(new AppForm("A", aimodotisDAO.getAimodotis(4)));

    }*/
}
