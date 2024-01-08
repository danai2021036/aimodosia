package gr.hua.dit.aimodotes.demo.rest;

import gr.hua.dit.aimodotes.demo.dao.AimodotisDAO;
import gr.hua.dit.aimodotes.demo.entity.Aimodotis;
import gr.hua.dit.aimodotes.demo.entity.DonationRequest;
import gr.hua.dit.aimodotes.demo.entity.Role;
import gr.hua.dit.aimodotes.demo.payload.response.MessageResponse;
import gr.hua.dit.aimodotes.demo.repository.AimodotisRepository;
import gr.hua.dit.aimodotes.demo.service.DonationRequestService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/aimodotes")
public class AimodotisRestController {

    @Autowired
    private AimodotisRepository aimodotisRepository;

    @Autowired
    private AimodotisDAO aimodotisDAO;

    @Autowired
    private DonationRequestService donationRequestService;

    @PostConstruct
    public void setup() {
        aimodotisRepository.findByAMKA("15110301262").orElseGet(() -> {
            aimodotisRepository.save(new Aimodotis("Nafsika", "Papaioannou", "naf@hua.gr", "6985762160", "15110301262", 'F', null, 20, "Athens"));
            return null;
        });
        aimodotisRepository.findByAMKA("25110301550").orElseGet(() -> {
            aimodotisRepository.save(new Aimodotis("Giwrgos", "Gkolfinopoulos", "geo@hua.gr", "6980763944", "25110301550", 'M', null, 20, "Athens"));
            return null;
        });
        aimodotisRepository.findByAMKA("13456789068").orElseGet(() -> {
            aimodotisRepository.save(new Aimodotis("Danai", "Kamperou", "dan@hua.gr", "6935546778", "13456789068", 'F', null, 20,"Patra"));
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
    public Aimodotis getAimodotis(@PathVariable Integer aimodotis_id) {
        return aimodotisDAO.getAimodotis(aimodotis_id);
    }

    @PutMapping("/update/{aimodotis_id}")
    public ResponseEntity<Aimodotis> updateAimodotis(@PathVariable Integer aimodotis_id, @RequestBody Aimodotis updatedAimodotis) {
        Optional<Aimodotis> existingAimodotisOptional = aimodotisRepository.findById(aimodotis_id);//findByAMKA(aimodotischeck.getAMKA());

        if(existingAimodotisOptional.isPresent()) {
            Aimodotis existingAimodotis = existingAimodotisOptional.get();

//            try{
//                BeanUtils.copyProperties(updatedAimodotis,existingAimodotis);
//            } catch (Exception e) {
//                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//            }
//            Aimodotis savedAimodotis = aimodotisRepository.save(existingAimodotis);
            existingAimodotis.setFname(updatedAimodotis.getFname());
            existingAimodotis.setLname(updatedAimodotis.getLname());
            existingAimodotis.setEmail(updatedAimodotis.getEmail());
            existingAimodotis.setPhone(updatedAimodotis.getPhone());
            existingAimodotis.setAMKA(updatedAimodotis.getAMKA());
            existingAimodotis.setSex(updatedAimodotis.getSex());
            existingAimodotis.setLast_donation(updatedAimodotis.getLast_donation());
            existingAimodotis.setAge(updatedAimodotis.getAge());
            existingAimodotis.setLocation(updatedAimodotis.getLocation());

            Aimodotis savedAimodotis = aimodotisRepository.save(existingAimodotis);
            return ResponseEntity.ok(savedAimodotis);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //eleghos last aimodosias
    public boolean checkDateLastDon(Aimodotis aimodotis, DonationRequest donationRequest) {
        LocalDate lastDonDate = aimodotis.getLast_donation();
        LocalDate donReqDate = donationRequest.getDate();

        Period period = Period.between(donReqDate, lastDonDate);
        int diff = Math.abs(period.getMonths());

        if(lastDonDate!=null) {
            if (diff < 3) {
                System.out.println("declined");
                return false;
            }
        }

        System.out.println("accepted");
        return true;
    }


    //apodoxi aporripsi mnmtos
    @PostMapping("/donationrequests/{aimodotis_id}/{donation_request_id}/accept")
    public void acceptRequest(@PathVariable Integer aimodotis_id, @PathVariable Integer donation_request_id) {
        Aimodotis aimodotis = aimodotisDAO.getAimodotis(aimodotis_id);
        DonationRequest donationRequest = donationRequestService.getDonationRequest(donation_request_id);
        if(checkDateLastDon(aimodotis, donationRequest)) {
            //edit aimdotis last donation date
            aimodotis.setLast_donation(donationRequest.getDate());
            updateAimodotis(aimodotis_id,aimodotis);
        } else {
            donationRequest.removeAimodotis(aimodotis);
            donationRequestService.saveDonationRequest(donationRequest);
        }
    }

    @DeleteMapping("/donationrequests/{aimodotis_id}/{donation_request_id}/decline")
    public void declineRequest(@PathVariable Integer aimodotis_id, @PathVariable Integer donation_request_id){
        Aimodotis aimodotis = aimodotisDAO.getAimodotis(aimodotis_id);
        DonationRequest donationRequest = donationRequestService.getDonationRequest(donation_request_id);
        donationRequest.removeAimodotis(aimodotis);
        donationRequestService.saveDonationRequest(donationRequest);
    }

    //update gia donationrequest
    //check periptosi i imerominia null
}
