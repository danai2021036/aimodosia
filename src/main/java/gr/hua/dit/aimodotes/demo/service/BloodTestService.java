package gr.hua.dit.aimodotes.demo.service;

import gr.hua.dit.aimodotes.demo.dao.AimodotisDAO;
import gr.hua.dit.aimodotes.demo.entity.AppForm;
import gr.hua.dit.aimodotes.demo.entity.BloodTest;
import gr.hua.dit.aimodotes.demo.repository.BloodTestRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BloodTestService {

    @Autowired
    private BloodTestRepository bloodTestRepository;

    @Autowired
    private  AppFormService appFormService;

    @Transactional
    public void saveBloodTest(BloodTest bloodTest, Integer appform_id){
        AppForm appForm = appFormService.getAppForm(appform_id);
        bloodTest.setAppForm(appForm);
        bloodTestRepository.save(bloodTest);
    }

    @Transactional
    public BloodTest getBloodTest(Integer appform_id){
        return bloodTestRepository.findByAppForm_Id(appform_id).get();
    }

    public void deleteBloodTest(Integer bloodtest_id){
        bloodTestRepository.deleteById(bloodtest_id);
    }

    @Transactional
    public List<BloodTest> getBloodTests(){
        return bloodTestRepository.findAll();
    }
}
