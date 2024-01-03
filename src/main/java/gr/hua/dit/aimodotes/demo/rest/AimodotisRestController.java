package gr.hua.dit.aimodotes.demo.rest;

import gr.hua.dit.aimodotes.demo.dao.AimodotisDAO;
import gr.hua.dit.aimodotes.demo.entity.Aimodotis;
import gr.hua.dit.aimodotes.demo.entity.Role;
import gr.hua.dit.aimodotes.demo.payload.response.MessageResponse;
import gr.hua.dit.aimodotes.demo.repository.AimodotisRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/aimodotes")
public class AimodotisRestController {

    @Autowired
    private AimodotisRepository aimodotisRepository;

    @Autowired
    private AimodotisDAO aimodotisDAO;

    @PostConstruct
    public void setup() {
        aimodotisRepository.findByAMKA("15110301262").orElseGet(() -> {
            aimodotisRepository.save(new Aimodotis("Nafsika", "Papaioannou", "naf@hua.gr", "6985762160", "15110301262", 'F', null, 20));
            return null;
        });
        aimodotisRepository.findByAMKA("25110301550").orElseGet(() -> {
            aimodotisRepository.save(new Aimodotis("Giwrgos", "Gkolfinopoulos", "geo@hua.gr", "6980763944", "25110301550", 'M', null, 20));
            return null;
        });
        aimodotisRepository.findByAMKA("13456789068").orElseGet(() -> {
            aimodotisRepository.save(new Aimodotis("Danai", "Kamperou", "dan@hua.gr", "6935546778", "13456789068", 'F', null, 20));
            return null;
        });
    }

    //@Secured({"ROLE_SECRETARY", "ROLE_ADMIN"})

    @GetMapping("")
    public List<Aimodotis> getAimodotes(){
        return aimodotisDAO.getAimodotes();
    }

    @PostMapping("/new")
    public Aimodotis saveAimodotis(@RequestBody Aimodotis aimodotis){
        if(aimodotisRepository.findByAMKA(aimodotis.getAMKA()).isPresent()){
            System.out.println("Aimodotis already exists.");
            return null;
        }else {
            return aimodotisDAO.saveAimodotis(aimodotis);
        }
    }

    //delete
    @DeleteMapping("/delete/{aimodotis_id}")
    public void deleteAimodotis(@PathVariable Integer aimodotis_id){
        aimodotisDAO.deleteAimodotis(aimodotis_id);
    }

    //enan
    @GetMapping("{aimodotis_id}")
    public Aimodotis getAimodotis(@PathVariable Integer aimodotis_id){
        return aimodotisDAO.getAimodotis(aimodotis_id);
    }
    //edit
//    @PostMapping("/update/{aimodotis_id}")
//    public Aimodotis updateAimodotis(@RequestBody Aimodotis aimodotis){
//        if(aimodotisRepository.findByAMKA(aimodotis.getAMKA()).isPresent()){
//            return aimodotisDAO.saveAimodotis(aimodotis);
//        }else {
//            System.out.println("Aimodotis doesnt exist.");
//            return null;
//        }
//    }
}
