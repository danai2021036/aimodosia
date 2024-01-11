package gr.hua.dit.aimodotes.demo.rest;

import gr.hua.dit.aimodotes.demo.dao.AimodotisDAO;
import gr.hua.dit.aimodotes.demo.entity.*;
import gr.hua.dit.aimodotes.demo.repository.AimodotisRepository;
import gr.hua.dit.aimodotes.demo.repository.RoleRepository;
import gr.hua.dit.aimodotes.demo.repository.UserRepository;
import gr.hua.dit.aimodotes.demo.service.AppFormService;
import gr.hua.dit.aimodotes.demo.service.BloodTestService;
import gr.hua.dit.aimodotes.demo.service.DonationRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.Period;
import java.util.*;

@RestController
@RequestMapping("/api/aimodotes")
public class AimodotisRestController {

    @Autowired
    private AimodotisRepository aimodotisRepository;

    @Autowired
    private AimodotisDAO aimodotisDAO;

    @Autowired
    private DonationRequestService donationRequestService;

    @Autowired
    private AppFormService appFormService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

//    @PostConstruct
//    public void setup() {
//        aimodotisRepository.findByAMKA("15110301262").orElseGet(() -> {
//            aimodotisRepository.save(new Aimodotis("Nafsika", "Papaioannou", "naf@hua.gr", "6985762160", "15110301262", 'F', null, 20, "Athens"));
//            return null;
//        });
//        aimodotisRepository.findByAMKA("25110301550").orElseGet(() -> {
//            aimodotisRepository.save(new Aimodotis("Giwrgos", "Gkolfinopoulos", "geo@hua.gr", "6980763944", "25110301550", 'M', null, 20, "Athens"));
//            return null;
//        });
//        aimodotisRepository.findByAMKA("13456789068").orElseGet(() -> {
//            aimodotisRepository.save(new Aimodotis("Danai", "Kamperou", "dan@hua.gr", "6935546778", "13456789068", 'F', null, 20, "Patra"));
//            return null;
//        });
//    }


    @GetMapping("")
    @Secured({"ROLE_ADMIN","ROLE_SECRETARY"})
    public List<Aimodotis> getAimodotes(){
        return aimodotisDAO.getAimodotes();
    }

    @PostMapping("/new")
    @Secured("ROLE_ADMIN")
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
    @Secured("ROLE_ADMIN")
    public void deleteAimodotis(@PathVariable Integer aimodotis_id){
        Aimodotis aimodotis = aimodotisDAO.getAimodotis(aimodotis_id);
        AppForm appForm = aimodotis.getAppForm();
        appFormService.deleteAppForm(appForm.getId());
        aimodotisDAO.deleteAimodotis(aimodotis_id);

    }

    //enan
    @GetMapping("{aimodotis_id}")
    @Secured({"ROLE_ADMIN","ROLE_SECRETARY"})
    public Aimodotis getAimodotis(@PathVariable Integer aimodotis_id) {
        return aimodotisDAO.getAimodotis(aimodotis_id);
    }



    @PutMapping("/update/{aimodotis_id}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<Aimodotis> updateAimodotis(@PathVariable Integer aimodotis_id, @RequestBody Aimodotis updatedAimodotis) {
        Optional<Aimodotis> existingAimodotisOptional = aimodotisRepository.findById(aimodotis_id);

        if(existingAimodotisOptional.isPresent()) {
            Aimodotis existingAimodotis = existingAimodotisOptional.get();

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

    @GetMapping("/donationrequests/{aimodotis_id}")
    @ResponseBody
    @Secured("ROLE_AIMODOTIS")
    public List<DonationRequest> getAvailableDonations(@PathVariable Integer aimodotis_id){
        Aimodotis aimodotis = aimodotisDAO.getAimodotis(aimodotis_id);
        List<DonationRequest> donationRequests = donationRequestService.getDonationRequests();
        List<DonationRequest> availableDonationRequests = new ArrayList<>();
        for (int i=0; i<donationRequests.size(); i++){
            if (checkDateLastDon(aimodotis,donationRequests.get(i)) && aimodotis.getLocation().equals(donationRequests.get(i).getLocation())){
               availableDonationRequests.add(donationRequests.get(i));
            }
        }
        System.out.println(availableDonationRequests);
        return availableDonationRequests;
    }

    //eleghos last aimodosias
    public boolean checkDateLastDon(Aimodotis aimodotis, DonationRequest donationRequest) {
        LocalDate lastDonDate = aimodotis.getLast_donation();
        LocalDate donReqDate = donationRequest.getDate();


        if(lastDonDate==null) {
            if(donReqDate.isAfter(LocalDate.now())) {
                System.out.println("accepted");
                return true;
            }else{
                return false;
            }

        }
        Period period = Period.between(donReqDate, lastDonDate);
        int diff = Math.abs(period.getMonths());
        if ((diff < 3 || donReqDate.isBefore(lastDonDate)) && donReqDate.isBefore(LocalDate.now())) {
            System.out.println("declined");
            return false;
        }
        return true;
    }

    @PostMapping("/donationrequests/{aimodotis_id}/{donation_request_id}/accept")
    @Secured("ROLE_AIMODOTIS")
    public void acceptRequest(@PathVariable Integer aimodotis_id, @PathVariable Integer donation_request_id) {
        Aimodotis aimodotis = aimodotisDAO.getAimodotis(aimodotis_id);
        DonationRequest donationRequest = donationRequestService.getDonationRequest(donation_request_id);

            //edit aimdotis last donation date
            aimodotis.setLast_donation(donationRequest.getDate());
            updateAimodotis(aimodotis_id,aimodotis);
            donationRequest.addAimodotis(aimodotis);
            donationRequestService.saveDonationRequest(donationRequest);
    }

    @PostMapping("/confirmcontactinfo/{aimodotis_id}")
    @Secured("ROLE_USER")
    public ResponseEntity<String> confirmContactInfo(@PathVariable Integer aimodotis_id) {
        Aimodotis aimodotis = aimodotisRepository.findById(aimodotis_id).get();
        if (userRepository.findByEmail(aimodotis.getEmail()).isPresent()) {
            User user = userRepository.findByEmail(aimodotis.getEmail()).get();
            Set<Role> roles = user.getRoles();
            roles.add(this.roleRepository.findByName("ROLE_AIMODOTIS").orElseThrow());
            user.setRoles(roles);
            userRepository.save(user);
            return ResponseEntity.ok("You are now a Blood Donator!");
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("User not found!");
    }
}
