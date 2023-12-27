package gr.hua.dit.aimodotes.demo.service;

import gr.hua.dit.aimodotes.demo.dao.AimodotisDAO;
import gr.hua.dit.aimodotes.demo.entity.Aimodotis;
import gr.hua.dit.aimodotes.demo.entity.DonationRequest;
import gr.hua.dit.aimodotes.demo.repository.DonationRequestRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DonationRequestService {
    @Autowired
    private AimodotisDAO aimodotisDAO;

    @Autowired
    private DonationRequestRepository donationRequestRepository;

    @Transactional
    public List<DonationRequest> getDonationRequests(){
        return donationRequestRepository.findAll();
    }

    @Transactional
    public void saveDonationRequest(DonationRequest donationRequest){
        donationRequestRepository.save(donationRequest);
    }

    @Transactional
    public void deleteDonationRequest(Integer donationrequest_id){
        donationRequestRepository.deleteById(donationrequest_id);
    }

    @Transactional
    public DonationRequest getDonationRequest(Integer donationrequest_id){
        return donationRequestRepository.findById(donationrequest_id).get();
    }

    public List<DonationRequest> getAimodotisDonationBloodRequests(Integer aimodotis_id){
        Aimodotis aimodotis = aimodotisDAO.getAimodotis(aimodotis_id);
        return aimodotis.getDonationRequests();
    }
}
