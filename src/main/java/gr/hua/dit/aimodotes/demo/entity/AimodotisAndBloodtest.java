package gr.hua.dit.aimodotes.demo.entity;

public class AimodotisAndBloodtest {
    private Aimodotis aimodotis;
    private BloodTest bloodTest;

    public Aimodotis getAimodotis() {
        return aimodotis;
    }

    public void setAimodotis(Aimodotis aimodotis) {
        this.aimodotis = aimodotis;
    }

    public BloodTest getBloodTest() {
        return bloodTest;
    }

    public void setBloodTest(BloodTest bloodTest) {
        this.bloodTest = bloodTest;
    }

    public AimodotisAndBloodtest(Aimodotis aimodotis, BloodTest bloodTest) {
        this.aimodotis = aimodotis;
        this.bloodTest = bloodTest;
    }

    public AimodotisAndBloodtest() {
    }
}
