package gr.hua.dit.aimodotes.demo.service;

import gr.hua.dit.aimodotes.demo.dao.AimodotisDAO;
import gr.hua.dit.aimodotes.demo.entity.Aimodotis;
import gr.hua.dit.aimodotes.demo.entity.BloodTest;
import gr.hua.dit.aimodotes.demo.repository.BloodTestRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BloodTestService {

    @Autowired
    private AimodotisDAO aimodotisDAO;

    @Autowired
    private BloodTestRepository bloodTestRepository;

    @Transactional
    public void saveBloodTest(BloodTest bloodTest, Integer aimodotis_id){
        Aimodotis aimodotis = aimodotisDAO.getAimodotis(aimodotis_id);
        bloodTest.setAimodotis(aimodotis);
        bloodTestRepository.save(bloodTest);
    }

    @Transactional
    public BloodTest getBloodTest(Integer bloodtest_id){
        return bloodTestRepository.findById(bloodtest_id).get();
    }

    public void deleteBloodTest(Integer bloodtest_id){
        bloodTestRepository.deleteById(bloodtest_id);
    }

}
