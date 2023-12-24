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

    @OneToMany(mappedBy = "secretary", cascade = CascadeType.ALL)
    private List<AppForm> appForms;

    @OneToMany(mappedBy = "secretary", cascade = CascadeType.ALL)
    private List<DonationRequest> donationRequests;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Secretary(Integer id) {
        this.id = id;
    }

    public Secretary() {
    }
}
