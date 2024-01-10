package gr.hua.dit.aimodotes.demo.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
public class AppForm {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private Integer id;

    public enum Status {ACCEPTED, PENDING};
    @Column
    private Status status;

    @Column
    private LocalDate appDate;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="blood_test_id")
    private BloodTest bloodTest;

    //anapodi sxesh sto DB ???????????????????????????????????????????????????
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="aimodotis_id")
    private Aimodotis aimodotis;

    @ManyToOne(cascade = {//CascadeType.DETACH,
            CascadeType.REFRESH,
            //CascadeType.PERSIST,
            CascadeType.MERGE})
    @JoinColumn(name = "secretary_id")
    private Secretary secretary;

    public Aimodotis getAimodotis() {
        return aimodotis;
    }

    public void setAimodotis(Aimodotis aimodotis) {
        this.aimodotis = aimodotis;
    }

    public Integer getId() {
        return id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDate getAppDate() {
        return appDate;
    }

    public void setAppDate(LocalDate appDate) {
        this.appDate = appDate;
    }

    public AppForm(Integer id, Status status, LocalDate appDate) {
        this.id = id;
        this.status = status;
        this.appDate = appDate;
    }

    public AppForm() {
    }
}
