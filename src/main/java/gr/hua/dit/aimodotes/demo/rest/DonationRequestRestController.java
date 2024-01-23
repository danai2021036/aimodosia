package gr.hua.dit.aimodotes.demo.rest;

import gr.hua.dit.aimodotes.demo.dao.AimodotisDAO;
import gr.hua.dit.aimodotes.demo.entity.DonationRequest;
import gr.hua.dit.aimodotes.demo.repository.DonationRequestRepository;
import gr.hua.dit.aimodotes.demo.repository.SecretaryRepository;
import gr.hua.dit.aimodotes.demo.service.DonationRequestService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/donationrequest")
public class DonationRequestRestController {
    @Autowired
    private DonationRequestRepository donationRequestRepository;

    @Autowired
    private DonationRequestService donationRequestService;

    @Autowired
    private SecretaryRepository secretaryRepository;

    //setup donation requests
    @PostConstruct
    public void setup() {
        donationRequestRepository.findByLocationAndDate("Athens",LocalDate.parse("2024-04-05")).orElseGet(() -> {
            DonationRequest donationRequest = donationRequestRepository.save(new DonationRequest("Athens", LocalDate.parse("2024-04-05")));
            donationRequest.setSecretary(secretaryRepository.findByAFM("123456789").get());
            donationRequestService.saveDonationRequest(donationRequest);
            return null;
        });
        donationRequestRepository.findByLocationAndDate("Patra",LocalDate.parse("2024-06-05")).orElseGet(() -> {
            DonationRequest donationRequest = donationRequestRepository.save(new DonationRequest("Patra", LocalDate.parse("2024-06-05")));
            donationRequest.setSecretary(secretaryRepository.findByAFM("123456789").get());
            donationRequestService.saveDonationRequest(donationRequest);
            return null;
        });
    }

    //admin and secretary can see all the donation requests
    @GetMapping("")
    @Secured({"ROLE_ADMIN","ROLE_SECRETARY"})
    public List<DonationRequest> getDonationRequests(){
        return donationRequestService.getDonationRequests();
    }

    //secretary can create a new donation request based on the location and the date
    @PostMapping("/{secretary_id}/new")
    @Secured("ROLE_SECRETARY")
    public DonationRequest saveDonationRequest(@PathVariable Integer secretary_id, @RequestBody DonationRequest donationRequest){
        if(donationRequestRepository.findByLocationAndDate(donationRequest.getLocation(),donationRequest.getDate()).isPresent()){
            System.out.println("Donation Request already exists.");
            return null;
        }else {
            donationRequest.setSecretary(secretaryRepository.findById(secretary_id).get());
            donationRequestService.saveDonationRequest(donationRequest);
            return donationRequest;
        }
    }

    //secretary can delete one donation request
    @DeleteMapping("/delete/{donation_request_id}")
    @Secured("ROLE_SECRETARY")
    public void deleteDonationRequest(@PathVariable Integer donation_request_id){
        donationRequestService.deleteDonationRequest(donation_request_id);
    }

    //secretary can see one donation request
    @GetMapping("{donation_request_id}")
    @Secured("ROLE_SECRETARY")
    public DonationRequest getDonationRequest(@PathVariable Integer donation_request_id){
        return donationRequestService.getDonationRequest(donation_request_id);
    }

}
