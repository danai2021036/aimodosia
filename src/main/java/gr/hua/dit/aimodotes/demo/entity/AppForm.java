package gr.hua.dit.aimodotes.demo.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class AppForm {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private Integer id;

    @Column
    private String bloodtype;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="blood_test_id")
    private BloodTest bloodTest;

    //anapodi sxesh sto DB ???????????????????????????????????????????????????
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="aimodotis_id")
    private Aimodotis aimodotis;

    public Aimodotis getAimodotis() {
        return aimodotis;
    }

    public void setAimodotis(Aimodotis aimodotis) {
        this.aimodotis = aimodotis;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBloodtype() {
        return bloodtype;
    }

    public void setBloodtype(String bloodtype) {
        this.bloodtype = bloodtype;
    }

    public AppForm(Integer id, String bloodtype, Aimodotis aimodotis) {
        this.id = id;
        this.bloodtype = bloodtype;
    }

    public AppForm() {
    }
}
