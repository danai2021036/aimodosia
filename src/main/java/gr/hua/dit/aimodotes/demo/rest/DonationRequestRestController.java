package gr.hua.dit.aimodotes.demo.rest;

import gr.hua.dit.aimodotes.demo.dao.AimodotisDAO;
import gr.hua.dit.aimodotes.demo.entity.DonationRequest;
import gr.hua.dit.aimodotes.demo.repository.DonationRequestRepository;
import gr.hua.dit.aimodotes.demo.service.DonationRequestService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/donationrequest")
public class DonationRequestRestController {
    @Autowired
    private DonationRequestRepository donationRequestRepository;

    @Autowired
    private DonationRequestService donationRequestService;


    @PostConstruct
    public void setup() {
        donationRequestRepository.findByLocationAndDate("Athens",LocalDate.parse("2024-01-05")).orElseGet(() -> {
            donationRequestRepository.save(new DonationRequest("Athens", LocalDate.parse("2024-01-05")));
            return null;
        });
    }

    @GetMapping("")
    @Secured({"ROLE_ADMIN","ROLE_SECRETARY"})
    public List<DonationRequest> getDonationRequests(){
        return donationRequestService.getDonationRequests();
    }

    @PostMapping("/new")
    @Secured("ROLE_SECRETARY")
    public DonationRequest saveDonationRequest(@RequestBody DonationRequest donationRequest){
        if(donationRequestRepository.findByLocationAndDate(donationRequest.getLocation(),donationRequest.getDate()).isPresent()){
            System.out.println("Donation Request already exists.");
            return null;
        }else {
            donationRequestService.saveDonationRequest(donationRequest);
            return donationRequest;
        }
    }

    //delete
    @DeleteMapping("/delete/{donation_request_id}")
    @Secured("ROLE_SECRETARY")
    public void deleteDonationRequest(@PathVariable Integer donation_request_id){
        donationRequestService.deleteDonationRequest(donation_request_id);
    }

    //enan
    @GetMapping("{donation_request_id}")
    @Secured("ROLE_SECRETARY")
    public DonationRequest getDonationRequest(@PathVariable Integer donation_request_id){
        return donationRequestService.getDonationRequest(donation_request_id);
    }

}
