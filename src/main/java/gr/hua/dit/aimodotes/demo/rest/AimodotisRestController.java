package gr.hua.dit.aimodotes.demo.rest;

import gr.hua.dit.aimodotes.demo.dao.AimodotisDAO;
import gr.hua.dit.aimodotes.demo.entity.*;
import gr.hua.dit.aimodotes.demo.repository.*;
import gr.hua.dit.aimodotes.demo.service.AppFormService;
import gr.hua.dit.aimodotes.demo.service.BloodTestService;
import gr.hua.dit.aimodotes.demo.service.DonationRequestService;
import jakarta.annotation.PostConstruct;
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

    @Autowired
    private AppFormRepository appFormRepository;

    @Autowired
    private BloodTestRepository bloodTestRepository;

    @Autowired
    private SecretaryRepository secretaryRepository;

    //setup blood donors
    @PostConstruct
    public void setup() {
        secretaryRepository.findByAFM("123456789").orElseGet(() -> {
            secretaryRepository.save(new Secretary("Maria", "Papa","123456789", "sec@gmail.gr"));
            return null;
        });
        aimodotisRepository.findByAMKA("05110301111").orElseGet(() -> {
            Aimodotis aimodotis = aimodotisRepository.save(new Aimodotis("Nafsika", "Papaioannou", "naf@gmail.gr", "6985762160", "05110301111", 'F', LocalDate.parse("2024-01-11"), 20, "Athens"));
            AppForm appForm = appFormRepository.save(new AppForm(AppForm.Status.ACCEPTED,LocalDate.parse("2024-01-10")));
            BloodTest bloodTest = bloodTestRepository.save(new BloodTest(LocalDate.parse("2023-11-25"), "details","0+"));
            appForm.setAimodotis(aimodotis);
            appForm.setBloodTest(bloodTest);
            appForm.setSecretary(secretaryRepository.findByAFM("123456789").get());
            aimodotis.setAppForm(appForm);
            bloodTest.setAppForm(appForm);
            bloodTestRepository.save(bloodTest);
            aimodotisDAO.saveAimodotis(aimodotis);
            appFormRepository.save(appForm);
            return null;
        });
        aimodotisRepository.findByAMKA("25110301550").orElseGet(() -> {
            Aimodotis aimodotis = aimodotisRepository.save(new Aimodotis("Giwrgos", "Gkolfinopoulos", "geo@gmail.gr", "6980763944", "25110301550", 'M', LocalDate.parse("2023-12-01"), 20, "Athens"));
            AppForm appForm = appFormRepository.save(new AppForm(AppForm.Status.ACCEPTED,LocalDate.parse("2024-11-28")));
            BloodTest bloodTest = bloodTestRepository.save(new BloodTest(LocalDate.parse("2023-10-30"), "details","A+"));
            appForm.setAimodotis(aimodotis);
            appForm.setBloodTest(bloodTest);
            appForm.setSecretary(secretaryRepository.findByAFM("123456789").get());
            aimodotis.setAppForm(appForm);
            bloodTest.setAppForm(appForm);
            bloodTestRepository.save(bloodTest);
            aimodotisDAO.saveAimodotis(aimodotis);
            appFormRepository.save(appForm);
            return null;
        });
        aimodotisRepository.findByAMKA("13456789068").orElseGet(() -> {
            Aimodotis aimodotis = aimodotisRepository.save(new Aimodotis("Danai", "Kamperou", "dan@gmail.gr", "6935546778", "13456789068", 'F', null, 20, "Patra"));
            AppForm appForm = appFormRepository.save(new AppForm(AppForm.Status.PENDING,LocalDate.parse("2024-01-09")));
            BloodTest bloodTest = bloodTestRepository.save(new BloodTest(LocalDate.parse("2023-12-22"), "details","B+"));
            appForm.setAimodotis(aimodotis);
            appForm.setBloodTest(bloodTest);
            appForm.setSecretary(secretaryRepository.findByAFM("123456789").get());
            aimodotis.setAppForm(appForm);
            bloodTest.setAppForm(appForm);
            bloodTestRepository.save(bloodTest);
            aimodotisDAO.saveAimodotis(aimodotis);
            appFormRepository.save(appForm);
            return null;
        });
    }

    //admin and secretary can see all the blood donors
    @GetMapping("")
    @Secured({"ROLE_ADMIN","ROLE_SECRETARY"})
    public List<Aimodotis> getAimodotes(){
        return aimodotisDAO.getAimodotes();
    }

    //admin can save one blood donor
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

    //admin can delete one blood donor
    @DeleteMapping("/delete/{aimodotis_id}")
    @Secured("ROLE_ADMIN")
    public void deleteAimodotis(@PathVariable Integer aimodotis_id){
        Aimodotis aimodotis = aimodotisDAO.getAimodotis(aimodotis_id);
        AppForm appForm = aimodotis.getAppForm();
        appFormService.deleteAppForm(appForm.getId());
        aimodotisDAO.deleteAimodotis(aimodotis_id);

    }

    //admin and secretary can see one blood donor
    @GetMapping("{aimodotis_id}")
    @Secured({"ROLE_ADMIN","ROLE_SECRETARY"})
    public Aimodotis getAimodotis(@PathVariable Integer aimodotis_id) {
        return aimodotisDAO.getAimodotis(aimodotis_id);
    }


    //admin can update one blood donor's details
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

    //blood donor can see all the available donation request where he follows the requirements
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

    //a method that checks if you can participate in a blood donation based on the last time you participated in one
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

    //blood donor can accept a blood donation request and his last donation date gets updated
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

    //blood donor can confirm his contact info after they have accepted his application and he gets the blood donors role and he is able to participate in blood donations
    @PostMapping("/confirmcontactinfo/{aimodotis_id}")
    @Secured("ROLE_USER")
    public ResponseEntity<String> confirmContactInfo(@PathVariable Integer aimodotis_id) {
        Aimodotis aimodotis = aimodotisRepository.findById(aimodotis_id).get();
        String email = (String) aimodotis.getEmail();
        AppForm appForm = appFormRepository.findByAimodotis(aimodotis).get();
        if (userRepository.findByEmail(email).isPresent() && appForm.getStatus().equals(AppForm.Status.ACCEPTED)) {
            User user = userRepository.findByEmail(email).get();
            Set<Role> roles = user.getRoles();
            roles.add(this.roleRepository.findByName("ROLE_AIMODOTIS").orElseThrow());
            user.setRoles(roles);
            userRepository.save(user);
            return ResponseEntity.ok("You are now a Blood Donator!");
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("User not found!");
    }
}
