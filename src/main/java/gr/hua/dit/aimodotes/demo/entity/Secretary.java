package gr.hua.dit.aimodotes.demo.entity;

import jakarta.persistence.*;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.Email;

import java.util.List;

@Entity
public class Secretary {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private Integer id;

    @Column
    private String fname;

    @Column
    private String lname;

    @Column
    private String AFM;

    @Column
    private String email;

    public String getAFM() {
        return AFM;
    }

    public void setAFM(String AFM) {
        this.AFM = AFM;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @OneToMany(mappedBy = "secretary", cascade = {//CascadeType.DETACH,
            CascadeType.REFRESH,
            //CascadeType.PERSIST,
            CascadeType.MERGE})
    private List<AppForm> appForms;

    @OneToMany(mappedBy = "secretary", cascade = {//CascadeType.DETACH,
            CascadeType.REFRESH,
            //CascadeType.PERSIST,
            CascadeType.MERGE})
    private List<DonationRequest> donationRequests;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Secretary(String fname, String lname, String AFM, String email) {
        this.fname = fname;
        this.lname = lname;
        this.AFM = AFM;
        this.email = email;
    }

    public Secretary() {
    }
}
