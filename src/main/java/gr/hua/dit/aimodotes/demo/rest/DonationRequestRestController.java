package gr.hua.dit.aimodotes.demo.rest;

import gr.hua.dit.aimodotes.demo.repository.DonationRequestRepository;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/donationrequest")
@Hidden
public class DonationRequestRestController {
    private DonationRequestRepository donationRequestRepository;
}
