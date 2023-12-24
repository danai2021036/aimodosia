package gr.hua.dit.aimodotes.demo.entity;

import jakarta.persistence.*;
import jakarta.persistence.Entity;

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

    public Secretary(Integer id, String fname, String lname) {
        this.id = id;
        this.fname = fname;
        this.lname = lname;
    }

    public Secretary() {
    }
}
