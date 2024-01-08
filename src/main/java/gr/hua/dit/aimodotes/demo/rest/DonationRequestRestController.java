package gr.hua.dit.aimodotes.demo.rest;

import gr.hua.dit.aimodotes.demo.dao.AimodotisDAO;
import org.springframework.core.annotation.MergedAnnotations;
import org.springframework.http.ResponseEntity;
import gr.hua.dit.aimodotes.demo.entity.Aimodotis;
import gr.hua.dit.aimodotes.demo.entity.DonationRequest;
import gr.hua.dit.aimodotes.demo.payload.response.MessageResponse;
import gr.hua.dit.aimodotes.demo.repository.DonationRequestRepository;
import gr.hua.dit.aimodotes.demo.service.DonationRequestService;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @Autowired
    private AimodotisDAO aimodotisDAO;

    @PostConstruct
    public void setup() {
        donationRequestRepository.findByLocationAndDate("Athens",LocalDate.parse("2024-01-05")).orElseGet(() -> {
            donationRequestRepository.save(new DonationRequest("Athens", LocalDate.parse("2024-01-05")));
            return null;
        });
    }

    @GetMapping("")
    public List<DonationRequest> getDonationRequests(){
        return donationRequestService.getDonationRequests();
    }

    @PostMapping("/new")
    public DonationRequest saveDonationRequest(@RequestBody DonationRequest donationRequest){
        if(donationRequestRepository.findByLocationAndDate(donationRequest.getLocation(),donationRequest.getDate()).isPresent()){
            System.out.println("Donation Request already exists.");
            return null;
        }else {
            donationRequestService.saveDonationRequest(donationRequest);
            searchAimodotesForRequest(donationRequest);
            return donationRequest;
        }
    }

    //delete
    @DeleteMapping("/delete/{donation_request_id}")
    public void deleteDonationRequest(@PathVariable Integer donation_request_id){
        donationRequestService.deleteDonationRequest(donation_request_id);
    }

    //enan
    @GetMapping("{donation_request_id}")
    public DonationRequest getDonationRequest(@PathVariable Integer donation_request_id){
        return donationRequestService.getDonationRequest(donation_request_id);
    }

    //mhnimta
    @PostMapping("aimodotes/{donation_request_id}/{aimodotis_id}")
    @ResponseBody
    private String sendDonationRequest(@PathVariable Integer donation_request_id, @PathVariable Integer aimodotis_id){
        Aimodotis aimodotis = aimodotisDAO.getAimodotis(aimodotis_id);
        DonationRequest donationRequest = donationRequestService.getDonationRequest(donation_request_id);
        donationRequest.addAimodotis(aimodotis);
        donationRequestService.saveDonationRequest(donationRequest);
        String message = aimodotis.getFname()+ "New Donation Request! Location: " + donationRequest.getLocation() + " Date: " + donationRequest.getDate();
        return message;
    }

    //search gramateias gia kapoio request
    public void searchAimodotesForRequest(DonationRequest donationRequest){
        List<Aimodotis> aimodotes = aimodotisDAO.getAimodotes();
        String reqlocation = donationRequest.getLocation();
        Integer i = 0;
        for (Aimodotis aimodotis: aimodotes) {
            if (aimodotis.getLocation()==reqlocation){
                sendDonationRequest(donationRequest.getId(),aimodotis.getId());
                i=1;
            }
        }
        if(i==0){
            System.out.println("No matches found for Donation Request: " + donationRequest.getId());
        }

    }


}
