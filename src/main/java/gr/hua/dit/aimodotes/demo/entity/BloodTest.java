package gr.hua.dit.aimodotes.demo.entity;

import jakarta.persistence.*;

import java.util.Date;
@Entity
public class BloodTest {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private Integer id;

    @Column
    private Date date;

    @Column
    private String details;

    //anapodh sxesh DB??????????????????????????????????????????????
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public BloodTest(Integer id, Date date, String details, Aimodotis aimodotis) {
        this.id = id;
        this.date = date;
        this.details = details;
    }

    public BloodTest() {
    }
}
